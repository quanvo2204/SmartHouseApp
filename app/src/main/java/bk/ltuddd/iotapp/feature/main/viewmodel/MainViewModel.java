package bk.ltuddd.iotapp.feature.main.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import bk.ltuddd.iotapp.R;
import bk.ltuddd.iotapp.core.base.BaseViewModel;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.data.model.User;
import bk.ltuddd.iotapp.feature.main.repository.MainRepository;
import bk.ltuddd.iotapp.feature.main.repository.MainRepositoryImpl;
import bk.ltuddd.iotapp.utils.livedata.SingleLiveEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {

    private final MainRepository mainRepository = new MainRepositoryImpl();

    public SingleLiveEvent<Boolean> updateUserSuccess = new SingleLiveEvent<>();

    private MutableLiveData<User> _userInfo = new MutableLiveData<>();

    public LiveData<User> userInfo() {
        return _userInfo;
    }

    private MutableLiveData<List<String>> _deviceType = new MutableLiveData<>();

    public LiveData<List<String>> deviceType() {
        return _deviceType;
    }

    private MutableLiveData<List<DeviceModel>> _listDeviceModel = new MutableLiveData<>();
    public LiveData<List<DeviceModel>> listDeviceModel() {
        return _listDeviceModel;
    }

    private MutableLiveData<DeviceModel> _deviceModel = new MutableLiveData<>();
    public LiveData<DeviceModel> deviceModel() {
        return _deviceModel;
    }

    public SingleLiveEvent<Boolean> isUpdateSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> addDeviceToUserSuccess = new SingleLiveEvent<>();

    private MutableLiveData<List<DeviceModel>> _listUserDeviceModel = new MutableLiveData<>();
    public LiveData<List<DeviceModel>> listUserDeviceModel() {
        return _listUserDeviceModel;
    }

    public List<DeviceModel> listDeviceSelected = new ArrayList<>();

    private MutableLiveData<List<DeviceModel>> _sensorState = new MutableLiveData<>();
    public LiveData<List<DeviceModel>> sensorState() {
        return _sensorState;
    }

    private MutableLiveData<List<Long>> _sensorSerials= new MutableLiveData<>();
    public LiveData<List<Long>> sensorSerials() {
        return _sensorSerials;
    }


    public void updateUserInfo(User user, String userUid) {
        compositeDisposable.add(
        mainRepository.updateUserInfo(user, userUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> updateUserSuccess.setValue(true), throwable -> updateUserSuccess.setValue(false)
                ));
    }

    public void queryUserInfo(String userUid) {
        compositeDisposable.add(
                mainRepository.queryUser(userUid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userInfo -> {
                                    _userInfo.setValue(userInfo);
                                }, throwable -> {

                                }
                        )
        );
    }

    public void queryDeviceType() {
        compositeDisposable.add(
                mainRepository.queryDeviceType()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                deviceType -> _deviceType.setValue(deviceType),
                                throwable -> setErrorStringId(R.string.error_send_otp_from_firebase)
                        )
        );
    }

    public void getListDevice(List<Long> serials) {
        compositeDisposable.add(
                mainRepository.getListDevice(serials)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listDeviceModel -> _listDeviceModel.setValue(listDeviceModel),
                                throwable -> setErrorStringId(R.string.error_send_otp_from_firebase)
                        )
        );
    }

    public void updateLampState(long serial, int state) {
        compositeDisposable.add(
                mainRepository.updateStateLamp(serial,state)
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

    public void getDeviceBySerial(long serial) {
        compositeDisposable.add(
                mainRepository.queryDeviceBySerial(serial)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                device -> _deviceModel.setValue(device),
                                throwable -> {
                                    setErrorStringId(R.string.serial_not_exist);
                                    setLoading(false);
                                }
                        )
        );
    }

    public void addDeviceToUser(DeviceModel deviceModel, String phoneNumber) {
        compositeDisposable.add(
                mainRepository.updateUserDevice(deviceModel, phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isSuccess -> {
                                    addDeviceToUserSuccess.setValue(isSuccess);
                                }
                        )
        );
    }

    public void getListUserDevice(String phoneNumber) {
        compositeDisposable.add(
                mainRepository.getListUserDevice(phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listUserDevice -> {
                                    _listUserDeviceModel.setValue(listUserDevice);
                                }, throwable -> setErrorStringId(R.string.error_send_otp_from_firebase)

                        )
        );
    }

    public void getListSensorSerial(String type, String uid) {
        compositeDisposable.add(
                mainRepository.getListSensorSerial(type, uid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listSensorSerial -> {
                                    _sensorSerials.setValue(listSensorSerial);
                                }, throwable -> setErrorStringId(R.string.error_send_otp_from_firebase)

                        )
        );
    }

    public void removeDevice(List<String> listDeviceName, String phoneNumber) {
        compositeDisposable.add(
                mainRepository.removeDevice(listDeviceName, phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    public void observeTempHumid(List<Long> serials) {
        compositeDisposable.add(
                mainRepository.observeHumidTemperature(serials)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listSensor -> {
                                    _sensorState.setValue(listSensor);
                                    Log.e("Bello","abcd");
                                },
                                throwable -> Log.e("Error: ", "Observe sensor failed" + throwable.toString())
                        )
        );
    }




}
