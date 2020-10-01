package app.mawared.alhayat.update;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.update.model.SendCodeModel;
import app.mawared.alhayat.update.model.UpdateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateViewModel extends ViewModel {


    public MutableLiveData<UpdateModel> updateEmailRes = new MutableLiveData<>();
    public MutableLiveData<SendCodeModel> sendCodeModelMutableLiveData = new MutableLiveData<>();

    public void updateEmail(JsonObject jsonObject, String token) {
        RetrofitClient.getApiInterface().uodateEmail(jsonObject, token).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful()) {
                    updateEmailRes.setValue(response.body());

                } else {
                    updateEmailRes.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                updateEmailRes.setValue(null);

            }
        });
    }


    public void updateName(JsonObject jsonObject, String token) {
        RetrofitClient.getApiInterface().uodateName(jsonObject, token).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful()) {
                    updateEmailRes.setValue(response.body());

                } else {
                    updateEmailRes.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                updateEmailRes.setValue(null);

            }
        });
    }

    public void updateMob(JsonObject jsonObject, String token) {
        RetrofitClient.getApiInterface().updateMob(jsonObject, token).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful()) {
                    updateEmailRes.setValue(response.body());

                } else {
                    updateEmailRes.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                updateEmailRes.setValue(null);

            }
        });
    }


    public void sendCode(JsonObject jsonObject, String token) {
        RetrofitClient.getApiInterface().sendCodeMobile(jsonObject, token).enqueue(new Callback<SendCodeModel>() {
            @Override
            public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                if (response.isSuccessful()) {
                    sendCodeModelMutableLiveData.setValue(response.body());

                } else {
                    sendCodeModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SendCodeModel> call, Throwable t) {
                sendCodeModelMutableLiveData.setValue(null);

            }
        });
    }
}
