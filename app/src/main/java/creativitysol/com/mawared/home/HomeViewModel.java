package creativitysol.com.mawared.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.home.model.CitiesModel;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.MiniModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    MutableLiveData<HomeProductModel> result = new MutableLiveData<>();
    MutableLiveData<HomeSliderModel> slider = new MutableLiveData<>();
    MutableLiveData<MiniModel> minimum = new MutableLiveData<>();
    MutableLiveData<CitiesModel> cities = new MutableLiveData<>();
    MutableLiveData<HomeProductModel> filteredProducts = new MutableLiveData<>();

    void filterByCity(String id) {
        RetrofitClient.getApiInterface().filterByCity(id).enqueue(new Callback<HomeProductModel>() {
            @Override
            public void onResponse(Call<HomeProductModel> call, Response<HomeProductModel> response) {

                filteredProducts.setValue(response.body());
                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<HomeProductModel> call, Throwable t) {
                Log.d("rere2", t.getMessage());

            }
        });
    }

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

    void getMin() {
        RetrofitClient.getApiInterface().getMinmum().enqueue(new Callback<MiniModel>() {
            @Override
            public void onResponse(Call<MiniModel> call, Response<MiniModel> response) {
                minimum.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<MiniModel> call, Throwable t) {
                Log.d("rere", t.getMessage());

            }
        });
    }

    void getCities() {
        RetrofitClient.getApiInterface().getCities().enqueue(new Callback<CitiesModel>() {
            @Override
            public void onResponse(Call<CitiesModel> call, Response<CitiesModel> response) {
                cities.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<CitiesModel> call, Throwable t) {
                Log.d("rere", t.getMessage());

            }
        });
    }


}
