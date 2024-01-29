package bk.ltuddd.iotapp.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivityLoginBinding;
import bk.ltuddd.iotapp.feature.auth.viewmodel.AuthViewModel;
import bk.ltuddd.iotapp.feature.main.ui.activity.MainActivity;
import bk.ltuddd.iotapp.utils.Constant;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, AuthViewModel> {

    boolean isPasswordVisible = false;

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
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
        binding.tvRegister.setOnClickListener(view -> openActivity(RegisterActivity.class));
        binding.btnLogin.setOnClickListener(v -> onClickBtnLogin());
        binding.eyeImage.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.edtPassword.setTransformationMethod(null);
                binding.eyeImage.setSelected(true);
            } else {
                binding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.eyeImage.setSelected(false);
            }
            binding.edtPassword.setSelection(binding.edtPassword.length());
        });
        binding.tvForgotPassword.setOnClickListener(v -> openActivity(ForgetPassActivity.class));
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.userResponse().observe(this,user -> {
            String password = binding.edtPassword.getText().toString();
            String phoneNumber = binding.edtPhone.getText().toString();
            if (password.equals(user.getPassword())) {
                showToastShort(getString(R.string.success_login));
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF,phoneNumber).apply();
                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                showToastShort(getString(R.string.error_login));
            }
        });
    }

    private void onClickBtnLogin() {
        String phoneNumber = binding.edtPhone.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (viewModel.checkValidPhoneNumber(phoneNumber) && viewModel.checkValidPassword(password)) {
            viewModel.loginRequest(phoneNumber, password);
        } else {
            showToastShort(getString(R.string.activity_config_account_alert_config_account_label));
        }
    }

}
