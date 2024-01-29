package bk.ltuddd.iotapp.feature.countdown.viewmodel;

import bk.ltuddd.iotapp.core.base.BaseViewModel;
import bk.ltuddd.iotapp.feature.countdown.repository.CountDownRepository;
import bk.ltuddd.iotapp.feature.countdown.repository.CountDownRepositoryImpl;

public class CountDownViewModel extends BaseViewModel {

    private CountDownRepository countDownRepository = new CountDownRepositoryImpl();

    public int stateLightCountDown = 1;

    public int oldHour = 0;

    public int oldMinute = 0;

    public int oldSecond = 0;

    public int hour = 0;

    public int minute = 0;

    public int second = 0;

    public int getStateLightCountDown() {
        return stateLightCountDown;
    }

    public void setStateLightCountDown(int stateLightCountDown) {
        this.stateLightCountDown = stateLightCountDown;
    }

    public int getOldHour() {
        return oldHour;
    }

    public void setOldHour(int oldHour) {
        this.oldHour = oldHour;
    }

    public int getOldMinute() {
        return oldMinute;
    }

    public void setOldMinute(int oldMinute) {
        this.oldMinute = oldMinute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getOldSecond() {
        return oldSecond;
    }

    public void setOldSecond(int oldSecond) {
        this.oldSecond = oldSecond;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
