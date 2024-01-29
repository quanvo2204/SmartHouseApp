package bk.ltuddd.iotapp.feature.main.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.function.Consumer;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityAddDeviceBinding;
import bk.ltuddd.iotapp.feature.main.ui.adapter.AddDeviceAdapter;
import bk.ltuddd.iotapp.feature.main.viewmodel.MainViewModel;
import bk.ltuddd.iotapp.utils.Constant;
import bk.ltuddd.iotapp.utils.view.DialogView;

public class AddDeviceActivity extends BaseActivity<ActivityAddDeviceBinding, MainViewModel> {

    private AddDeviceAdapter addDeviceAdapter = new AddDeviceAdapter();
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
    private DeviceModel device = new DeviceModel();

    @Override
    protected ActivityAddDeviceBinding getViewBinding() {
        return ActivityAddDeviceBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.deviceType().observe(this, listDeviceType -> {
            addDeviceAdapter.submitList(listDeviceType);
        });
        viewModel.deviceModel().observe(this,deviceModel -> {
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF,Constant.EMPTY_STRING);
            viewModel.addDeviceToUser(deviceModel, phoneNumber);
            device = deviceModel;
        });

        viewModel.addDeviceToUserSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                onLoading(false);
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_DEVICE, device);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onCommonViewLoaded() {
        viewModel.queryDeviceType();
        binding.rcvDevice.setAdapter(addDeviceAdapter);
        binding.rcvDevice.setLayoutManager(gridLayoutManager);
        addDeviceAdapter.setOnItemClickListener(() -> DialogView.showDialogSerial(AddDeviceActivity.this, consumer));
//        dialogConfirmFragment.setOnPasswordEnteredListener(password -> {
//            if (serialNumber.equals(password)) {
//                openActivity(MainActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    public void addViewListener() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

    }

    private Consumer<String> consumer = s -> {
        if (s.isEmpty()) {
            showToastShort(getString(R.string.serial_empty));
        } else if (s.length() < 8) {
            showToastShort(getString(R.string.serial_invalid));
        } else {
            viewModel.getDeviceBySerial(Long.parseLong(s));
            onLoading(true);
        }
    };
}
