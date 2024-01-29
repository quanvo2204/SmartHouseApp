package bk.ltuddd.iotapp.feature.main.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;


import com.google.firebase.auth.FirebaseAuth;
import bk.ltuddd.iotapp.core.base.BaseFragment;
import bk.ltuddd.iotapp.data.model.User;
import bk.ltuddd.iotapp.databinding.FragmentUserInfoBinding;
import bk.ltuddd.iotapp.feature.main.viewmodel.MainViewModel;
import bk.ltuddd.iotapp.utils.Constant;

public class UserInfoFragment extends BaseFragment<FragmentUserInfoBinding, MainViewModel> {

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public void onCommonViewLoaded() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(Constant.KEY_UID_PREF,Constant.EMPTY_STRING);
        viewModel.queryUserInfo(uid);
    }

    @Override
    public void addViewListener() {
        viewBinding.btnBack.setOnClickListener(v -> {
            updateUser();
            requireActivity().onBackPressed();
        });
        viewBinding.toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });


    }

    @Override
    public void addDataObserve() {
        viewModel.userInfo().observe(this,user -> {
            viewBinding.edtName.setText(user.getName());
            viewBinding.tvEmailAdress.setText(user.getEmail());
            viewBinding.layoutContent.edtEmail.setText(user.getEmail());
            viewBinding.layoutContent.edtAddress.setText(user.getAddress());
            viewBinding.layoutContent.tvMobile.setText(user.getPhoneNumber());
            viewBinding.layoutContent.edtDob.setText(user.getBirthday());
        });

    }

    @Override
    protected FragmentUserInfoBinding getBinding(LayoutInflater inflater) {
        return FragmentUserInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected String getTagFragment() {
        return UserInfoFragment.class.getSimpleName();
    }

    private void updateUser() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString(Constant.KEY_UID_PREF,Constant.EMPTY_STRING);
        User user1 = new User();
        user1.setName(viewBinding.edtName.getText().toString());
        user1.setEmail(viewBinding.layoutContent.edtEmail.getText().toString());
        user1.setAddress(viewBinding.layoutContent.edtAddress.getText().toString());
        user1.setBirthday(viewBinding.layoutContent.edtDob.getText().toString());
        viewModel.updateUserInfo(user1, uid);
    }
}
