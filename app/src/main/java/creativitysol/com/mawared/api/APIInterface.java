package creativitysol.com.mawared.api;


import com.google.gson.JsonObject;

import java.util.Map;

import creativitysol.com.mawared.about.model.SocialsModel;
import creativitysol.com.mawared.home.model.HomeProductModel;

import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.MiniModel;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import creativitysol.com.mawared.sendorder.model.TimesModel;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;


public interface APIInterface {


    @GET("products")
    Call<HomeProductModel> getHomeProducts();


    @GET("settings/slider")
    Call<HomeSliderModel> getHomeSlider();

    @GET("addresses")
    Call<AddressModel> getaddresses(@Header("Authorization") String topen);


    @GET("payment/banks")
    Call<BanksModel> getBanks();

    @GET("settings/orders/min-amount")
    Call<MiniModel> getMinmum();


    @GET("settings/socials")
    Call<SocialsModel> getSocials();


    @POST("login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);

    @POST("deliver-time/available-times")
    Call<TimesModel> getTimes(@Body JsonObject jsonObject,@Header("Authorization") String topen);

    @Multipart
    @POST("orders")
    Call<ResponseBody> sendOrder(@PartMap Map<String, RequestBody> params, @Header("Authorization") String topen);





}

