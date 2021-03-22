package app.mawared.alhayat;


import android.content.res.Configuration;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.yariksoffice.lingver.Lingver;

import java.util.Map;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.onesignal.OneSignalNotificationOpenedHandler;
import app.mawared.alhayat.onesignal.OneSignalNotificationReceivedHandler;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends android.app.Application {
    private static final String AF_DEV_KEY = "8oaGQFkkvfkp2VZaqV42BV";

    String token;
    String UUID = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(this);
        Lingver.getInstance().setLocale(this, "ar");

        Paper.init(this);

        MapsInitializer.initialize(this);


       // MapView mapView = new MapView(this);
      //  mapView.onCreate(null);

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this);
        AppsFlyerLib.getInstance().start(this);


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Lingver.getInstance().setLocale(this, "ar");

    }
}
