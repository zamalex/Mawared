package creativitysol.com.mawared;


import com.onesignal.OneSignal;

import io.paperdb.Paper;

public class MyApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Paper.init(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }
}
