package app.mawared.alhayat;


import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.yariksoffice.lingver.Lingver;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.onesignal.OneSignalNotificationOpenedHandler;
import app.mawared.alhayat.onesignal.OneSignalNotificationReceivedHandler;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends android.app.Application {

    String token;
    String UUID = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Lingver.init(this);
        Lingver.getInstance().setLocale(this, "ar");

        Paper.init(this);



        Log.d("debugonee", "User:" + UUID);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Lingver.getInstance().setLocale(this, "ar");

    }
}
