package app.mawared.alhayat.reset;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.reset.model.ResetModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetViewModel extends ViewModel {

    MutableLiveData<ResetModel> res = new MutableLiveData<>();

    void resetPass(JsonObject jsonObject) {
        RetrofitClient.getApiInterface().resetPass(jsonObject).enqueue(new Callback<ResetModel>() {
            @Override
            public void onResponse(Call<ResetModel> call, Response<ResetModel> response) {
                res.setValue(response.body());
                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<ResetModel> call, Throwable t) {
                Log.d("rere2", t.getMessage());
                res.setValue(null);
            }
        });
    }
}
