package bk.ltuddd.iotapp.feature.timer.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityTimerBinding;
import bk.ltuddd.iotapp.feature.timer.viewmodel.TimerViewModel;
import bk.ltuddd.iotapp.utils.Constant;

public class TimerActivity extends BaseActivity<ActivityTimerBinding, TimerViewModel> {



    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

    @Override
    protected ActivityTimerBinding getViewBinding() {
        return ActivityTimerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<TimerViewModel> getViewModelClass() {
        return TimerViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.toolBarDetailLighting.setTitle(getString(R.string.activity_add_timer_title));

    }

    @Override
    public void addViewListener() {
        DeviceModel deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_DEVICE_MODEL, deviceModel);
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());
        binding.btnAdd.setOnClickListener(v -> {

        });

    }
}
