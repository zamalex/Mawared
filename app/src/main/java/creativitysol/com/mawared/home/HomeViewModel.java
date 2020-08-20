package creativitysol.com.mawared.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.home.model.HomeProductModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    MutableLiveData<HomeProductModel> result = new MutableLiveData<>();

    void getHomeProducts(){
        RetrofitClient.getApiInterface().getHomeProducts().enqueue(new Callback<HomeProductModel>() {
            @Override
            public void onResponse(Call<HomeProductModel> call, Response<HomeProductModel> response) {
                result.setValue(response.body());
                Log.d("rere","done");
            }

            @Override
            public void onFailure(Call<HomeProductModel> call, Throwable t) {
                Log.d("rere",t.getMessage());

            }
        });
    }

}
