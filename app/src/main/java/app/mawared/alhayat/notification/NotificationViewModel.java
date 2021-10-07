package app.mawared.alhayat.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.notification.model.Notification;
import app.mawared.alhayat.notification.newmodel.NewNotifications;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<NewNotifications> notificationMutableLiveData;

    public MutableLiveData<NewNotifications> getAllNotification(int pageNumber){
        notificationMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getNotification(pageNumber,
                "Bearer "+Paper.book().read("token","none"))
                .enqueue(new Callback<NewNotifications>() {
            @Override
            public void onResponse(Call<NewNotifications> call, Response<NewNotifications> response) {
                if (response.body() != null) {
                    notificationMutableLiveData.postValue(response.body());
                }
                else {
                    notificationMutableLiveData.postValue(null);

                }
               /* if (response.code()==401){
                    notificationMutableLiveData.postValue(new NewNotifications(401));
                }*/
            }
            @Override
            public void onFailure(Call<NewNotifications> call, Throwable t) {
                notificationMutableLiveData.postValue(null);

            }
        });
        return notificationMutableLiveData;
    }
}
