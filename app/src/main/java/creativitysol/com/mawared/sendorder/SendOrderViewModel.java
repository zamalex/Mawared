package creativitysol.com.mawared.sendorder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import creativitysol.com.mawared.sendorder.model.PaymentModel;
import creativitysol.com.mawared.sendorder.model.TimesModel;
import creativitysol.com.mawared.sendorder.model.copon.CoponModel;
import creativitysol.com.mawared.sendorder.model.points.PointsModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderViewModel extends ViewModel {

    MutableLiveData<AddressModel> addresses = new MutableLiveData<>();
    MutableLiveData<BanksModel> banks = new MutableLiveData<>();
    MutableLiveData<TimesModel> times = new MutableLiveData<>();
    MutableLiveData<PaymentModel> payment = new MutableLiveData<>();
    MutableLiveData<PointsModel> points = new MutableLiveData<>();
    MutableLiveData<CoponModel> copon = new MutableLiveData<>();

    void getAddresses(String token) {

        RetrofitClient.getApiInterface().getaddresses(token).enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                if (response.isSuccessful()) {
                    addresses.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                Log.d("aeeee",t.getMessage());
            }
        });

    }

    void getBanks() {

        RetrofitClient.getApiInterface().getBanks().enqueue(new Callback<BanksModel>() {
            @Override
            public void onResponse(Call<BanksModel> call, Response<BanksModel> response) {
                if (response.isSuccessful()) {
                    banks.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BanksModel> call, Throwable t) {

            }
        });

    }

    void getTimes(JsonObject jsonObject, String token) {

        RetrofitClient.getApiInterface().getTimes(jsonObject, token).enqueue(new Callback<TimesModel>() {
            @Override
            public void onResponse(Call<TimesModel> call, Response<TimesModel> response) {
                if (response.isSuccessful()) {
                    times.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TimesModel> call, Throwable t) {

            }
        });

    }

    void getPayment() {

        RetrofitClient.getApiInterface().getPayment().enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                if (response.isSuccessful()) {
                    payment.setValue(response.body());
                } else {
                    payment.setValue(null);

                }
            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                payment.setValue(null);

            }
        });

    }

    MutableLiveData<ResponseBody> addAddressResponse = new MutableLiveData<>();

    void addAddress(String name,String mobile,String lat,String lng,String address,String type,String token){
        RetrofitClient.getApiInterface().addAddress(name,mobile,lat,lng,address,type,token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()){
                    addAddressResponse.setValue(response.body());
                }
                else {
                    addAddressResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                addAddressResponse.setValue(null);
            }
        });
    }


    void getPoints(String token){
        RetrofitClient.getApiInterface().getPoints(token).enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {

                if (response.isSuccessful()){
                    points.setValue(response.body());
                }
                else {
                    points.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable t) {
                points.setValue(null);
            }
        });
    }


    void checkCopon(JsonObject obj,String token){
        RetrofitClient.getApiInterface().checkCopon(obj,token).enqueue(new Callback<CoponModel>() {
            @Override
            public void onResponse(Call<CoponModel> call, Response<CoponModel> response) {

                if (response.isSuccessful()){
                    copon.setValue(response.body());
                }
                else {
                    copon.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CoponModel> call, Throwable t) {
                copon.setValue(null);
            }
        });
    }
}
