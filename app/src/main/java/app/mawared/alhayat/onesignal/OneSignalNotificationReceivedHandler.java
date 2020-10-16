package app.mawared.alhayat.onesignal;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;
        Log.i("OneSignalExample", "on recive data " + notification.payload.toJSONObject().toString());
        Log.i("OneSignalExample", "recieved");

        if (data != null) {

                Log.i("OneSignalExample", "on recive data " + data.toString());
        }
    }
}