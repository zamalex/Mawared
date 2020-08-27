package creativitysol.com.mawared.api;


import com.google.gson.JsonObject;

import creativitysol.com.mawared.home.model.HomeProductModel;

import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.login.model.LoginResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIInterface {


    @GET("products")
    Call<HomeProductModel> getHomeProducts();


    @GET("settings/slider")
    Call<HomeSliderModel> getHomeSlider();


    @POST("login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);




}

