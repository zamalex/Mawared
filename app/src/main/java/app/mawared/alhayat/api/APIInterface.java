package app.mawared.alhayat.api;


import com.google.gson.JsonObject;

import java.util.Map;

import app.mawared.alhayat.about.model.SocialsModel;
import app.mawared.alhayat.activiation.model.ActivationiModel;
import app.mawared.alhayat.cities.Cities;
import app.mawared.alhayat.contactus.model.ContactUsResponse;
import app.mawared.alhayat.defaultaddress.DefaultAddressResponse;
import app.mawared.alhayat.forgot.model.ForgotModel;
import app.mawared.alhayat.home.model.CitiesModel;
import app.mawared.alhayat.home.model.HomeProductModel;

import app.mawared.alhayat.home.model.HomeSliderModel;
import app.mawared.alhayat.home.model.MiniModel;
import app.mawared.alhayat.home.model.addmodel.AddCardModel;
import app.mawared.alhayat.home.model.checkrate.CheckRate;
import app.mawared.alhayat.home.model.prodetails.ProductDetails;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.checkmodel.CheckCardModel;
import app.mawared.alhayat.login.model.newlogin.OtpResponse;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.mycart.model.CardModel;
import app.mawared.alhayat.notification.model.Notification;
import app.mawared.alhayat.orderdetails.model.OrderDetails;
import app.mawared.alhayat.orders.model.AllOrder;
import app.mawared.alhayat.orders.newmodel.MyOrdersResponse;
import app.mawared.alhayat.register.model.RegisterBody;
import app.mawared.alhayat.register.model.RegisterModel;
import app.mawared.alhayat.registeration.model.LoginRegistration;
import app.mawared.alhayat.registeration.terms.model.Terms;
import app.mawared.alhayat.reset.model.ResetModel;
import app.mawared.alhayat.sendorder.model.AddressModel;
import app.mawared.alhayat.sendorder.model.BanksModel;
import app.mawared.alhayat.sendorder.model.PaymentModel;
import app.mawared.alhayat.sendorder.model.TimesModel;
import app.mawared.alhayat.sendorder.model.copon.CoponModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.ConfirmModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.visa.VisaModel;
import app.mawared.alhayat.sendorder.model.points.PointsModel;
import app.mawared.alhayat.sendorder.newaddress.AddressNewResponse;
import app.mawared.alhayat.support.chat.model.SendMsgModel;
import app.mawared.alhayat.support.chat.model.received.ReceivedChat;
import app.mawared.alhayat.support.chatlist.model.ChatList;
import app.mawared.alhayat.update.model.SendCodeModel;
import app.mawared.alhayat.update.model.UpdateModel;
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
///////////////////////////////////////////////////////////////////////////////

    //new Auth=======

    //login
    @POST("auth/otp-login")
    Call<OtpResponse> requestLoginOtp(@Body JsonObject jsonObject);

    //verify login otp
    @POST("auth/otp-verify")
    Call<VerifyLoginResponse> verifyLoginOtp(@Body JsonObject jsonObject);

//register
    @POST("auth/otp-register")
    Call<OtpResponse> completeNewAccount(@Body RegisterBody registerBody,@Header("Authorization") String token);

////slider
@GET("settings/slider")
Call<HomeSliderModel> getHomeSlider();

    ///update profile
    @POST("user/email/update")
    Call<UpdateModel> uodateEmail(@Body JsonObject jsonObject, @Header("Authorization") String topen);

    @POST("user/name/update")
    Call<UpdateModel> uodateName(@Body JsonObject jsonObject, @Header("Authorization") String topen);

    ///banks
    @GET("settings/banks-list")
    Call<BanksModel> getBanks();


    //home products
    @GET("products")
    Call<HomeProductModel> getHomeProducts(@Query("cart_id") String cart_id,@Query("lat") String lat,@Query("long") String lng,@Query("page") int page);


    //add cart to user
    @POST("carts/{cart_id}/add-to-user")
    Call<ResponseBody> bindUserCard(@Path("cart_id") String cart_id);


    //add new address
    @POST("addresses/store")
    Call<ResponseBody> addNewAddress(@Body JsonObject jsonObject, @Header("Authorization") String topen);

    //delete address
    @POST("addresses/{id}/delete")
    Call<ResponseBody> deleteAddress(@Path("id") String id, @Header("Authorization") String topen);


    //set default address
    @POST("addresses/set-default-address")
    Call<ResponseBody> setDefaultAddress(@Body JsonObject jsonObject, @Header("Authorization") String topen);


    //get default address
    @GET("addresses/get-default-address")
    Call<DefaultAddressResponse> getDefaultAddress(@Header("Authorization") String topen);


    //////////////////////////////////////////////////////////////////////////

   // @GET("products")
   // Call<HomeProductModel> getHomeProducts(@Query("cart_id") String cart_id,@Query("lat") String lat,@Query("long") String lng);

    @GET("orders")
    Call<MyOrdersResponse> getAllOrders(@Query("page") int pageNumber, @Header("Authorization") String token);

    @GET("orders")
    Call<MyOrdersResponse> searchOrder(@Query("order_number") long pageNumber, @Header("Authorization") String token);

    @GET("orders/{orderId}/show")
    Call<OrderDetails> getOrderDetails(@Path("orderId") int id, @Header("Authorization") String token);

    @GET("notifications")
    Call<Notification> getNotification(@Query("page") int pageNumber, @Header("Authorization") String token);

    @POST("contact-us")
    Call<ContactUsResponse> getFromContact(@Query("title") String messageTitle, @Query("info") String messagesContent, @Query("mobile") String mobile, @Query("username") String username, @Header("Authorization") String token);

    @POST("register/check-mobile")
    Call<LoginRegistration> checkMobile(@Body JsonObject mobileNumber);

    @POST("register/verify")
    Call<ActivationiModel> verifyCode(@Body JsonObject verifyCode);

    @GET("settings/cities")
    Call<Cities> getCities();

    @POST("register/profile")
    Call<LoginResponse> registerNewAccount(@Body RegisterBody registerBody);

    @GET("pages/points-terms")
    Call<Terms> getTermsPoints(@Header("Authorization") String token);

    @GET("privacy-terms")
    Call<Terms> getPrivacyTerms();



    @GET("addresses")
    Call<AddressNewResponse> getaddresses(@Header("Authorization") String topen);


   /* @GET("settings/banks-list")
    Call<BanksModel> getBanks();*/

    @GET("settings/orders/min-amount")
    Call<MiniModel> getMinmum();

    @GET("cities")
    Call<CitiesModel> getMCities();

    @GET("products/{id}")
    Call<HomeProductModel> filterByCity(@Path("id") String id);


    @GET("settings/socials")
    Call<SocialsModel> getSocials();


    @POST("login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);

   /* @POST("update-email")
    Call<UpdateModel> uodateEmail(@Body JsonObject jsonObject, @Header("Authorization") String topen);
*/
    @POST("mobile-notifications/subscription")
    Call<ResponseBody> sendNotificationToken(@Body JsonObject jsonObject, @Header("Authorization") String topen);

    /*@POST("update-name")
    Call<UpdateModel> uodateName(@Body JsonObject jsonObject, @Header("Authorization") String topen);
*/
    @POST("update-mobile")
    Call<UpdateModel> updateMob(@Body JsonObject jsonObject, @Header("Authorization") String topen);

    @POST("password/forget")
    Call<ForgotModel> forgotPass(@Body JsonObject jsonObject);

    @POST("password/reset")
    Call<LoginResponse> resetPass(@Body JsonObject jsonObject);

    @POST("orders/get-available-shifts")
    Call<TimesModel> getTimes(@Body JsonObject jsonObject, @Header("Authorization") String token);

    @Multipart
    @POST("orders/create")
    Call<VisaModel> sendOrderVisa(@PartMap Map<String, RequestBody> params, @Header("Authorization") String topen);


    @Multipart
    @POST("orders/create")
    Call<ConfirmModel> sendOrder(@PartMap Map<String, RequestBody> params, @Header("Authorization") String topen);


    @POST("add-new-address")
    Call<ResponseBody> addAddress(@Query("username") String username, @Query("mobile") String mobile,
                                  @Query("lat") String lat, @Query("lng") String lang,
                                  @Query("address") String address, @Query("delivery_type") String delivery_type,
                                  @Header("Authorization") String token);

    @GET("user/wallet")
    Call<PointsModel> getPoints(@Header("Authorization") String token);

    @POST("orders/apply-coupon")
    Call<CoponModel> checkCopon(@Body JsonObject jsonObject, @Header("Authorization") String token);


    @POST("carts/add")
    Call<AddCardModel> addToCard(@Query("product_id") String product_id, @Query("amount") String amount, @Query("device_id") String device_id, @Query("cart_id") String cart_id, @Query("math_type") String math_type, @Query("city_id") String city_id,@Query("lat") String lat,@Query("lng") String lng);


    @POST("points/calculate-price")
    Call<ResponseBody> calculatePts(@Query("points") String points, @Query("price") String price);

    @POST("carts/{cart_id}/products/{product_id}/remove")
    Call<ResponseBody> removeFromCard(@Path("cart_id") String cart_id, @Path("product_id") String product_id);




    @GET("orders/{id}/cancel")
    Call<ConfirmModel> cancelOrder(@Path("id") String id, @Header("Authorization") String token);


    @POST("send-mobile-code")
    Call<SendCodeModel> sendCodeMobile(@Body JsonObject jsonObject, @Header("Authorization") String token);


    @POST("messages/send")
    Call<SendMsgModel> sendMessage(@Query("message") String message, @Query("conversation_id") String conversation_id, @Query("order_id") String order_id, @Query("title") String title, @Header("Authorization") String token);


    @GET("messages")
    Call<ChatList> getChats(@Header("Authorization") String token);


    @GET("messages/get-chat")
    Call<ReceivedChat> receivedChat(@Query("conversation_id") String conversation_id, @Query("order_id") String order_id, @Header("Authorization") String token);


    @POST("payment/return")
    Call<ResponseBody> tstPayment();

    @POST("orders/rate")
    Call<ResponseBody> rateOrder(@Header("Authorization") String token, @Body JsonObject body);


    @GET("carts/{user_id}/user-cart")
    Call<CheckCardModel> checkUserCard(@Path("user_id") String user_id);

    @GET("carts/{card_id}/items")
    Call<CardModel> getCard(@Path("card_id") String card_id);

    @GET("payment/payment-methods")
    Call<PaymentModel> getPayment();

    @GET("messages/count-unread")
    Call<NotifyCountModel> getNotifyCount(@Header("Authorization") String token);


    @GET("orders/has-new-updates")
    Call<OrdersCountModel> getOrdersCount(@Header("Authorization") String token);


    @GET("products/{id}/show")
    Call<ProductDetails> getProDetails(@Path("id") String id,@Query("cart_id")String cart_id,@Query("city_id")String city_id);


    @GET("orders/has-new-updates")
    Call<CheckRate> checkRate(@Header("Authorization") String token);

}

