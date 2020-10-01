package app.mawared.alhayat.forgot;

import android.util.Log;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.forgot.model.ForgotModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotViewModel extends ViewModel {



    MutableLiveData<ForgotModel> res = new MutableLiveData<>();

    void forgotPass(JsonObject jsonObject) {
        RetrofitClient.getApiInterface().forgotPass(jsonObject).enqueue(new Callback<ForgotModel>() {
            @Override
            public void onResponse(Call<ForgotModel> call, Response<ForgotModel> response) {

                res.setValue(response.body());
                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<ForgotModel> call, Throwable t) {
                Log.d("rere2", t.getMessage());
                res.setValue(null);
            }
        });
    }



}
