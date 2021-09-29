package app.mawared.alhayat.sendorder;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.sendorder.model.AddressModel;
import app.mawared.alhayat.sendorder.model.BanksModel;
import app.mawared.alhayat.sendorder.model.PaymentModel;
import app.mawared.alhayat.sendorder.model.TimesModel;
import app.mawared.alhayat.sendorder.model.copon.CoponModel;
import app.mawared.alhayat.sendorder.model.points.PointsModel;
import app.mawared.alhayat.sendorder.newaddress.AddressNewResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderViewModel extends ViewModel {

   public MutableLiveData<AddressNewResponse> addresses = new MutableLiveData<>();
    MutableLiveData<BanksModel> banks = new MutableLiveData<>();
    MutableLiveData<TimesModel> times = new MutableLiveData<>();
    MutableLiveData<PaymentModel> payment = new MutableLiveData<>();
     public  MutableLiveData<PointsModel> points = new MutableLiveData<>();
    MutableLiveData<CoponModel> copon = new MutableLiveData<>();

   public void getAddresses(String token) {

        RetrofitClient.getApiInterface().getaddresses(token).enqueue(new Callback<AddressNewResponse>() {
            @Override
            public void onResponse(Call<AddressNewResponse> call, Response<AddressNewResponse> response) {
                if (response.isSuccessful()) {
                    addresses.setValue(response.body());
                }
                if (response.code()==401){
                    addresses.setValue(new AddressNewResponse(Long.parseLong(401+"")));
                }
            }

            @Override
            public void onFailure(Call<AddressNewResponse> call, Throwable t) {
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
                if (response.code()==401){
                    times.setValue(new TimesModel(Long.parseLong(401+"")));
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


    public void getPoints(String token){
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
