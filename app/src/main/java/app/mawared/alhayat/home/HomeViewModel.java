package app.mawared.alhayat.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.home.model.CitiesModel;
import app.mawared.alhayat.home.model.HomeProductModel;
import app.mawared.alhayat.home.model.HomeSliderModel;
import app.mawared.alhayat.home.model.MiniModel;
import app.mawared.alhayat.home.model.checkrate.CheckRate;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import app.mawared.alhayat.login.model.checkmodel.CheckCardModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    public MutableLiveData<NotifyCountModel> notifyCount = new MutableLiveData<>();
    public MutableLiveData<OrdersCountModel> ordersCount = new MutableLiveData<>();


    MutableLiveData<HomeProductModel> result = new MutableLiveData<>();
    MutableLiveData<HomeSliderModel> slider = new MutableLiveData<>();
    MutableLiveData<MiniModel> minimum = new MutableLiveData<>();
    MutableLiveData<CitiesModel> cities = new MutableLiveData<>();
    MutableLiveData<HomeProductModel> filteredProducts = new MutableLiveData<>();


    public MutableLiveData<CheckRate> checkRate(String token) {
        MutableLiveData<CheckRate> mutableLiveData = new MutableLiveData<CheckRate>();

        RetrofitClient.getApiInterface().checkRate("Bearer " + token).enqueue(new Callback<CheckRate>() {
            @Override
            public void onResponse(Call<CheckRate> call, Response<CheckRate> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else mutableLiveData.setValue(null);

            }

            @Override
            public void onFailure(Call<CheckRate> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }

    public void getNotifyCount(String token) {
        RetrofitClient.getApiInterface().getNotifyCount(token).enqueue(new Callback<NotifyCountModel>() {
            @Override
            public void onResponse(Call<NotifyCountModel> call, Response<NotifyCountModel> response) {

                notifyCount.setValue(response.body());
                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<NotifyCountModel> call, Throwable t) {
                Log.d("rere2", t.getMessage());
                notifyCount.setValue(null);


            }
        });
    }

    public void getOrdersCount(String token) {
        RetrofitClient.getApiInterface().getOrdersCount(token).enqueue(new Callback<OrdersCountModel>() {
            @Override
            public void onResponse(Call<OrdersCountModel> call, Response<OrdersCountModel> response) {

                ordersCount.setValue(response.body());
                Log.d("rere2", "done");
            }

            @Override
            public void onFailure(Call<OrdersCountModel> call, Throwable t) {
                Log.d("rere2", t.getMessage());
                ordersCount.setValue(null);


            }
        });
    }


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

    void getHomeProducts(String cart_id,String lat,String lng,int page) {
        RetrofitClient.getApiInterface().getHomeProducts(cart_id,lat,lng,page).enqueue(new Callback<HomeProductModel>() {
            @Override
            public void onResponse(Call<HomeProductModel> call, Response<HomeProductModel> response) {
                result.setValue(response.body());
                Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<HomeProductModel> call, Throwable t) {
                Log.e("rere", t.getMessage());
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

               if (response.isSuccessful())cities.setValue(response.body());
               else cities.setValue(null);
               Log.d("rere", "done");
            }

            @Override
            public void onFailure(Call<CitiesModel> call, Throwable t) {
                Log.d("rere", t.getMessage());
                cities.setValue(null);

            }
        });
    }

    MutableLiveData<ResponseBody> bindResponse = new MutableLiveData<>();

    public void bindUserCard(String card_id, String user_id) {
        RetrofitClient.getApiInterface().bindUserCard(card_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                bindResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                bindResponse.setValue(null);
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
