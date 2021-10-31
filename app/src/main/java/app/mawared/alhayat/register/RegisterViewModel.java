package app.mawared.alhayat.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.cities.Cities;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.newlogin.OtpResponse;
import app.mawared.alhayat.register.model.RegisterBody;
import app.mawared.alhayat.register.model.RegisterModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    MutableLiveData<Cities> citiesMutableLiveData;
    MutableLiveData<OtpResponse> registerMutableLiveData;
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
    public MutableLiveData<OtpResponse> setNewAccount(RegisterBody registerBody,String token){
        registerMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().completeNewAccount(registerBody,"Bearer "+token).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if(response.isSuccessful()) {
                    registerMutableLiveData.postValue(response.body());
                }else registerMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                registerMutableLiveData.postValue(null);

            }
        });

        return registerMutableLiveData;
    }
}
