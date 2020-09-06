package creativitysol.com.mawared.forgot;

import android.util.Log;


import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.api.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotViewModel extends ViewModel {




    void forgotPass(JsonObject jsonObject) {
        RetrofitClient.getApiInterface().forgotPass(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("rere2", t.getMessage());

            }
        });
    }



}
