package bk.ltuddd.iotapp.feature.sensor.repository;

import bk.ltuddd.iotapp.data.model.DeviceModel;
import io.reactivex.Single;

public interface SensorRepository {
    Single<DeviceModel> querySensorBySerial(long serial);

}
