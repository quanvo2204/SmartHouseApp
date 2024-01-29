package bk.ltuddd.iotapp.feature.smartlight.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.function.Consumer;

import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivitySettingChildDeviceBinding;
import bk.ltuddd.iotapp.feature.main.ui.activity.MainActivity;
import bk.ltuddd.iotapp.feature.smartlight.viewmodel.SmartLightViewModel;
import bk.ltuddd.iotapp.utils.Constant;
import bk.ltuddd.iotapp.utils.view.DialogView;

public class SettingSmartLightActivity extends BaseActivity<ActivitySettingChildDeviceBinding, SmartLightViewModel> {

    private long serial;

    @Override
    protected ActivitySettingChildDeviceBinding getViewBinding() {
        return ActivitySettingChildDeviceBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<SmartLightViewModel> getViewModelClass() {
        return SmartLightViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isUpdateSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                onLoading(false);
            }
        });

        viewModel.isRemoveSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                onLoading(false);
                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
        DeviceModel deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        serial = deviceModel.getSerial();
        binding.tvChildDeviceName.setText(deviceModel.getName());

    }

    @Override
    public void addViewListener() {
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());
        binding.clBoxNameDevice.setOnClickListener(v -> DialogView.showDialogNameDevice(this, consumer, binding.tvChildDeviceName.getText().toString()));
        binding.clBoxRemoveDevice.setOnClickListener(v -> {
            onLoading(true);
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            String uid = sharedPreferences.getString(Constant.KEY_UID_PREF, Constant.EMPTY_STRING);
            Log.e("Bello","serial: " + serial);
            viewModel.removeDevice(serial, uid);
        });

    }

    private final Consumer<String> consumer = s -> {
        onLoading(true);
        viewModel.updateDeviceName(s, serial);
        binding.tvChildDeviceName.setText(s);
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_DEVICE_NAME, binding.tvChildDeviceName.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
