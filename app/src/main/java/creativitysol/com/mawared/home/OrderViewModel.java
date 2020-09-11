package creativitysol.com.mawared.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.orders.model.AllOrder;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {

    APIInterface apiInterface;
    public MutableLiveData<AllOrder> orderMutableLiveData;
    public MutableLiveData<AllOrder> orderSearchLiveData;

    public MutableLiveData<AllOrder> getAllOrders(int pageNumber){
        orderMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getAllOrders(pageNumber,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk5ODQ0MTc2LCJleHAiOjE2MDA0NDg5NzYsIm5iZiI6MTU5OTg0NDE3NiwianRpIjoieXJhUXVmSXllaUtLM0E4TSJ9.2aoJij9XgyyYUU9GQv3LrbB9rU3QP0Wyy2SeUzr6v2w").enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(Call<AllOrder> call, Response<AllOrder> response) {
                if (response.body() != null) {
                    orderMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AllOrder> call, Throwable t) {

            }
        });
        return orderMutableLiveData;
    }

    public MutableLiveData<AllOrder> searchOrder(int orderNumber){
        orderSearchLiveData = new MutableLiveData<>();


        RetrofitClient.getApiInterface().searchOrder(orderNumber,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk5ODQ0MTc2LCJleHAiOjE2MDA0NDg5NzYsIm5iZiI6MTU5OTg0NDE3NiwianRpIjoieXJhUXVmSXllaUtLM0E4TSJ9.2aoJij9XgyyYUU9GQv3LrbB9rU3QP0Wyy2SeUzr6v2w").enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(Call<AllOrder> call, Response<AllOrder> response) {
                if (response.body() != null) {
                    orderSearchLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AllOrder> call, Throwable t) {

            }
        });

        return orderSearchLiveData;
    }


}
