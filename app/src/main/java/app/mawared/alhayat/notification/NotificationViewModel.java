package app.mawared.alhayat.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.notification.model.Notification;
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
                "Bearer "+Paper.book().read("token","none"))
                .enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.body() != null) {
                    notificationMutableLiveData.postValue(response.body());
                }
                else {
                    notificationMutableLiveData.postValue(null);

                }
                if (response.code()==401){
                    notificationMutableLiveData.postValue(new Notification(401));
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                notificationMutableLiveData.postValue(null);

            }
        });
        return notificationMutableLiveData;
    }
}
