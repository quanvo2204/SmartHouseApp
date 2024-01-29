package bk.ltuddd.iotapp.feature.auth.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;


import bk.ltuddd.iotapp.core.base.BaseActivity;
import bk.ltuddd.iotapp.databinding.ActivitySplashBinding;
import bk.ltuddd.iotapp.feature.auth.viewmodel.AuthViewModel;
import bk.ltuddd.iotapp.feature.main.ui.activity.MainActivity;
import bk.ltuddd.iotapp.utils.Constant;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, AuthViewModel> {

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<AuthViewModel> getViewModelClass() {
        return AuthViewModel.class;
    }

    @Override
    public void addDataObserve() {
        super.addDataObserve();
        viewModel.isAccountExisted.observe(this, isAccountExisted -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (isAccountExisted) {
                    openActivity(MainActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
                finish();
            }, 5000);
        });
//        Handler handler = new Handler();
//        handler.postDelayed(() -> {
//                if (isAccountExisted) {
//                    openActivity(MainActivity.class);
//                } else {
//                    openActivity(LoginActivity.class);
//                }
//            openActivity(LoginActivity.class);
//            finish();
//        }, 5000);
    }

    @Override
    public void onCommonViewLoaded() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(Constant.KEY_PHONE_NUMBER_PREF, Constant.EMPTY_STRING);
        viewModel.checkExistedAccount(phoneNumber);
    }

    @Override
    public void addViewListener() {

    }



}
