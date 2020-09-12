package creativitysol.com.mawared.api;


import com.google.gson.JsonObject;

import java.util.Map;

import creativitysol.com.mawared.about.model.SocialsModel;
import creativitysol.com.mawared.activiation.model.ActivationiModel;
import creativitysol.com.mawared.cities.Cities;
import creativitysol.com.mawared.contactus.model.ContactUsResponse;
import creativitysol.com.mawared.home.model.CitiesModel;
import creativitysol.com.mawared.home.model.HomeProductModel;

import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.MiniModel;
import creativitysol.com.mawared.home.model.addmodel.AddCardModel;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.mycart.model.CardModel;
import creativitysol.com.mawared.notification.model.Notification;
import creativitysol.com.mawared.orderdetails.model.OrderDetails;
import creativitysol.com.mawared.orders.model.AllOrder;
import creativitysol.com.mawared.register.model.RegisterBody;
import creativitysol.com.mawared.register.model.RegisterModel;
import creativitysol.com.mawared.registeration.model.LoginRegistration;
import creativitysol.com.mawared.registeration.terms.model.Terms;
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
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIInterface {


    @GET("products")
    Call<HomeProductModel> getHomeProducts();

    @GET("orders")
    Call<AllOrder> getAllOrders(@Query("page") int pageNumber, @Header("Authorization") String token);

    @GET("orders")
    Call<AllOrder> searchOrder(@Query("q") int pageNumber, @Header("Authorization") String token);

    @GET("orders/{orderId}/details")
    Call<OrderDetails> getOrderDetails(@Path("orderId") int id, @Header("Authorization") String token);

    @GET("notifications")
    Call<Notification> getNotification(@Query("page") int pageNumber, @Header("Authorization") String token);

    @POST("contact-us")
    Call<ContactUsResponse> getFromContact(@Query("title") String messageTitle, @Query("info") String messagesContent, @Header("Authorization") String token);

    @POST("register/check-mobile")
    Call<LoginRegistration> checkMobile(@Body JsonObject mobileNumber);

    @POST("register/verify")
    Call<ActivationiModel> verifyCode(@Body JsonObject verifyCode);

    @GET("cities")
    Call<Cities> getCities();

    @POST("register/profile")
    Call<RegisterModel> registerNewAccount(@Body RegisterBody registerBody);

    @GET("points/terms")
    Call<Terms> getTermsPoints(@Header("Authorization") String token);

    @GET("settings/slider")
    Call<HomeSliderModel> getHomeSlider();

    @GET("addresses")
    Call<AddressModel> getaddresses(@Header("Authorization") String topen);


    @GET("payment/banks")
    Call<BanksModel> getBanks();

    @GET("settings/orders/min-amount")
    Call<MiniModel> getMinmum();

    @GET("cities")
    Call<CitiesModel> getMCities();

    @GET("products/{id}")
    Call<HomeProductModel> filterByCity(@Path("id")String id);


    @GET("settings/socials")
    Call<SocialsModel> getSocials();


    @POST("login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);

    @POST("password/forget")
    Call<ResponseBody> forgotPass(@Body JsonObject jsonObject);

    @POST("deliver-time/available-times")
    Call<TimesModel> getTimes(@Body JsonObject jsonObject,@Header("Authorization") String topen);

    @Multipart
    @POST("orders")
    Call<ResponseBody> sendOrder(@PartMap Map<String, RequestBody> params, @Header("Authorization") String topen);


    @POST("carts/add")
    Call<AddCardModel> addToCard(@Query("product_id")String product_id, @Query("amount")String amount, @Query("device_id")String device_id, @Query("cart_id")String cart_id, @Query("math_type")String math_type);

    @POST("carts/{id}/remove")
    Call<ResponseBody> removeFromCard(@Path("id")String id);

    @POST("carts/{cart_id}/user/{user_id}/update")
    Call<ResponseBody> bindUserCard(@Path("cart_id")String cart_id,@Path("user_id")String user_id);

    @GET("carts/{card_id}/items")
    Call<CardModel> getCard(@Path("card_id")String card_id);



}

