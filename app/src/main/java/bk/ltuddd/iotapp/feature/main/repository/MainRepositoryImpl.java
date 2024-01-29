package bk.ltuddd.iotapp.feature.main.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;

import bk.ltuddd.iotapp.data.model.DeviceModel;
import bk.ltuddd.iotapp.data.model.User;
import bk.ltuddd.iotapp.utils.Constant;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MainRepositoryImpl implements MainRepository {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public Completable updateUserInfo(User user, String userUid) {
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put(Constant.NODE_USER_NAME, user.getName());
        updateValues.put(Constant.NODE_USER_EMAIL, user.getEmail());
        updateValues.put(Constant.NODE_USER_ADDRESS, user.getAddress());
        updateValues.put(Constant.NODE_USER_BIRTHDAY, user.getBirthday());
        return Completable.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .child(userUid)
                    .updateChildren(updateValues)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(new Throwable(task.getException()));
                        }
                    });
        });
    }

    @Override
    public Single<User> queryUser(String userUid) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_USER)
                    .child(userUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    emitter.onSuccess(user);
                                    return;
                                }
                            }
                            emitter.onError(new Exception("Query failed"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<List<String>> queryDeviceType() {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constant.NODE_DEVICE_TYPE)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> deviceTypeList = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String deviceType = dataSnapshot.getKey();
                                    deviceTypeList.add(deviceType);
                                }

                            }
                            emitter.onSuccess(deviceTypeList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            emitter.onError(error.toException());
                        }
                    });
        });
    }

    @Override
    public Single<DeviceModel> queryDeviceBySerial(long serial) {
        return Single.create(emitter -> {
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

    @Override
    public Single<List<DeviceModel>> getListDevice(List<Long> serials) {
        DatabaseReference deviceRef = firebaseDatabase.getReference(Constant.NODE_DEVICE);

        return Single.create(emitter -> {
            deviceRef.orderByChild(Constant.NODE_DEVICE_SERIAL)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<DeviceModel> listDeviceModel = new ArrayList<>();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    DeviceModel deviceModel = dataSnapshot.getValue(DeviceModel.class);
                                    if (deviceModel != null && serials.contains(deviceModel.getSerial())) {
                                        listDeviceModel.add(deviceModel);
                                    }
                                }
                                emitter.onSuccess(listDeviceModel);
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
    public Single<Boolean> updateUserDevice(DeviceModel deviceModel, String phoneNumber) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(Constant.NODE_USER);
        return Single.create(emitter -> {
            Query query = userReference.orderByChild(Constant.NODE_USER_PHONE_NUMBER).equalTo(phoneNumber);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String userUid = userSnapshot.getKey();
                            DatabaseReference userDeviceSerial = userReference.child(userUid)
                                    .child(Constant.NODE_USER_DEVICE)
                                    .child(deviceModel.getName());
                            userDeviceSerial.setValue(deviceModel)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            emitter.onSuccess(true);
                                        } else {
                                            emitter.onSuccess(false);
                                        }
                                    });
                        }
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
    public Single<List<DeviceModel>> getListUserDevice(String phoneNumber) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constant.NODE_USER);
        return Single.create(emitter -> {
            usersRef.orderByChild(Constant.NODE_USER_PHONE_NUMBER)
                    .equalTo(phoneNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                List<DeviceModel> listDeviceModel = new ArrayList<>();
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    DataSnapshot deviceSnapshot = userSnapshot.child(Constant.NODE_USER_DEVICE);
                                    for (DataSnapshot deviceChildSnapshot : deviceSnapshot.getChildren()) {
                                        DeviceModel deviceModel = deviceChildSnapshot.getValue(DeviceModel.class);
                                        if (deviceModel != null) {
                                            listDeviceModel.add(deviceModel);
                                        }
                                    }
                                }
                                emitter.onSuccess(listDeviceModel);
                            } else {
                                emitter.onError(new Exception());
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
    public Completable removeDevice(List<String> listDeviceName, String phoneNumber) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constant.NODE_USER);
        return Completable.create(emitter -> {
            Query phoneNumberQuery = usersRef.orderByChild(Constant.NODE_USER_PHONE_NUMBER).equalTo(phoneNumber);
            phoneNumberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            for (String deviceName : listDeviceName) {
                                DatabaseReference deviceRef = usersRef.child(userId).child(Constant.NODE_USER_DEVICE).child(deviceName);
                                deviceRef.removeValue();
                            }
                        }
                        emitter.onComplete();
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
    public Single<List<Long>> getListSensorSerial(String type, String uid) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constant.NODE_USER).child(uid).child(Constant.NODE_USER_DEVICE);
        return Single.create(emitter -> {
            Query phoneNumberQuery = databaseReference.orderByChild(Constant.NODE_TYPE).equalTo(type);
            phoneNumberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<Long> listSensorSerial = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Long serial = dataSnapshot.child(Constant.NODE_DEVICE_SERIAL).getValue(Long.class);
                                if (serial != null) {
                                    listSensorSerial.add(serial);
                                }

                        }
                        emitter.onSuccess(listSensorSerial);
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
    public Single<List<DeviceModel>> observeHumidTemperature(List<Long> serials) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constant.NODE_DEVICE);
        return Single.create(
                emitter -> {
                    for (long serial : serials) {
                        Query sensorQuery = databaseReference.orderByChild(Constant.NODE_DEVICE_SERIAL).equalTo(serial);
                        sensorQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    List<DeviceModel> listDeviceModel = new ArrayList<>();
                                    for (DataSnapshot sensorSnapshot : snapshot.getChildren()) {
                                        DeviceModel deviceModel = sensorSnapshot.getValue(DeviceModel.class);
                                        if (deviceModel != null) {
                                            Log.e("Bello","change: " + deviceModel.getSerial());
                                            listDeviceModel.add(deviceModel);
                                        }

                                    }
                                    emitter.onSuccess(listDeviceModel);
                                } else {
                                    emitter.onError(new Exception());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
        );
    }


}
