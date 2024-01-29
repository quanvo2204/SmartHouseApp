package bk.ltuddd.iotapp.feature.countdown.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.databinding.ActivityCountDownBinding;
import bk.ltuddd.iotapp.feature.countdown.ui.activity.AddCountDownActivity;
import bk.ltuddd.iotapp.feature.countdown.viewmodel.CountDownViewModel;
import bk.ltuddd.iotapp.feature.countdown.ui.adapter.CountDownAdapter;
import bk.ltuddd.iotapp.utils.Constant;

public class CountDownActivity extends BaseActivity<ActivityCountDownBinding, CountDownViewModel> {

    private final int REQUEST_CODE = 160;

    private final CountDownAdapter countDownAdapter = new CountDownAdapter();

    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

    @Override
    protected ActivityCountDownBinding getViewBinding() {
        return ActivityCountDownBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<CountDownViewModel> getViewModelClass() {
        return CountDownViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        binding.rcvListCountDown.setAdapter(countDownAdapter);
        binding.rcvListCountDown.setLayoutManager(linearLayoutManager);
        binding.toolBarDetailLighting.setTitle(getString(R.string.activity_add_count_down_title));

    }

    @Override
    public void addViewListener() {
        DeviceModel deviceModel = (DeviceModel) getIntent().getSerializableExtra(Constant.KEY_DEVICE_MODEL);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_DEVICE_MODEL, deviceModel);
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());
        binding.btnAdd.setOnClickListener(v -> {
            openActivityWithDataForResult(AddCountDownActivity.class, bundle, REQUEST_CODE);
        });

    }
}
