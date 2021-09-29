package app.mawared.alhayat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.defaultaddress.DefaultAddressResponse;
import app.mawared.alhayat.orders.model.AllOrder;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressViewModel extends ViewModel {
    public MutableLiveData<ResponseBody> addressResponse=  new MutableLiveData<>();;
    public MutableLiveData<ResponseBody> deleteAddressResponse=  new MutableLiveData<>();;
    public MutableLiveData<DefaultAddressResponse> defaultAddressResponseMutableLiveData=  new MutableLiveData<>();;

    public void addAddress(JsonObject body){

        RetrofitClient.getApiInterface().addNewAddress(body,"Bearer "+ Paper.book().read("token","none")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    addressResponse.setValue(response.body());
                }
                else
                    addressResponse.setValue(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                addressResponse.setValue(null);

            }
        });

    }

    public void getDafaultAddress(String token){

        RetrofitClient.getApiInterface().getDefaultAddress("Bearer "+ token).enqueue(new Callback<DefaultAddressResponse>() {
            @Override
            public void onResponse(Call<DefaultAddressResponse> call, Response<DefaultAddressResponse> response) {
                if (response.body() != null) {
                    defaultAddressResponseMutableLiveData.setValue(response.body());
                }
                else
                    defaultAddressResponseMutableLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<DefaultAddressResponse> call, Throwable t) {
                defaultAddressResponseMutableLiveData.setValue(null);

            }
        });

    }

    public void delateAddress(int id){

        RetrofitClient.getApiInterface().deleteAddress(id+"","Bearer "+ Paper.book().read("token","none")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    deleteAddressResponse.setValue(response.body());
                }
                else
                    deleteAddressResponse.setValue(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                deleteAddressResponse.setValue(null);

            }
        });

    }


    public void setDefaultAddress(JsonObject jsonObject,String token){

        RetrofitClient.getApiInterface().setDefaultAddress(jsonObject,"Bearer "+token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }



}
