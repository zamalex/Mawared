package creativitysol.com.mawared.activiation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ResponseBody> responseBodyMutableLiveData;
    public MutableLiveData<ResponseBody> verifyMobile(JsonObject verifyMobile){
        responseBodyMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().verifyCode(verifyMobile).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                responseBodyMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return responseBodyMutableLiveData;
    }
}
