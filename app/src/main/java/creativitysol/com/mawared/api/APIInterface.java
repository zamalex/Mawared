package creativitysol.com.mawared.api;


import com.google.gson.JsonObject;

import creativitysol.com.mawared.home.model.HomeProductModel;

import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface APIInterface {


    @GET("products")
    Call<HomeProductModel> getHomeProducts();


    @GET("settings/slider")
    Call<HomeSliderModel> getHomeSlider();

    @GET("addresses")
    Call<AddressModel> getaddresses(@Header("Authorization") String topen);


    @GET("payment/banks")
    Call<BanksModel> getBanks();


    @POST("login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);




}

