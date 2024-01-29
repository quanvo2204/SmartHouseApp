package bk.ltuddd.iotapp.feature.smartlight.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bk.ltuddd.iotapp.utils.Constant;
import io.reactivex.Single;

public class SmartLightRepositoryImpl implements SmartLightRepository{

    @Override
    public Single<Boolean> updateStateLamp(long serial, int state) {
        DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_DEVICE);
        return Single.create(emitter -> {
            deviceRef.orderByChild(Constant.NODE_DEVICE_SERIAL)
                    .equalTo(serial)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String deviceKey = dataSnapshot.getKey();
                                DatabaseReference databaseReference = deviceRef.child(deviceKey).child(Constant.NODE_LAMP_STATE);
                                databaseReference.setValue(state)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                emitter.onSuccess(true);
                                            } else {
                                                emitter.onError(task.getException());
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<Boolean> updateDeviceName(String name, long serial) {
        DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_DEVICE);
        return Single.create( emitter -> deviceRef.orderByChild(Constant.NODE_DEVICE_SERIAL)
                .equalTo(serial)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String deviceKey = dataSnapshot.getKey();
                            DatabaseReference databaseReference = deviceRef.child(deviceKey).child(Constant.NODE_DEVICE_NAME);
                            databaseReference.setValue(name)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            emitter.onSuccess(true);
                                        } else {
                                            emitter.onError(task.getException());
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                })

        );
    }

    @Override
    public Single<Boolean> removeDevice(long serial, String uid) {
        DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_USER);
        DatabaseReference userDeviceRef = deviceRef.child(uid).child(Constant.NODE_USER_DEVICE);
        return Single.create( emitter ->
                userDeviceRef
                .orderByChild(Constant.NODE_DEVICE_SERIAL)
                .equalTo(serial)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.e("Bello","onHere");
//                                String deviceKey = snapshot.child("Đèn 1").getKey();
                                Log.e("Bello","deviceKey: " + userDeviceRef);

                                userDeviceRef.removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        emitter.onSuccess(true);
                                    } else {
                                        emitter.onError(task.getException());
                                    }
                                });
                            }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                })
        );
    }
}
