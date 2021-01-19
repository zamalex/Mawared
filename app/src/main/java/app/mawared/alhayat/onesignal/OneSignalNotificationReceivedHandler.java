package app.mawared.alhayat.onesignal;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.home.HomeViewModel;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import io.paperdb.Paper;

public class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    Context context;
    Application application;

    HomeViewModel homeViewModel;
    public OneSignalNotificationReceivedHandler(Context context,Application application) {
        this.context = context;
        this.application = application;

        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(HomeViewModel.class);
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;
        Log.i("OneSignalExample", "on recive data " + notification.payload.toJSONObject().toString());
        Log.i("OneSignalExample", "recieved");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// start
        if (context instanceof MainActivity){
            if (((MainActivity)context).navigationView!=null){
                String token = Paper.book().read("token", "none");

                if (!token.equals("none")) {
                    homeViewModel.getNotifyCount("Bearer " + token);
                    homeViewModel.getOrdersCount("Bearer " + token);

                }

                homeViewModel.notifyCount.observe(((MainActivity)context), new Observer<NotifyCountModel>() {
                    @Override
                    public void onChanged(NotifyCountModel notifyCountModel) {
                        if (notifyCountModel != null) {
                            if (notifyCountModel.getSuccess()) {
                                if (notifyCountModel.getData().getUnread() > 0&&context!=null)
                                    ((MainActivity)context).navigationView.getOrCreateBadge(R.id.support).setNumber(Integer.parseInt(notifyCountModel.getData().getUnread().toString()));
                            }
                        }
                    }
                });

                homeViewModel.ordersCount.observe(((MainActivity)context), new Observer<OrdersCountModel>() {
                    @Override
                    public void onChanged(OrdersCountModel notifyCountModel) {
                        if (notifyCountModel != null) {
                            if (notifyCountModel.getSuccess()) {
                                if (notifyCountModel.getData().getHasNewUpdates()&&context!=null)
                                    ((MainActivity)context).navigationView.getOrCreateBadge(R.id.orders).setNumber(notifyCountModel.getData().getCount());
                            }
                        }
                    }
                });
            }

        }
////////////////////////////////////////////////////////////////////////////////////////////// end
        if (data != null) {

                Log.i("OneSignalExample", "on recive data " + data.toString());
        }
    }
}