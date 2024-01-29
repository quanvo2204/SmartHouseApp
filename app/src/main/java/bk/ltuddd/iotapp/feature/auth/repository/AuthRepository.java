package bk.ltuddd.iotapp.feature.auth.repository;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;

import bk.ltuddd.iotapp.data.model.User;
import io.reactivex.Completable;
import io.reactivex.Single;


public interface AuthRepository {
    Completable sendOtp(PhoneAuthOptions phoneAuthOptions);
    Completable createUser(User user);
    Single<Boolean> verifyOtp(PhoneAuthCredential phoneAuthCredential,String otpCode);
    Single<User> login(String phoneNumber, String password);
    Single<Boolean> checkExistAccount(String phoneNumber);
}
