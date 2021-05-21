package app.mawared.alhayat.api;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static APIInterface apiInterface = null;

    static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();


                Request.Builder requestBuilder = original.newBuilder();



                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                  .baseUrl("http://mawared.badee.com.sa/api/v1/") //test
                   // .baseUrl("http://mawaredal-hayat.com/api/v1/")//production
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
