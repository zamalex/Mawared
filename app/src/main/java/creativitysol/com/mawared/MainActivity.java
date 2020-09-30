package creativitysol.com.mawared;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yariksoffice.lingver.Lingver;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import creativitysol.com.mawared.about.AboutMawaredFragment;
import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.home.HomeFragment;
import creativitysol.com.mawared.login.LoginActivity;
import creativitysol.com.mawared.orders.OrderFragment;
import creativitysol.com.mawared.settings.SettingsFragment;
import creativitysol.com.mawared.support.SupportFragment;
import creativitysol.com.mawared.support.chat.ChatFragment;
import creativitysol.com.mawared.support.chatlist.ChatListFragment;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    public FragmentStack fragmentStack;
    BottomNavigationView navigationView;
    private boolean paymentSuccess  = false;
    KProgressHUD dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        setContentView(R.layout.activity_main);

        dialog = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        navigationView = findViewById(R.id.navigation);

        final HomeFragment homeFragment = new HomeFragment();
        final SupportFragment supportFragment = new SupportFragment();
        final ChatFragment chatFragment = new ChatFragment();
        final AboutMawaredFragment aboutMawaredFragment = new AboutMawaredFragment();


        fragmentStack = new FragmentStack(this, getSupportFragmentManager(), R.id.main_container);
        fragmentStack.replace(homeFragment);

        if (getIntent().getStringExtra("order")!=null){
            if (getIntent().getStringExtra("order").equals("order")){
                navigationView.setSelectedItemId(R.id.orders);
                fragmentStack.replace(new OrderFragment());

            }
        }


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.products)
                    fragmentStack.replace(homeFragment);
                else if (item.getItemId() == R.id.support) {
                    String token = Paper.book().read("token", "none");

                    if (token.equals("none")){
                        Toast.makeText(MainActivity.this, "يجب عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }else {
                        fragmentStack.replace(new ChatListFragment());

                    }

                }
                else if (item.getItemId() == R.id.settings) {

                    String token = Paper.book().read("token", "none");

                    if (token.equals("none")){
                        Toast.makeText(MainActivity.this, "يجب عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }else {
                        fragmentStack.replace(new SettingsFragment());

                    }
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
    }
}