package creativitysol.com.mawared.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.home.model.CitiesModel;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.MiniModel;
import creativitysol.com.mawared.login.model.checkmodel.CheckCardModel;
import okhttp3.ResponseBody;
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
                filteredProducts.setValue(null);


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
                result.setValue(null);

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
                slider.setValue(null);


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
                minimum.setValue(null);

            }
        });
    }

    void getCities() {
        RetrofitClient.getApiInterface().getMCities().enqueue(new Callback<CitiesModel>() {
            @Override
            public void onResponse(Call<CitiesModel> call, Response<CitiesModel> response) {
                cities.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<CitiesModel> call, Throwable t) {
                Log.d("rere", t.getMessage());
                cities.setValue(null);

            }
        });
    }

    public  void bindUserCard(String card_id,String user_id) {
        RetrofitClient.getApiInterface().bindUserCard(card_id,user_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    MutableLiveData<CheckCardModel> checkCardModelMutableLiveData = new MutableLiveData<>();

    void checkUserCart(String user_id) {

        RetrofitClient.getApiInterface().checkUserCard(user_id).enqueue(new Callback<CheckCardModel>() {
            @Override
            public void onResponse(Call<CheckCardModel> call, Response<CheckCardModel> response) {
                if (response.isSuccessful()) {
                    checkCardModelMutableLiveData.setValue(response.body());
                } else checkCardModelMutableLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<CheckCardModel> call, Throwable t) {
                checkCardModelMutableLiveData.setValue(null);

            }
        });

    }

}
