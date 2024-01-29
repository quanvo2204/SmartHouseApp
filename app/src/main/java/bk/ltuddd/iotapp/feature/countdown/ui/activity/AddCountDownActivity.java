package bk.ltuddd.iotapp.feature.countdown.ui.activity;


import android.widget.RadioGroup;

import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivityAddCountDownBinding;
import bk.ltuddd.iotapp.feature.countdown.viewmodel.CountDownViewModel;


public class AddCountDownActivity extends BaseActivity<ActivityAddCountDownBinding, CountDownViewModel> {

    @Override
    protected ActivityAddCountDownBinding getViewBinding() {
        return ActivityAddCountDownBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<CountDownViewModel> getViewModelClass() {
        return CountDownViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {
        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = (group, checkedId) -> {
            if (group.getCheckedRadioButtonId() == binding.rdOnlyOn.getId()) {
                viewModel.stateLightCountDown = 1;
            } else if (group.getCheckedRadioButtonId() == binding.rdOnlyOff.getId()){
                viewModel.stateLightCountDown = 0;
            }
        };
        binding.rdgAction.setOnCheckedChangeListener(onCheckedChangeListener);


    }

    @Override
    public void addViewListener() {
        binding.btnDrawer.setOnClickListener(v -> onBackPressed());

    }

}
