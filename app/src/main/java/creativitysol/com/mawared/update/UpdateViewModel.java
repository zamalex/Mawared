package creativitysol.com.mawared.update;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.update.model.UpdateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateViewModel extends ViewModel {


    public MutableLiveData<UpdateModel> updateEmailRes = new MutableLiveData<>();

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
}
