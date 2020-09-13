package creativitysol.com.mawared.registeration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.registeration.model.LoginRegistration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<LoginRegistration> responseBodyMutableLiveData;

    public MutableLiveData<LoginRegistration> checkMobile(JsonObject mobileNumber){
        responseBodyMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().checkMobile(mobileNumber).enqueue(new Callback<LoginRegistration>() {
            @Override
            public void onResponse(Call<LoginRegistration> call, Response<LoginRegistration> response) {
                responseBodyMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginRegistration> call, Throwable t) {
                responseBodyMutableLiveData.postValue(null);

            }
        });
        return responseBodyMutableLiveData;
    }
}
