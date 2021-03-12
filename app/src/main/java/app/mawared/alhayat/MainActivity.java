package app.mawared.alhayat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.onesignal.OneSignal;
import com.yariksoffice.lingver.Lingver;

import app.mawared.alhayat.about.AboutMawaredFragment;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.home.HomeFragment;
import app.mawared.alhayat.home.HomeViewModel;
import app.mawared.alhayat.home.ProductDetailsFragment;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.notification.NotificationFragments;
import app.mawared.alhayat.onesignal.OneSignalNotificationOpenedHandler;
import app.mawared.alhayat.onesignal.OneSignalNotificationReceivedHandler;
import app.mawared.alhayat.orderdetails.OrderDetailsFragment;
import app.mawared.alhayat.orders.OrderFragment;
import app.mawared.alhayat.settings.SettingsFragment;
import app.mawared.alhayat.support.SupportFragment;
import app.mawared.alhayat.support.chat.ChatFragment;
import app.mawared.alhayat.support.chatlist.ChatListFragment;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public FragmentStack fragmentStack;
    public BottomNavigationView navigationView;
    private boolean paymentSuccess = false;
    KProgressHUD dialog;

    SharedPreferences.Editor orderIdPref;
    HomeViewModel viewModel;
    public Boolean didShow = false;
    String token;
    String UUID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(HomeViewModel.class);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bb = new Bundle();
        bb.putString("screen", "Main screen Android");
        mFirebaseAnalytics.logEvent("user_location", bb);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// start one signal
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            if (deepLink != null)
                                if (deepLink.getQueryParameter("id") != null) {
                                    Toast.makeText(MainActivity.this, "lonk is " + deepLink.getQueryParameter("id"), Toast.LENGTH_SHORT).show();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("product", deepLink.getQueryParameter("id"));
                                    bundle.putString("city", null);
                                    ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                                    productDetailsFragment.setArguments(bundle);

                                    if (fragmentStack == null) {
                                        fragmentStack = new FragmentStack(MainActivity.this, getSupportFragmentManager(), R.id.main_container);

                                    }

                                    fragmentStack.push(productDetailsFragment);

                                }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler(this, getApplication()))
                .init();


        UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        token = Paper.book().read("token", null);

        if (UUID != null && token != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("player_id", UUID);
            RetrofitClient.getApiInterface().sendNotificationToken(jsonObject, "Bearer " + token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////// end one signal
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            555);
                    Log.e("updatee", "requested");
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.e("updatee", "catch");

                }
            } else Log.e("updatee", "else");

        });


        setContentView(R.layout.activity_main);

        dialog = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        navigationView = findViewById(R.id.navigation);


        //  navigationView.getOrCreateBadge(R.id.support).setNumber(5);
        //navigationView.getOrCreateBadge(R.id.orders);


        final HomeFragment homeFragment = new HomeFragment();
        final SupportFragment supportFragment = new SupportFragment();
        final ChatFragment chatFragment = new ChatFragment();
        final AboutMawaredFragment aboutMawaredFragment = new AboutMawaredFragment();

        orderIdPref = getSharedPreferences("mwared", Context.MODE_PRIVATE).edit();


        if (fragmentStack == null)
            fragmentStack = new FragmentStack(this, getSupportFragmentManager(), R.id.main_container);
        fragmentStack.replace(homeFragment);

        if (getIntent().getStringExtra("order") != null) {
            if (getIntent().getStringExtra("order").equals("order")) {
                navigationView.setSelectedItemId(R.id.orders);
                fragmentStack.replace(new OrderFragment());

            }
        }

        if (getIntent().getStringExtra("order") != null) {
            if (getIntent().getStringExtra("order").equals("rate")) {
                navigationView.setSelectedItemId(R.id.orders);
                OrderFragment orderFragment = new OrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("rate", "rate");
                orderFragment.setArguments(bundle);
                fragmentStack.replace(orderFragment);

            }
        }

        if (getIntent().getStringExtra("type") != null) {
            if (getIntent().getStringExtra("type").equals("chat")) {
                navigationView.setSelectedItemId(R.id.support);
                ChatListFragment chatListFragment = new ChatListFragment();
                Bundle b = new Bundle();
                b.putString("conversation", getIntent().getStringExtra("conversation"));
                chatListFragment.setArguments(b);

                fragmentStack.replace(chatListFragment);

            } else if (getIntent().getStringExtra("type").equals("change_status")) {
                // navigationView.setSelectedItemId(R.id.support);
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();

                if (getIntent().getStringExtra("order_id") != null) {
                    orderIdPref.putInt("orderId", Integer.parseInt(getIntent().getStringExtra("order_id"))).apply();


                    fragmentStack.push(orderDetailsFragment);
                }


            } else if (getIntent().getStringExtra("type").equals("points")) {
                navigationView.setSelectedItemId(R.id.settings);
                SettingsFragment settingsFragment = new SettingsFragment();


                fragmentStack.replace(settingsFragment);


            } else {
                navigationView.setSelectedItemId(R.id.settings);
                SettingsFragment settingsFragment = new SettingsFragment();


                fragmentStack.replace(settingsFragment);
                fragmentStack.push(new NotificationFragments());

            }
        }


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.products)
                    fragmentStack.replace(homeFragment);
                else if (item.getItemId() == R.id.support) {
                    String token = Paper.book().read("token", "none");

                    if (token.equals("none")) {
                        Toast.makeText(MainActivity.this, "يجب عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    } else {
                        fragmentStack.replace(new ChatListFragment());

                    }

                } else if (item.getItemId() == R.id.settings) {

                    String token = Paper.book().read("token", "none");
                    fragmentStack.replace(new SettingsFragment());

                   /* if (token.equals("none")) {
                        Toast.makeText(MainActivity.this, "يجب عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    } else {
                        fragmentStack.replace(new SettingsFragment());
                        // throw new RuntimeException("Test Crash"); // Force a crash


                    }*/                   // throw new RuntimeException("Test Crash"); // Force a crash

                } else if (item.getItemId() == R.id.orders)
                    fragmentStack.replace(new OrderFragment());


                return true;
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "updated " + resultCode, Toast.LENGTH_SHORT).show();
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, "canceled " + resultCode, Toast.LENGTH_SHORT).show();
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else if (resultCode != ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(this, "failed " + resultCode, Toast.LENGTH_SHORT).show();
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    public void bottomNavVisibility(boolean isVisible) {
        if (isVisible)
            navigationView.setVisibility(View.VISIBLE);
        else
            navigationView.setVisibility(View.GONE);
    }

    public void showDialog(Boolean show) {
        if (dialog == null) {
            dialog = KProgressHUD.create(MainActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }

        if (show) {
            if (!dialog.isShowing())
                dialog.show();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }


    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Lingver.getInstance().setLocale(this, "ar");
        String token = Paper.book().read("token", "none");

        if (!token.equals("none")) {
            viewModel.getNotifyCount("Bearer " + token);
            viewModel.getOrdersCount("Bearer " + token);

        }

        viewModel.notifyCount.observe(this, new Observer<NotifyCountModel>() {
            @Override
            public void onChanged(NotifyCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess()) {
                        if (notifyCountModel.getData().getUnread() > 0)
                            navigationView.getOrCreateBadge(R.id.support).setNumber(Integer.parseInt(notifyCountModel.getData().getUnread().toString()));
                        else
                            navigationView.removeBadge(R.id.support);

                    }
                }
            }
        });

        viewModel.ordersCount.observe(this, new Observer<OrdersCountModel>() {
            @Override
            public void onChanged(OrdersCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess()) {
                        if (notifyCountModel.getData().getHasNewUpdates())
                            navigationView.getOrCreateBadge(R.id.orders).setNumber(notifyCountModel.getData().getCount());
                    } else
                        navigationView.removeBadge(R.id.orders);
                }
            }
        });
    }
}