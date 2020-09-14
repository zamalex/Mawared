package creativitysol.com.mawared.contactus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.APIInterface;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.contactus.model.ContactUsResponse;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsViewModel extends ViewModel {
    APIInterface apiInterface;
    MutableLiveData<ContactUsResponse> contactUsResponseMutableLiveData;

    public MutableLiveData<ContactUsResponse> contactUs(String title,String content){
        contactUsResponseMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getFromContact(title,content,"Bearer "+ Paper.book().read("token","none"))
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
