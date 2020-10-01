package app.mawared.alhayat.activiation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.activiation.model.ActivationiModel;
import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ActivationiModel> responseBodyMutableLiveData = new MutableLiveData<>();

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
}
