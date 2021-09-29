package app.mawared.alhayat.activiation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import app.mawared.alhayat.activiation.model.ActivationiModel;
import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ActivationiModel> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<VerifyLoginResponse> verifyOtpResponse = new MutableLiveData<>();

    public MutableLiveData<ActivationiModel> verifyMobile(JsonObject verifyMobile) {
        responseBodyMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().verifyCode(verifyMobile).enqueue(new Callback<ActivationiModel>() {
            @Override
            public void onResponse(Call<ActivationiModel> call, Response<ActivationiModel> response) {
                responseBodyMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ActivationiModel> call, Throwable t) {
                responseBodyMutableLiveData.setValue(null);

            }
        });
        return responseBodyMutableLiveData;
    }


    void verifyLoginOtop(JsonObject jsonObject) {

        RetrofitClient.getApiInterface().verifyLoginOtp(jsonObject).enqueue(new Callback<VerifyLoginResponse>() {
            @Override
            public void onResponse(Call<VerifyLoginResponse> call, Response<VerifyLoginResponse> response) {
                if (response.isSuccessful()) {
                    verifyOtpResponse.setValue(response.body());
                } else if (response.code()==442){verifyOtpResponse.setValue(null);
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e("error",jObjError.getJSONObject("error").getString("message"));

                    } catch (Exception e) {
                        Log.e("error",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyLoginResponse> call, Throwable t) {
                verifyOtpResponse.setValue(null);
            }
        });

    }
}
