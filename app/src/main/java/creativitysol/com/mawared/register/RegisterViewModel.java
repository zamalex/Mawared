package creativitysol.com.mawared.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.cities.Cities;
import creativitysol.com.mawared.register.model.RegisterBody;
import creativitysol.com.mawared.register.model.RegisterModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterViewModel extends ViewModel {
    MutableLiveData<Cities> citiesMutableLiveData;
    MutableLiveData<RegisterModel> registerMutableLiveData;
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
    public MutableLiveData<RegisterModel> setNewAccount(RegisterBody registerBody){
        registerMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().registerNewAccount(registerBody).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if(response.code() == 200) {
                    registerMutableLiveData.postValue(response.body());
                }else registerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                registerMutableLiveData.postValue(null);

            }
        });

        return registerMutableLiveData;
    }
}
