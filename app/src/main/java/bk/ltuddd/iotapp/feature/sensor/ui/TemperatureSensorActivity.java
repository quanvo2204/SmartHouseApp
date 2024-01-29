package bk.ltuddd.iotapp.feature.sensor.ui;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityTemperatureSensorBinding;
import bk.ltuddd.iotapp.feature.sensor.viewmodel.SensorViewModel;
import bk.ltuddd.iotapp.utils.Constant;

public class TemperatureSensorActivity extends BaseActivity<ActivityTemperatureSensorBinding, SensorViewModel> {

    @Override
    protected ActivityTemperatureSensorBinding getViewBinding() {
        return ActivityTemperatureSensorBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<SensorViewModel> getViewModelClass() {
        return SensorViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.deviceModel().observe(this, sensorResponse -> {
            binding.tvTemp.setText(formatTemp(sensorResponse.getTemp()));
            binding.tvPercentHumidity.setText(formatHumid(sensorResponse.getHumid()));
            binding.tvNameSensor.setText(sensorResponse.getName());
        });
    }

    @Override
    public void onCommonViewLoaded() {
        Intent intent = new Intent();
        DeviceModel deviceModel = new DeviceModel();
        deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        viewModel.getSensorBySerial(deviceModel.getSerial());
        Window window = getWindow();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constant.NODE_DEVICE);
        Query sensorQuery = databaseReference.orderByChild(Constant.NODE_DEVICE_SERIAL).equalTo(deviceModel.getSerial());
        sensorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot sensorSnapshot : snapshot.getChildren()) {
                        DeviceModel deviceModel = sensorSnapshot.getValue(DeviceModel.class);
                        if (deviceModel != null) {
                            Log.e("Bello","change: " + deviceModel.getSerial());
                            binding.tvTemp.setText(formatTemp(deviceModel.getTemp()));
                            binding.tvPercentHumidity.setText(formatHumid(deviceModel.getHumid()));
                        }

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    public void addViewListener() {
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());
    }

    private String formatTemp(long temp) {
        return temp + " " + "°C";
    }
    private String formatHumid(long humid) {
        return humid + " " + "%";
    }
}
