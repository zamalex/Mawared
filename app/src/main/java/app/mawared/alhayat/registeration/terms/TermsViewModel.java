package app.mawared.alhayat.registeration.terms;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.registeration.terms.model.Terms;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsViewModel extends ViewModel {
    MutableLiveData<Terms> termsMutableLiveData;
    MutableLiveData<Terms> privacyMutableLiveData;
    public MutableLiveData<Terms> getTermsAndConditions(String token){
        termsMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getTermsPoints(token).enqueue(new Callback<Terms>() {
            @Override
            public void onResponse(Call<Terms> call, Response<Terms> response) {
                termsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Terms> call, Throwable t) {

            }
        });



        return termsMutableLiveData;
    }

    public MutableLiveData<Terms> getPrivacyTerms(){
        privacyMutableLiveData = new MutableLiveData<>();
        RetrofitClient.getApiInterface().getPrivacyTerms().enqueue(new Callback<Terms>() {
            @Override
            public void onResponse(Call<Terms> call, Response<Terms> response) {
                privacyMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Terms> call, Throwable t) {
                privacyMutableLiveData.postValue(null);
            }
        });



        return privacyMutableLiveData;
    }
}
