package app.mawared.alhayat.api;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.mawared.alhayat.AddressModel;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import io.paperdb.Paper;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.mawared.alhayat.api.Constants.BASE_URL;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static APIInterface apiInterface = null;

    static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();

                VerifyLoginResponse verifyLoginResponse = Paper.book().read("login", null);
                String token = "";
                String lat = "";
                String lng = "";
                if (verifyLoginResponse!=null)
                    token = verifyLoginResponse.getAccessToken();
                AddressModel addressModel =  Paper.book().read("address", null);
                if (addressModel!=null){
                    lat=addressModel.getLat()+"";
                    lng=addressModel.getLng()+"";
                }

                Request.Builder requestBuilder = original.newBuilder().addHeader("Authorization","Bearer "+token).addHeader("lat",lat).addHeader("lng",lng);
                Map<String,String> heads = new HashMap<>();
                heads.put("Authorization","Bearer "+token);
                heads.put("lat",lat);
                heads.put("lng",lng);


                Headers headers =  Headers.of(heads);
                requestBuilder.headers(headers);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        }).build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                  .baseUrl(BASE_URL) //new test
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }


        return retrofit;

        //----------------------------------------------------------------------------------------------
    }

    public static APIInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getClient().create(APIInterface.class);


        }
        return apiInterface;
    }


}
