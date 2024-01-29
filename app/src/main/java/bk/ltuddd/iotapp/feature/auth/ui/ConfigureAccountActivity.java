package bk.ltuddd.iotapp.feature.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivityConfigAccountBinding;
import bk.ltuddd.iotapp.feature.auth.viewmodel.AuthViewModel;
import bk.ltuddd.iotapp.feature.main.ui.activity.MainActivity;
import bk.ltuddd.iotapp.utils.Constant;


public class ConfigureAccountActivity extends BaseActivity<ActivityConfigAccountBinding, AuthViewModel> {


    boolean isPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;

    @Override
    protected ActivityConfigAccountBinding getViewBinding() {
        return ActivityConfigAccountBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.registerSuccess.observe(this,registerSuccess -> {
            if (registerSuccess) {
                showToastShort(getString(R.string.success_create_user));
                openActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                showToastShort(getString(R.string.error_create_user));
                onLoading(false);
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
        onLoading(false);
    }

    @Override
    public void addViewListener() {
        binding.btnSend.setOnClickListener(v -> onClickBtnSend());
        binding.eyeImage1.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.customEdtPassword.setTransformationMethod(null);
                binding.eyeImage1.setSelected(true);
            } else {
                binding.customEdtPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.eyeImage1.setSelected(false);
            }
            binding.customEdtPassword.setSelection(binding.customEdtPassword.length());
        });
        binding.eyeImage2.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                binding.customEdtConfirmPassword.setTransformationMethod(null);
                binding.eyeImage2.setSelected(true);
            } else {
                binding.customEdtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.eyeImage2.setSelected(false);
            }
            binding.customEdtConfirmPassword.setSelection(binding.customEdtConfirmPassword.length());
        });
        binding.customToolbar.setNavigationOnClickListener(v -> {
            openActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        });

    }

    private void onClickBtnSend() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String password = binding.customEdtPassword.getText().toString();
        String confirmPassword = binding.customEdtConfirmPassword.getText().toString();
        if (password.isEmpty()) {
            showToastShort(getString(R.string.error_edt_password_null));
        } else {
            if (viewModel.checkValidPassword(binding.customEdtPassword.getText().toString())) {
                if (password.equals(confirmPassword)) {
                    onLoading(true);
                    viewModel.createUserWithPhoneAndPassword(confirmPassword, sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF, Constant.EMPTY_STRING));
                    sharedPreferences.edit().putString(Constant.KEY_UID_PREF, FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                } else {
                    showToastShort(getString(R.string.error_different_password));
                }
            } else {
                showToastShort(getString(R.string.activity_config_account_alert_config_account_label));
            }
        }
    }
}
