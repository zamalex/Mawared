package creativitysol.com.mawared.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    void login(JsonObject jsonObject){

        RetrofitClient.getApiInterface().login(jsonObject).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    loginResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

}
