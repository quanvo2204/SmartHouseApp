package bk.ltuddd.iotapp.feature.main.repository;

import java.util.List;
import java.util.concurrent.Flow;

import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.data.model.User;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface MainRepository {

    Completable updateUserInfo(User user, String userUid);
    Single<User> queryUser(String userUid);
    Single<List<String>> queryDeviceType();
    Single<DeviceModel> queryDeviceBySerial(long serial);
    Single<List<DeviceModel>> getListDevice(List<Long> serials);
    Single<Boolean> updateStateLamp(long serial,int state);
    Single<Boolean> updateUserDevice(DeviceModel deviceModel, String phoneNumber);
    Single<List<DeviceModel>> getListUserDevice(String phoneNumber);
    Completable removeDevice(List<String> listDeviceName, String phoneNumber);

    Single<List<Long>> getListSensorSerial(String type, String uid);

    Single<List<DeviceModel>> observeHumidTemperature(List<Long> serials);

}
