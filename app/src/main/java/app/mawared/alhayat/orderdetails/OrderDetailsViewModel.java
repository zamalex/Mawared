package app.mawared.alhayat.orderdetails;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.orderdetails.model.OrderDetails;
import app.mawared.alhayat.sendorder.model.paymentmodel.ConfirmModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.visa.VisaModel;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsViewModel extends ViewModel {
    MutableLiveData <OrderDetails> detailsMutableLiveData;
    MutableLiveData <ResponseBody> updatePaymentResponse = new MutableLiveData<>();
    MutableLiveData <VisaModel> updatePaymentResponseVisa = new MutableLiveData<>();

    public MutableLiveData<OrderDetails> getOrderDetails(int orderId){
        detailsMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getOrderDetails(orderId,"Bearer "+ Paper.book().read("token","none")).enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                if (response.body() != null) {
                    detailsMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {
                Log.d("odetails",t.getMessage());

            }
        });
        return detailsMutableLiveData;
    }



    MutableLiveData<ConfirmModel> cancelResponse = new MutableLiveData<>();

    void cancelOrder(String jsonObject,String token){
        RetrofitClient.getApiInterface().cancelOrder(jsonObject,token).enqueue(new Callback<ConfirmModel>() {
            @Override
            public void onResponse(Call<ConfirmModel> call, Response<ConfirmModel> response) {
                if (response.isSuccessful()){
                    cancelResponse.setValue(response.body());

                }else {
                    cancelResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ConfirmModel> call, Throwable t) {
                cancelResponse.setValue(null);

            }
        });
    }


    void updatePayment(String id,JsonObject jsonObject){
        RetrofitClient.getApiInterface().updatePayment(id,jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    updatePaymentResponse.setValue(response.body());

                }else {
                    updatePaymentResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                updatePaymentResponse.setValue(null);

            }
        });
    }


    void updatePaymentVisa(String id,JsonObject jsonObject){
        RetrofitClient.getApiInterface().updatePaymentVisa(id,jsonObject).enqueue(new Callback<VisaModel>() {
            @Override
            public void onResponse(Call<VisaModel> call, Response<VisaModel> response) {
                if (response.isSuccessful()){
                    updatePaymentResponseVisa.setValue(response.body());

                }else {
                    updatePaymentResponseVisa.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<VisaModel> call, Throwable t) {
                updatePaymentResponseVisa.setValue(null);

            }
        });
    }
}
