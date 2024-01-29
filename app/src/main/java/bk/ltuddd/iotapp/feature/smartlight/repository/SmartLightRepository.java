package bk.ltuddd.iotapp.feature.smartlight.repository;

import io.reactivex.Single;

public interface SmartLightRepository {

    Single<Boolean> updateStateLamp(long serial, int state);

    Single<Boolean> updateDeviceName(String name, long serial);

    Single<Boolean> removeDevice(long serial, String uid);

}
