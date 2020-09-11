package creativitysol.com.mawared.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.notification.model.Notification;
import creativitysol.com.mawared.orders.model.AllOrder;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<Notification> notificationMutableLiveData;

    public MutableLiveData<Notification> getAllNotification(int pageNumber){
        notificationMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getNotification(pageNumber,
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk5ODQ0MTc2LCJleHAiOjE2MDA0NDg5NzYsIm5iZiI6MTU5OTg0NDE3NiwianRpIjoieXJhUXVmSXllaUtLM0E4TSJ9.2aoJij9XgyyYUU9GQv3LrbB9rU3QP0Wyy2SeUzr6v2w")
                .enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.body() != null) {
                    notificationMutableLiveData.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
            }
        });
        return notificationMutableLiveData;
    }
}
