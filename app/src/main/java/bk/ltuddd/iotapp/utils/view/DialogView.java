package bk.ltuddd.iotapp.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.function.Consumer;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.databinding.DialogDeviceNameBinding;
import bk.ltuddd.iotapp.databinding.DialogDevicePasswordBinding;

public class DialogView {

    public static void showDialogSerial(
            Activity activity,
            final Consumer<String> listenerPositive
    ) {
        try {
            DialogDevicePasswordBinding binding = DialogDevicePasswordBinding.inflate(LayoutInflater.from(activity));

            final Dialog dialog = new Dialog(activity, R.style.AppThemeNew_DialogTheme);
            dialog.setCancelable(false);
            dialog.setContentView(binding.getRoot());

            binding.btnAccept.setOnClickListener(v -> listenerPositive.accept(binding.edtSerial.getText().toString().trim()));

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        } catch (Exception e) {
            // Handle exception
        }
    }

    public static void showDialogNameDevice(
            Activity activity,
            final Consumer<String> listenerPositive,
            String oldName
    ) {
        try {
            DialogDeviceNameBinding dialogDeviceNameBinding = DialogDeviceNameBinding.inflate(LayoutInflater.from(activity));
            final Dialog dialog = new Dialog(activity, R.style.AppThemeNew_DialogTheme);
            dialog.setContentView(dialogDeviceNameBinding.getRoot());
            dialog.setCancelable(false);
            dialogDeviceNameBinding.edtSerial.setText(oldName);
            dialogDeviceNameBinding.btnAccept.setOnClickListener(v -> {
                listenerPositive.accept(dialogDeviceNameBinding.edtSerial.getText().toString().trim());
                dialog.dismiss();
            });
            dialogDeviceNameBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            // Handle exception
        }
    }

}
