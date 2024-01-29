package bk.ltuddd.iotapp.utils.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.shawnlin.numberpicker.NumberPicker;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.databinding.CustomNumberPickerBinding;

public class CustomNumberPicker extends LinearLayout {

    private CustomNumberPickerBinding binding;

    private Runnable scrollListener;


    public CustomNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = CustomNumberPickerBinding.inflate(layoutInflater, this, false);
        binding.pickerHour.setOnScrollListener((view, scrollState) -> {
            if (scrollListener != null) {
                scrollListener.run();
            }
        });
        binding.pickerMinute.setOnScrollListener((view, scrollState) -> {
            if (scrollListener != null) {
                scrollListener.run();
            }
        });
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker,0 ,0);

    }

    public int getHour() {
        return binding.pickerHour.getValue();
    }

    public int getMinute() {
        return binding.pickerMinute.getValue();
    }

    public int getSecond() {
        return binding.pickerSecond.getValue();
    }

    public void setHour(int hour) {
        if (hour >=0 && hour <= 23) {
            scrollToValue(binding.pickerHour, hour);
        }
    }

    public void setMinute(int minute) {
        if (minute >=0 && minute <= 59) {
            scrollToValue(binding.pickerMinute, minute);
        }
    }

    public void setSecond(int second) {
        if (second >= 0 && second <= 59) {
            scrollToValue(binding.pickerSecond, second);
        }
    }

    private void scrollToValue(NumberPicker numberPicker, int value) {
        numberPicker.setValue(value);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.hours = getHour();
        savedState.minutes = getMinute();
        savedState.seconds = getSecond();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            setHour(savedState.hours);
            setMinute(savedState.minutes);
            setSecond(savedState.seconds);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private static class SavedState extends BaseSavedState {
        int hours;
        int minutes;
        int seconds;

        public SavedState(Parcel source) {
            super(source);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(hours);
            out.writeInt(minutes);
            out.writeInt(seconds);
        }


        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
