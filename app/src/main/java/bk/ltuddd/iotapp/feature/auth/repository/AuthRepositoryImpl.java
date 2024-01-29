package bk.ltuddd.iotapp.feature.auth.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bk.ltuddd.iotapp.data.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public class AuthRepositoryImpl implements AuthRepository{

    @Override
    public Completable sendOtp(PhoneAuthOptions phoneAuthOptions) {
        return Completable.create( emitter -> {
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            Log.e("Hello",phoneAuthOptions.toString());
            emitter.onComplete();
                }
        );
    }

    @Override
    public Completable createUser(User user) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        return Completable.create(emitter -> {
            firebaseDatabase.getReference("users")
                    .child(userUid)
                    .child("phoneNumber")
                    .setValue(user.getPhoneNumber());
            firebaseDatabase.getReference("users")
                    .child(userUid)
                    .child("password")
                    .setValue(user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    @Override
    public Single<Boolean> verifyOtp(PhoneAuthCredential phoneAuthCredential, String otpCode) {
        return Single.create( emitter -> {
            String otpToken = phoneAuthCredential.getSmsCode();
            Log.e("Bello",otpToken);
            if (otpCode.equals(otpToken)) {
                emitter.onSuccess(true);
            } else {
                emitter.onSuccess(false);
            }
                }
        );
    }

    @Override
    public Single<User> login(String phoneNumber, String password) {
        return Single.create(emitter -> FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("phoneNumber")
                .equalTo(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapShot: snapshot.getChildren()) {
                                User user = dataSnapShot.getValue(User.class);
                                if (user != null) {
                                    emitter.onSuccess(user);
                                    return;
                                }
                            }
                        }
                        emitter.onError(new Exception("Authentication failed"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                }));
    }

    @Override
    public Single<Boolean> checkExistAccount(String phoneNumber) {
        return Single.create( emitter -> FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("phoneNumber")
                .equalTo(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        emitter.onError(error.toException());
                    }
                })

        );
    }

}
