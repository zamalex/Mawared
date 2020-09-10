package creativitysol.com.mawared;


import com.onesignal.OneSignal;
import com.yariksoffice.lingver.Lingver;

import io.paperdb.Paper;

public class MyApp extends android.app.Application {

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

    }
}
