package creativitysol.com.mawared.orderdetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.orderdetails.model.OrderDetails;
import creativitysol.com.mawared.orders.model.AllOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsViewModel extends ViewModel {
    MutableLiveData <OrderDetails> detailsMutableLiveData;

    public MutableLiveData<OrderDetails> getOrderDetails(int orderId){
        detailsMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getOrderDetails(orderId,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk5ODQ0MTc2LCJleHAiOjE2MDA0NDg5NzYsIm5iZiI6MTU5OTg0NDE3NiwianRpIjoieXJhUXVmSXllaUtLM0E4TSJ9.2aoJij9XgyyYUU9GQv3LrbB9rU3QP0Wyy2SeUzr6v2w").enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                if (response.body() != null) {
                    detailsMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {

            }
        });
        return detailsMutableLiveData;
    }
}
