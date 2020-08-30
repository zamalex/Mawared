package creativitysol.com.mawared.sendorder;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderViewModel extends ViewModel {

    MutableLiveData<AddressModel> addresses = new MutableLiveData<>();
    MutableLiveData<BanksModel> banks = new MutableLiveData<>();
    void getAddresses(String token){

        RetrofitClient.getApiInterface().getaddresses(token).enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if (response.isSuccessful()){
                    addresses.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {

            }
        });

    }

    void getBanks(){

        RetrofitClient.getApiInterface().getBanks().enqueue(new Callback<BanksModel>() {
            @Override
            public void onResponse(Call<BanksModel> call, Response<BanksModel> response) {
                if (response.isSuccessful()){
                    banks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BanksModel> call, Throwable t) {

            }
        });

    }
}
