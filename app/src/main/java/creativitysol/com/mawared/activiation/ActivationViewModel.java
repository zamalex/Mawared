package creativitysol.com.mawared.activiation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.activiation.model.ActivationiModel;
import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import okhttp3.ResponseBody;
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
