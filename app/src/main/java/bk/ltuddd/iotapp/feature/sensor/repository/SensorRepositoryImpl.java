package bk.ltuddd.iotapp.feature.sensor.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.utils.Constant;
import io.reactivex.Single;

public class SensorRepositoryImpl implements SensorRepository{
    @Override
    public Single<DeviceModel> querySensorBySerial(long serial) {
        return  Single.create(emitter -> {
            DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_DEVICE);
            deviceRef.orderByChild(Constant.NODE_DEVICE_SERIAL)
                    .equalTo(serial)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DeviceModel deviceModel = dataSnapshot.getValue(DeviceModel.class);
                                    emitter.onSuccess(deviceModel);
                                    return;
                                }
                            } else {
                                emitter.onError(new Exception("Serial not found."));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }
}
