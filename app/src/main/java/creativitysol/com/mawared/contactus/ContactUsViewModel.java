package creativitysol.com.mawared.contactus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.contactus.model.ContactUsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ContactUsResponse> contactUsResponseMutableLiveData;

    public MutableLiveData<ContactUsResponse> contactUs(String title,String content){
        contactUsResponseMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getFromContact(title,content,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk4OTUyOTU4LCJleHAiOjE1OTk1NTc3NTgsIm5iZiI6MTU5ODk1Mjk1OCwianRpIjoiVUlLWUJhSGNpczJ5MndaRCJ9.T7O4IHNFL-SDQsAfGt6ize40RmSnkqgRysjOMbqXVnc")
                .enqueue(new Callback<ContactUsResponse>() {
                    @Override
                    public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                        if(response.code() == 200) {
                            if (response.body() != null) {
                            contactUsResponseMutableLiveData.postValue(response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ContactUsResponse> call, Throwable t) {

                    }
                });


        return contactUsResponseMutableLiveData;
    }

}
