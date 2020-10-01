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
}
