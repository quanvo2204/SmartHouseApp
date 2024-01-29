package bk.ltuddd.iotapp.feature.smartlight.viewmodel;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseViewModel;
import bk.ltuddd.iotapp.feature.smartlight.repository.SmartLightRepository;
import bk.ltuddd.iotapp.feature.smartlight.repository.SmartLightRepositoryImpl;
import bk.ltuddd.iotapp.utils.livedata.SingleLiveEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SmartLightViewModel extends BaseViewModel {

    public SingleLiveEvent<Boolean> isUpdateSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> isRemoveSuccess = new SingleLiveEvent<>();

    private SmartLightRepository smartLightRepository = new SmartLightRepositoryImpl();

    public void updateLampState(long serial, int state) {
        compositeDisposable.add(
                smartLightRepository.updateStateLamp(serial, state)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(

                        )
        );
    }

    public void updateDeviceName(String name, long serial) {
        compositeDisposable.add(
                smartLightRepository.updateDeviceName(name, serial)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isSuccess -> isUpdateSuccess.setValue(isSuccess),
                                throwable -> {
                                    setErrorStringId(R.string.error_send_otp_from_firebase);
                                }
                        )
        );
    }

    public void removeDevice(long serial, String uid) {
        compositeDisposable.add(
                smartLightRepository.removeDevice(serial, uid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isSuccess -> isRemoveSuccess.setValue(isSuccess),
                                throwable -> {
                                    setErrorStringId(R.string.error_send_otp_from_firebase);
                                }
                        )
        );
    }


}
