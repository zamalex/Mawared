package creativitysol.com.mawared.orderdetails;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.orderdetails.model.OrderDetails;
import creativitysol.com.mawared.orders.model.AllOrder;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsViewModel extends ViewModel {
    MutableLiveData <OrderDetails> detailsMutableLiveData;

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
}
