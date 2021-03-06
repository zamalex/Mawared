package app.mawared.alhayat.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.orders.model.AllOrder;
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
        RetrofitClient.getApiInterface().getAllOrders(pageNumber,"Bearer "+Paper.book().read("token","none")).enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(Call<AllOrder> call, Response<AllOrder> response) {
                if (response.body() != null) {
                    orderMutableLiveData.postValue(response.body());
                }
                else
                    orderMutableLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<AllOrder> call, Throwable t) {
                orderMutableLiveData.setValue(null);

            }
        });
        return orderMutableLiveData;
    }

    public MutableLiveData<AllOrder> searchOrder(long orderNumber){
        orderSearchLiveData = new MutableLiveData<>();


        RetrofitClient.getApiInterface().searchOrder(orderNumber,"Bearer "+Paper.book().read("token","none")).enqueue(new Callback<AllOrder>() {
            @Override
            public void onResponse(Call<AllOrder> call, Response<AllOrder> response) {
                if (response.body() != null) {
                    orderSearchLiveData.postValue(response.body());
                }
                else
                    orderSearchLiveData.postValue(null);
            }


            @Override
            public void onFailure(Call<AllOrder> call, Throwable t) {
                orderSearchLiveData.postValue(null);
            }
        });

        return orderSearchLiveData;
    }


}
