package app.mawared.alhayat.about;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.about.model.SocialsModel;
import app.mawared.alhayat.api.RetrofitClient;
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
