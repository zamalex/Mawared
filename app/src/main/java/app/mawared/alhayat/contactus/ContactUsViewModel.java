package app.mawared.alhayat.contactus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.APIInterface;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.contactus.model.ContactUsResponse;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ContactUsResponse> contactUsResponseMutableLiveData;

    public MutableLiveData<ContactUsResponse> contactUs(String title,String content,String mobile){
        contactUsResponseMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getFromContact(title,content,mobile,"Bearer "+ Paper.book().read("token","none"))
                .enqueue(new Callback<ContactUsResponse>() {
                    @Override
                    public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                        if(response.code() == 200) {
                            if (response.body() != null) {
                            contactUsResponseMutableLiveData.postValue(response.body());
                            }
                            else
                            contactUsResponseMutableLiveData.postValue(null);

                        }
                    }

                    @Override
                    public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                        contactUsResponseMutableLiveData.postValue(null);

                    }
                });


        return contactUsResponseMutableLiveData;
    }

}
