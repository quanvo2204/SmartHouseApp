package bk.ltuddd.iotapp.feature.main.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.recycleview.BaseRecycleAdapter;
import bk.ltuddd.iotapp.core.recycleview.BaseViewHolder;
import bk.ltuddd.iotapp.databinding.ItemDeviceTypeBinding;
import bk.ltuddd.iotapp.utils.Constant;

public class AddDeviceAdapter extends BaseRecycleAdapter<String> {

    private OnItemClickListener onItemClickListener;

    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new DeviceTypeViewHolder(
                ItemDeviceTypeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    public class DeviceTypeViewHolder extends BaseViewHolder<ItemDeviceTypeBinding> {

        public DeviceTypeViewHolder(@NonNull ItemDeviceTypeBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            String deviceType = mData.get(position);
            getViewBinding().tvNameDevice.setText(deviceType);
            switch (deviceType) {
                case Constant.DHT11:
                    getViewBinding().imageDevice.setImageResource(R.drawable.sensor);
                    break;
                case Constant.SMART_LIGHT:
                    getViewBinding().imageDevice.setImageResource(R.drawable.smartlight);
                    break;
                default:
                    getViewBinding().imageDevice.setImageResource(R.drawable.smartiot);
                break;
            }
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick();
                }
            });



        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
