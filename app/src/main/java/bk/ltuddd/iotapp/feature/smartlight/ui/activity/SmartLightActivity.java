package bk.ltuddd.iotapp.feature.smartlight.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityDetailLightingBinding;
import bk.ltuddd.iotapp.feature.countdown.ui.activity.CountDownActivity;
import bk.ltuddd.iotapp.feature.smartlight.viewmodel.SmartLightViewModel;
import bk.ltuddd.iotapp.utils.Constant;

public class SmartLightActivity extends BaseActivity<ActivityDetailLightingBinding, SmartLightViewModel> {

    private boolean isLightOn = false;
    private static final int REQUEST_CODE_1 = 759;
    private static final int REQUEST_CODE_2 = 760;

    @Override
    protected ActivityDetailLightingBinding getViewBinding() {
        return ActivityDetailLightingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<SmartLightViewModel> getViewModelClass() {
        return SmartLightViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        DeviceModel deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        binding.tvNameLighting.setText(deviceModel.getName());
        if (deviceModel.getState() == 1) {
            isLightOn = true;
            binding.icTurnOnOff.setSelected(true);
        } else {
            isLightOn = false;
            binding.icTurnOnOff.setSelected(false);
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    public void addViewListener() {
        DeviceModel deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_DEVICE_MODEL, deviceModel);
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());
        binding.icTurnOnOff.setOnClickListener(v -> {
            if (isLightOn) {
                viewModel.updateLampState(deviceModel.getSerial(), 0);
                isLightOn = false;
            } else {
                viewModel.updateLampState(deviceModel.getSerial(), 1);
                isLightOn = true;
            }
            binding.icTurnOnOff.setSelected(isLightOn);
        });
        binding.layoutTimerTool.llAlarm.setOnClickListener(v -> {

        });
        binding.layoutTimerTool.llCountdown.setOnClickListener(v -> openActivityWithDataForResult(CountDownActivity.class, bundle, REQUEST_CODE_1));
        binding.btnAdd.setOnClickListener(v -> openActivityWithDataForResult(SettingSmartLightActivity.class, bundle, REQUEST_CODE_2));
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_2) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String deviceName = data.getStringExtra(Constant.KEY_DEVICE_NAME);
                    binding.tvNameLighting.setText(deviceName);
                }
            }
        }
    }

}
