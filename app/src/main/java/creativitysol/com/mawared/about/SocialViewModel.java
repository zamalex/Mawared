package creativitysol.com.mawared.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.about.model.SocialsModel;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialViewModel extends ViewModel {

    MutableLiveData<SocialsModel> socialRes = new MutableLiveData<>();
    void getSocials(){

        RetrofitClient.getApiInterface().getSocials().enqueue(new Callback<SocialsModel>() {
            @Override
            public void onResponse(Call<SocialsModel> call, Response<SocialsModel> response) {
                if (response.isSuccessful()){
                    socialRes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SocialsModel> call, Throwable t) {

            }
        });

    }

}
