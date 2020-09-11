package creativitysol.com.mawared.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.cities.Cities;
import creativitysol.com.mawared.register.model.RegisterBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterViewModel extends ViewModel {
    MutableLiveData<Cities> citiesMutableLiveData;
    MutableLiveData<ResponseBody> registerMutableLiveData;
    public MutableLiveData<Cities> getGetCities(){
        citiesMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                citiesMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });
        return citiesMutableLiveData;
    }
    public MutableLiveData<ResponseBody> setNewAccount(RegisterBody registerBody){
        registerMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().registerNewAccount(registerBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    registerMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return registerMutableLiveData;
    }
}
