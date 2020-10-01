package app.mawared.alhayat.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.cities.Cities;
import app.mawared.alhayat.register.model.RegisterBody;
import app.mawared.alhayat.register.model.RegisterModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
