package app.mawared.alhayat;


import android.content.res.Configuration;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onesignal.OneSignal;
import com.yariksoffice.lingver.Lingver;

import app.mawared.alhayat.api.RetrofitClient;
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

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

      /*  OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debugone", "User:" + userId);
                if (registrationId != null)
                    Log.d("debugone", "registrationId:" + registrationId);

            }
        });

*/

         UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
         token = Paper.book().read("token",null);

         if (UUID!=null&&token!=null){
             JsonObject jsonObject = new JsonObject();
             jsonObject.addProperty("player_id",UUID);
             RetrofitClient.getApiInterface().sendNotificationToken(jsonObject,"Bearer "+token).enqueue(new Callback<ResponseBody>() {
                 @Override
                 public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                 }

                 @Override
                 public void onFailure(Call<ResponseBody> call, Throwable t) {

                 }
             });
         }

        Log.d("debugonee", "User:" + UUID);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Lingver.getInstance().setLocale(this, "ar");

    }
}
