package creativitysol.com.mawared.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.HomeSliderModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    MutableLiveData<HomeProductModel> result = new MutableLiveData<>();
    MutableLiveData<HomeSliderModel> slider = new MutableLiveData<>();

    void getHomeProducts() {
        RetrofitClient.getApiInterface().getHomeProducts().enqueue(new Callback<HomeProductModel>() {
            @Override
            public void onResponse(Call<HomeProductModel> call, Response<HomeProductModel> response) {
                result.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<HomeProductModel> call, Throwable t) {
                Log.d("rere", t.getMessage());

            }
        });
    }

    void getHomeSlider() {
        RetrofitClient.getApiInterface().getHomeSlider().enqueue(new Callback<HomeSliderModel>() {
            @Override
            public void onResponse(Call<HomeSliderModel> call, Response<HomeSliderModel> response) {
                slider.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<HomeSliderModel> call, Throwable t) {
                Log.d("rere", t.getMessage());

            }
        });
    }
}
