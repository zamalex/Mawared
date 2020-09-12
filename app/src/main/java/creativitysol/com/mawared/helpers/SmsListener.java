package creativitysol.com.mawared.helpers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    public OnSmsReceivedListener listener = null;
    public Context context;

    public SmsListener()
    {

    }

    public void setOnSmsReceivedListener(Context context) {
        Log.d("Listener","SET");
        this.listener = (OnSmsReceivedListener) context;
        Log.d("Listener","SET SUCCESS");
    }

    public interface OnSmsReceivedListener {
        public void onSmsReceived(String otp);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                  //  msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<pdus.length; i++){
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        Log.d("MsgBody", message);


                        if (listener != null) {
                            listener.onSmsReceived(message);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.d("Listener", "Its null");
                        }

                    }
                }catch(Exception e){
                    Log.d("Exception caught", e.getLocalizedMessage());
                }
            }
        }
    }
}