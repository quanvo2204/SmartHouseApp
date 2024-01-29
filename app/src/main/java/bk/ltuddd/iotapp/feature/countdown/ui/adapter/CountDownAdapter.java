package bk.ltuddd.iotapp.feature.countdown.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.recycleview.BaseRecycleAdapter;
import bk.ltuddd.iotapp.core.recycleview.BaseViewHolder;
import bk.ltuddd.iotapp.data.model.RulesDetailDevice;
import bk.ltuddd.iotapp.databinding.ItemCountdownDevicesBinding;


public class CountDownAdapter extends BaseRecycleAdapter<RulesDetailDevice> {


    @Override
    public BaseViewHolder<?> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new CountDownViewHolder(ItemCountdownDevicesBinding.inflate(inflater, parent, false));
    }

    public class CountDownViewHolder extends BaseViewHolder<ItemCountdownDevicesBinding> {

        public CountDownViewHolder(@NonNull ItemCountdownDevicesBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void bindData(int position) {
            RulesDetailDevice rulesDetailDevice = mData.get(position);
            getViewBinding().tvTime.setText(rulesDetailDevice.getRemainTime());
            if (rulesDetailDevice.getState() == 0) {
                getViewBinding().swActiveTimer.setActivated(false);
                getViewBinding().tvTrait.setText(itemView.getContext().getString(R.string.count_down_off));
            } else {
                getViewBinding().swActiveTimer.setActivated(true);
                getViewBinding().tvTrait.setText(itemView.getContext().getString(R.string.count_down_on));
            }


        }
    }

    public void notifyItemChanged(RulesDetailDevice rulesDetailDevice) {
        for (int index = 0; index <= mData.size(); index++) {
            if (mData.get(index).equals(rulesDetailDevice)) {
                mData.set(index, rulesDetailDevice);
                notifyItemChanged(index);
            }
        }
    }

    public void addItemToList(RulesDetailDevice rulesDetailDevice) {
        mData.add(rulesDetailDevice);
        notifyItemInserted(mData.size() - 1);
    }

    public void removeItemFromList(RulesDetailDevice rulesDetailDevice) {
        for (int index = 0; index <= mData.size(); index++) {
            if (mData.get(index).equals(rulesDetailDevice)) {
                mData.remove(rulesDetailDevice);
                notifyItemRemoved(index);
            }
        }
    }
}
