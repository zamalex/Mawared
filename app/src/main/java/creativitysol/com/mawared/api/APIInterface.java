package creativitysol.com.mawared.api;


import creativitysol.com.mawared.home.model.HomeProductModel;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIInterface {


    @GET("products")
    Call<HomeProductModel> getHomeProducts();


}

