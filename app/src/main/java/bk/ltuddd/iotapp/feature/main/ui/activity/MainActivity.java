package bk.ltuddd.iotapp.feature.main.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityMainBinding;
import bk.ltuddd.iotapp.feature.auth.ui.LoginActivity;
import bk.ltuddd.iotapp.feature.main.ui.adapter.MainAdapter;
import bk.ltuddd.iotapp.feature.main.ui.fragment.UserInfoFragment;
import bk.ltuddd.iotapp.feature.main.viewmodel.MainViewModel;
import bk.ltuddd.iotapp.feature.sensor.ui.TemperatureSensorActivity;
import bk.ltuddd.iotapp.feature.smartlight.ui.activity.SmartLightActivity;
import bk.ltuddd.iotapp.utils.Constant;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainAdapter.OnItemClickRemove {

    private MainAdapter mainAdapter = new MainAdapter();
    private final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
    private List<Long> listDeviceSerials = new ArrayList<>();
    private List<DeviceModel> listDevice = new ArrayList<>();

    private List<String> listDeviceName = new ArrayList<>();
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_DETAIL_LIGHT = 2;

    private static final int REQUEST_CODE_DETAIL_SENSOR = 3;

    private boolean isSelectedAllDevices = false;



    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.rcvDevice.setAdapter(mainAdapter);
        binding.rcvDevice.setLayoutManager(gridLayoutManager);
        mainAdapter.setOnBtnStateLampClick((serial, state) -> viewModel.updateLampState(serial,state));
        mainAdapter.setOnItemLongClick(deviceModel -> {
            binding.layoutBottomSelectedView.getRoot().setVisibility(View.VISIBLE);
            if (deviceModel.isSelected()) {
                addListDeviceIsRemove(deviceModel);
            }
        });
        mainAdapter.setOnItemClickRemove(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
        viewModel.getListUserDevice(phoneNumber);
        onLoading(false);
        onClickNavigationDrawer();

        FirebaseDatabase.getInstance().getReference(Constant.NODE_DEVICE).child("Device4")
                .child(Constant.NODE_TEMPERATURE)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long temp = (long) snapshot.getValue();
                mainAdapter.setOnTempChange(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference(Constant.NODE_DEVICE).child("Device4")
                .child(Constant.NODE_HUMID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long humid = (long) snapshot.getValue();
                        mainAdapter.setOnHumidChange(humid);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();

        viewModel.listUserDeviceModel().observe(this, listUserDevice -> {
            for (int i = 0; i < listUserDevice.size(); i++) {
                listDeviceSerials.add(listUserDevice.get(i).getSerial());
                if (Objects.equals(listUserDevice.get(i).getType(), Constant.DHT11)) {
                    Log.e("Bello","observed");
                    SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                    String uid = sharedPreferences.getString(Constant.KEY_UID_PREF,Constant.EMPTY_STRING);
                    Log.e("Bello","uid: " + uid);
                    viewModel.getListSensorSerial(listUserDevice.get(i).getType(), uid);
                }
            }
            viewModel.getListDevice(listDeviceSerials);
        });

        viewModel.listDeviceModel().observe(this, listDeviceModel -> {
            mainAdapter.submitList(listDeviceModel);
        });

        viewModel.sensorSerials().observe(this, listDeviceSerials -> {
            viewModel.observeTempHumid(listDeviceSerials);
            Log.e("Bello","listSerial:" + listDeviceSerials);
        });

        viewModel.sensorState().observe(this, listSensor -> {
            Log.e("Bello","sensor:" + listSensor);
            mainAdapter.notifyItemsChanged(listSensor);
        });
    }

    @Override
    public void addViewListener() {
        binding.navHeaderMain.tvLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            binding.dlLayout.closeDrawer(GravityCompat.START);
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF, Constant.EMPTY_STRING).apply();
            openActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK,Intent.FLAG_ACTIVITY_NEW_TASK,Intent.FLAG_ACTIVITY_CLEAR_TASK);
        });
        binding.navHeaderMain.ivEmail.setOnClickListener(v -> {
            addFragment(UserInfoFragment.newInstance(),true);
            binding.dlLayout.closeDrawer(GravityCompat.START);
        });
        binding.btnDrawer.setOnClickListener(v -> {
            if (binding.dlLayout.isDrawerOpen(GravityCompat.START)) {
                binding.dlLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.dlLayout.openDrawer(GravityCompat.START);
                binding.navHeaderMain.navHeaderMain.bringToFront();
                binding.navHeaderMain.navHeaderMain.requestFocus();
            }
        });

        binding.navHeaderMain.btnHome.setOnClickListener(v -> {
            binding.dlLayout.closeDrawer(GravityCompat.START);
        });

        binding.btnAdd.setOnClickListener(v -> {
            openActivityForResult(AddDeviceActivity.class,REQUEST_CODE);
            viewModel.queryDeviceType();

        });

        binding.layoutBottomSelectedView.imgCancel.setOnClickListener(v -> {
            binding.layoutBottomSelectedView.getRoot().setVisibility(View.GONE);
            binding.layoutBottomSelectedView.imgAddAllDevices.setSelected(false);
            mainAdapter.onClickCancel();
        });

        binding.layoutBottomSelectedView.imgAddAllDevices.setOnClickListener(v -> {
            binding.layoutBottomSelectedView.imgAddAllDevices.setSelected(!isSelectedAllDevices);
            mainAdapter.setSelectedAllDevices(!isSelectedAllDevices);
            isSelectedAllDevices = !isSelectedAllDevices;
        });

        binding.layoutBottomSelectedView.imgDeleteScene.setOnClickListener(v -> {
            for (int i = 0; i < viewModel.listDeviceSelected.size(); i++) {
                listDeviceName.add(viewModel.listDeviceSelected.get(i).getName());
            }
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
            viewModel.removeDevice(listDeviceName, phoneNumber);
            binding.layoutBottomSelectedView.getRoot().setVisibility(View.GONE);
            mainAdapter.removeDevices(viewModel.listDeviceSelected);
            mainAdapter.onClickCancel();
        });
        mainAdapter.setOnItemClick(deviceModel -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.KEY_DEVICE_MODEL, deviceModel);
            Log.e("Bello","type: " + deviceModel.getType());
            switch (deviceModel.getType()) {
                case Constant.SMART_LIGHT: openActivityWithDataForResult(SmartLightActivity.class, bundle, REQUEST_CODE_DETAIL_LIGHT);
                break;
                case Constant.DHT11: openActivityWithDataForResult(TemperatureSensorActivity.class, bundle, REQUEST_CODE_DETAIL_SENSOR);
                break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    DeviceModel deviceModel = (DeviceModel) data.getSerializableExtra(Constant.KEY_DEVICE);
                    listDevice.clear();
                    listDevice.add(deviceModel);
                    mainAdapter.submitList(listDevice);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Xử lý khi Activity B bị hủy
            }
        } else if (requestCode == REQUEST_CODE_DETAIL_LIGHT) {
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF, Constant.EMPTY_STRING);
                Log.e("Bello", "here are you");
                mainAdapter.clearList();
                viewModel.getListUserDevice(phoneNumber);
            }
        }
    }


    private void onClickNavigationDrawer() {
        Window window = getWindow();
        binding.dlLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                float angle = slideOffset * 180;
                binding.btnDrawer.setRotation(angle);
                binding.navHeaderMain.navHeaderMain.bringToFront();
                binding.navHeaderMain.navHeaderMain.requestFocus();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                binding.btnDrawer.setRotation(90f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.setStatusBarColor(getColor(R.color.transparent));
                };

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                binding.btnDrawer.setRotation(0f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.setStatusBarColor(getColor(R.color.blue_gradient_end));
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void addListDeviceIsRemove(DeviceModel deviceModel) {

            if (!viewModel.listDeviceSelected.contains(deviceModel)) {
                viewModel.listDeviceSelected.add(deviceModel);
            }
    }

    @Override
    public void setOnItemClickRemove(Boolean is) {
        Log.e("Allo", String.valueOf(is));
    }
}