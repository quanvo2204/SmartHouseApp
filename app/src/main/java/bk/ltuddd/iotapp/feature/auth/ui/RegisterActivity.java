package bk.ltuddd.iotapp.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivityRegisterBinding;
import bk.ltuddd.iotapp.feature.auth.viewmodel.AuthViewModel;
import bk.ltuddd.iotapp.utils.Constant;


public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, AuthViewModel> {


    @Override
    protected ActivityRegisterBinding getViewBinding() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void onCommonViewLoaded() {

    }

    @Override
    public void addViewListener() {
        binding.btnNext.setOnClickListener(v -> {
//            Intent intent = new Intent(this,ValidateOtpActivity.class);
//            intent.putExtra(Constant.KEY_PHONE_NUMBER,binding.edtUsername.getText().toString());
//            startActivity(intent);
            String phoneNumber = binding.edtUsername.getText().toString();
           if (viewModel.checkValidPhoneNumber(phoneNumber)) {
               viewModel.checkExistedAccount(phoneNumber);
           } else {
               showToastShort(getString(R.string.activity_add_member_Invalid_format_Phone_number));
           }
        });
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void addDataObserve() {
        viewModel.isAccountExisted.observe(this, isExisted -> {
            if (isExisted) {
                showToastShort(getString(R.string.error_account_existed));
            } else {
                Intent intent = new Intent(this,ValidateOtpActivity.class);
                intent.putExtra(Constant.KEY_PHONE_NUMBER,binding.edtUsername.getText().toString());
                startActivity(intent);
            }
        });

    }
}
