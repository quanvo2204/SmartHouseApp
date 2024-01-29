package bk.ltuddd.iotapp.feature.main.ui.adapter;

import static bk.ltuddd.iotapp.utils.Constant.VIEW_TYPE_DHT11;
import static bk.ltuddd.iotapp.utils.Constant.VIEW_TYPE_LAMP;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.recycleview.BaseRecycleAdapter;
import bk.ltuddd.iotapp.core.recycleview.BaseViewHolder;
import bk.ltuddd.iotapp.data.model.DeviceModel;

import bk.ltuddd.iotapp.databinding.ItemDht11Binding;
import bk.ltuddd.iotapp.databinding.ItemLampBinding;
import bk.ltuddd.iotapp.utils.Constant;

public class MainAdapter extends BaseRecycleAdapter<DeviceModel> {

    private boolean isLightOn = false;
    private OnBtnStateLampClick onBtnStateLampClick;

    private OnItemLongClick onItemLongClick;

    private OnItemClick onItemClick;

    private boolean modeSelectedDevices = false;

    private OnItemClickRemove onItemClickRemove;

    private OnSensorChange onSensorChange;

    private long temp;
    private long humid;

    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
         if (viewType == VIEW_TYPE_DHT11) {
            ItemDht11Binding dht11Binding = ItemDht11Binding.inflate(inflater, parent, false);
            return new DHT11ViewHolder(dht11Binding);
        } else if (viewType == VIEW_TYPE_LAMP) {
            ItemLampBinding lampBinding = ItemLampBinding.inflate(inflater, parent, false);
            return new LampViewHolder(lampBinding);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        DeviceModel deviceModel = mData.get(position);
        if (deviceModel != null) {
            if (deviceModel.getType().equals(Constant.DHT11)) {
                return VIEW_TYPE_DHT11;
            } else if (deviceModel.getType().equals(Constant.SMART_LIGHT)) {
                return VIEW_TYPE_LAMP;
            }
        }
        return super.getItemViewType(position);
    }

    public class DHT11ViewHolder extends BaseDeviceViewHolder<ItemDht11Binding> {

        public DHT11ViewHolder(@NonNull ItemDht11Binding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            DeviceModel deviceModel = mData.get(position);
            getViewBinding().tvHumidity.setText(formatHumid(humid));
            getViewBinding().tvTemperature.setText(formatTemp(temp));
            getViewBinding().imageDevice.setImageResource(R.drawable.sensor);

        }
    }

    public class LampViewHolder extends BaseDeviceViewHolder<ItemLampBinding> {


        public LampViewHolder(@NonNull ItemLampBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            super.bindData(position);
            DeviceModel deviceModel = mData.get(position);
            getViewBinding().imageDevice.setImageResource(R.drawable.smartlight);
            if (deviceModel.getState() == 1) {
                getViewBinding().btnOnOff.setSelected(true);
                getViewBinding().tvState.setText(itemView.getContext().getString(R.string.state_on));
                getViewBinding().tvState.setTextColor(itemView.getContext().getColor(R.color.green_light));
                isLightOn = true;
            } else {
                getViewBinding().btnOnOff.setSelected(false);
                getViewBinding().tvState.setText(itemView.getContext().getString(R.string.state_off));
                getViewBinding().tvState.setTextColor(itemView.getContext().getColor(R.color.grey_light));
                isLightOn = false;
            }
            getViewBinding().btnOnOff.setOnClickListener(v -> {
                if (isLightOn) {
                    onBtnStateLampClick.setOnClickBtnState(deviceModel.getSerial(),0);
                    isLightOn = false;
                } else {
                    onBtnStateLampClick.setOnClickBtnState(deviceModel.getSerial(),1);
                    isLightOn = true;
                }
                getViewBinding().btnOnOff.setSelected(isLightOn);
                if (isLightOn) {
                    getViewBinding().tvState.setText(itemView.getContext().getString(R.string.state_on));
                    getViewBinding().tvState.setTextColor(itemView.getContext().getColor(R.color.green_light));
                } else {
                    getViewBinding().tvState.setText(itemView.getContext().getString(R.string.state_off));
                    getViewBinding().tvState.setTextColor(itemView.getContext().getColor(R.color.grey_light));

                }
            });
        }
    }

    public class BaseDeviceViewHolder<T extends ViewBinding> extends BaseViewHolder<T> {

        public BaseDeviceViewHolder(@NonNull T viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            AppCompatTextView tvDeviceName = getViewBinding().getRoot().findViewById(R.id.tv_name_device);
            AppCompatImageView ivChecked = getViewBinding().getRoot().findViewById(R.id.iv_checked);
            DeviceModel deviceModel = mData.get(position);
            tvDeviceName.setText(deviceModel.getName());
            if (modeSelectedDevices) {
                ivChecked.setVisibility(View.VISIBLE);
                ivChecked.setSelected(mData.get(position).isSelected());
            } else {
                ivChecked.setVisibility(View.GONE);
                mData.get(position).setSelected(false);
            }
            itemView.setOnClickListener(v -> {
                if (onItemClick != null) {
                    onItemClick.setOnItemClick(deviceModel);
                }
            });
            itemView.setOnLongClickListener(v -> {
                modeSelectedDevices = true;
                mData.get(position).setSelected(true);
                ivChecked.setVisibility(View.VISIBLE);
                if (onItemLongClick != null) {
                    onItemLongClick.setOnItemLongClick(deviceModel);
                }
                notifyDataSetChanged();
                return false;
            });
//            itemView.setOnClickListener(v -> {
//                modeSelectedDevices = true;
//                ivChecked.setSelected(!deviceModel.isSelected());
//                deviceModel.setSelected(!deviceModel.isSelected());
//                Log.e("Bello",String.valueOf(deviceModel.isSelected()));
//                if (onItemClickRemove != null) {
//                    onItemClickRemove.setOnItemClickRemove(deviceModel.isSelected());
//                }
//                notifyDataSetChanged();
//            });
        }
    }

    private String formatHumid(double humid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(humid).append(" ").append("%");
        return stringBuilder.toString();
    }

    private String formatTemp(double temp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(temp).append(" ").append("°C");
        return stringBuilder.toString();
    }

    public interface OnBtnStateLampClick {
        void setOnClickBtnState(long serial, int state);
    }

    public interface OnItemClick {
        void setOnItemClick(DeviceModel deviceModel);
    }
    public interface OnItemLongClick {
        void setOnItemLongClick(DeviceModel deviceModel);
    }

    public interface OnItemClickRemove {
        void setOnItemClickRemove(Boolean isSe);
    }

    public void setOnItemClickRemove(OnItemClickRemove onItemClickRemove) {
        this.onItemClickRemove = onItemClickRemove;
    }

    public void setOnBtnStateLampClick(OnBtnStateLampClick onBtnStateLampClick) {
        this.onBtnStateLampClick = onBtnStateLampClick;
    }

    public void setOnItemLongClick(OnItemLongClick onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedAllDevices(boolean isSelected) {
        for (DeviceModel deviceModel : mData) {
            deviceModel.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeDevices(List<DeviceModel> listDeviceSelected) {
        for (DeviceModel deviceModel : listDeviceSelected) {
            while (mData.contains(deviceModel)) {
                mData.remove(deviceModel);
            }
        }
        listDeviceSelected.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onClickCancel() {
        modeSelectedDevices = false;
        notifyDataSetChanged();
    }

    public interface OnSensorChange {

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOnTempChange(long temp) {
        this.temp = temp;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOnHumidChange(long humid) {
        this.humid = humid;
        notifyDataSetChanged();
    }

    public void notifyItemsChanged(List<DeviceModel> deviceModelList) {
        for (DeviceModel deviceModel: deviceModelList) {
            for (int index = 0; index < mData.size(); index++) {
                DeviceModel item = mData.get(index);
                if (item.getSerial() == deviceModel.getSerial()) {
                    mData.get(index).setHumid(deviceModel.getHumid());
                    mData.get(index).setTemp(deviceModel.getTemp());
                    notifyItemChanged(index);
                }
            }
        }
    }


}
