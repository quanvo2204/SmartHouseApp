package bk.ltuddd.iotapp.feature.sensor.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import bk.ltuddd.iotapp.core.base.BaseViewModel;
import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.feature.sensor.repository.SensorRepository;
import bk.ltuddd.iotapp.feature.sensor.repository.SensorRepositoryImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SensorViewModel extends BaseViewModel {

    private final SensorRepository sensorRepository = new SensorRepositoryImpl();

    private MutableLiveData<DeviceModel> _deviceModel = new MutableLiveData<>();
    public LiveData<DeviceModel> deviceModel() {
        return _deviceModel;
    }


    public void getSensorBySerial(long serial) {
        compositeDisposable.add(
                sensorRepository.querySensorBySerial(serial)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                sensor -> _deviceModel.setValue(sensor)
                        )
        );
    }

}
