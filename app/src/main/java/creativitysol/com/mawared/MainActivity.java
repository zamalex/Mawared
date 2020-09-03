package creativitysol.com.mawared;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import creativitysol.com.mawared.about.AboutMawaredFragment;
import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.home.HomeFragment;
import creativitysol.com.mawared.support.SupportFragment;
import creativitysol.com.mawared.support.chat.ChatFragment;

public class MainActivity extends AppCompatActivity {

    public FragmentStack fragmentStack;
    BottomNavigationView navigationView;

    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.activity_main);

        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();

        navigationView = findViewById(R.id.navigation);

        final HomeFragment homeFragment = new HomeFragment();
        final SupportFragment supportFragment = new SupportFragment();
        final ChatFragment chatFragment = new ChatFragment();
        final AboutMawaredFragment aboutMawaredFragment = new AboutMawaredFragment();


        fragmentStack = new FragmentStack(this, getSupportFragmentManager(), R.id.main_container);
        fragmentStack.replace(homeFragment);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.products)
                    fragmentStack.replace(homeFragment);
                else if (item.getItemId() == R.id.support)
                    fragmentStack.replace(supportFragment);
                else if (item.getItemId() == R.id.settings)
                    fragmentStack.replace(aboutMawaredFragment);
                // else if (item.getItemId() == R.id.orders)
                //fragmentStack.replace(chatFragment);

                return true;
            }
        });


    }

    public void bottomNavVisibility(boolean isVisible) {
        if (isVisible)
            navigationView.setVisibility(View.VISIBLE);
        else
            navigationView.setVisibility(View.GONE);
    }

    public void showDialog(Boolean show) {
        if (dialog == null) {
            dialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
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

}