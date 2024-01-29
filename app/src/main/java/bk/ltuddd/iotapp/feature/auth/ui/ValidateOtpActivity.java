package bk.ltuddd.iotapp.feature.auth.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mukesh.OnOtpCompletionListener;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivityValidateOtpBinding;
import bk.ltuddd.iotapp.feature.auth.viewmodel.AuthViewModel;
import bk.ltuddd.iotapp.utils.Constant;

public class ValidateOtpActivity extends BaseActivity<ActivityValidateOtpBinding, AuthViewModel> {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private OnOtpCompletionListener mOnOtpCompletionListener = otp -> {
        onLoading(true);
        viewModel.verifyCode(otp);
    };

    private CountDownTimer countDownTimer;

    private final String timeFormat = "%02d:%02d";

    private int mCountDown = 90;

    @Override
    protected ActivityValidateOtpBinding getViewBinding() {
        return ActivityValidateOtpBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
//        viewModel.otpToken().observe(this, otpCode -> {
//            if (otpCode != null) {
//                openActivity(ConfigureAccountActivity.class);
//            }
//        });
        viewModel.validateOtpSuccess.observe(this, isSuccess -> {
            if (isSuccess) {
                openActivity(ConfigureAccountActivity.class);
            } else {
                showToastShort(getString(R.string.activity_send_otp_failed_otp_code));
                onLoading(false);
            }
        });
    }

    @Override
    public void onCommonViewLoaded() {
        binding.edtOTP.requestFocus();
        binding.edtOTP.setEnabled(true);
        binding.edtOTP.setClickable(true);
        binding.edtOTP.setOtpCompletionListener(mOnOtpCompletionListener);
        sendOtp();
//        mAuth = FirebaseAuth.getInstance();
//
//        // Set up the authentication state listener
//        // User is already authenticated, navigate away from the login screen
//        mAuthListener = firebaseAuth -> {
//            onLoading(true);
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null) {
//                // User is already authenticated, navigate away from the login screen
//                openActivity(ConfigureAccountActivity.class);
//                finish();
//            }
//        };
    }

    @Override
    public void addViewListener() {
        binding.btnRequestOTP.setOnClickListener(v -> resendOtp());
        binding.customToolbar.setNavigationOnClickListener(v -> {
            countDownTimer.cancel();
            openActivity(LoginActivity.class);
            finish();
        });
    }

    private void setOtpDuration() {
        mCountDown = 90;
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(90000, 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    mCountDown -= 1;
                    binding.tvTimer.setText(String.format(timeFormat,0,mCountDown));
                }

                @SuppressLint("DefaultLocale")
                @Override
                public void onFinish() {
                    mCountDown = 0;
                    binding.tvTimer.setText(String.format(timeFormat,0,0));
                    countDownTimer.cancel();
                }
            };
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void sendOtp() {
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constant.KEY_PHONE_NUMBER_PREF,intent.getStringExtra(Constant.KEY_PHONE_NUMBER)).apply();
        viewModel.requestOtp(intent.getStringExtra(Constant.KEY_PHONE_NUMBER), this);
        setOtpDuration();
    }

    private void resendOtp() {
        countDownTimer.cancel();
        binding.edtOTP.clearComposingText();
        binding.edtOTP.setText(Constant.EMPTY_STRING);
        sendOtp();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }
}
