package app.mawared.alhayat.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.newlogin.OtpResponse;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    MutableLiveData<OtpResponse> requestOtpResponse = new MutableLiveData<>();

    void login(JsonObject jsonObject) {

        RetrofitClient.getApiInterface().login(jsonObject).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    loginResponse.setValue(response.body());
                } else loginResponse.setValue(null);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponse.setValue(null);
            }
        });

    }

    void requestOtp(JsonObject jsonObject) {

        RetrofitClient.getApiInterface().requestLoginOtp(jsonObject).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                Response<OtpResponse> otpResponse = response;

                if (otpResponse.isSuccessful()) {

                    requestOtpResponse.setValue(otpResponse.body());
                } else{requestOtpResponse.setValue(null);
                  Log.e("errrr","error");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                requestOtpResponse.setValue(null);
            }
        });

    }




}
