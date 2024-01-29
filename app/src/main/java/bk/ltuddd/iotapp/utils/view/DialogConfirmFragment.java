package bk.ltuddd.iotapp.utils.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.databinding.DialogDevicePasswordBinding;
import bk.ltuddd.iotapp.feature.main.ui.activity.MainActivity;
import bk.ltuddd.iotapp.feature.main.viewmodel.MainViewModel;

public class DialogConfirmFragment extends DialogFragment {

    private DialogDevicePasswordBinding dialogDevicePasswordBinding;
    private OnPasswordEnteredListener listener;


    public static DialogConfirmFragment newInstance() {
        return new DialogConfirmFragment();
    }


    public interface OnPasswordEnteredListener {
        void onPasswordEntered(String password);
    }

    public DialogConfirmFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogDevicePasswordBinding = DialogDevicePasswordBinding.inflate(inflater, container, false);
        return dialogDevicePasswordBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogDevicePasswordBinding.btnCancel.setOnClickListener(v -> {
            dismiss();
        });
//        dialogDevicePasswordBinding.btnAccept.setOnClickListener(v -> {
//            String password = dialogDevicePasswordBinding.edtPassword.getText().toString();
////            mainViewModel.queryDeviceBySerial(password);
//        });
    }

    public void setOnPasswordEnteredListener(OnPasswordEnteredListener listener) {
        this.listener = listener;
    }

    private void updateDeviceToUser() {

    }


}
