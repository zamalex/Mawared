package creativitysol.com.mawared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

     FragmentStack fragmentStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.activity_main);



        fragmentStack = new FragmentStack(this,getSupportFragmentManager(),R.id.main_container);
        fragmentStack.replace(new HomeFragment());

    }


}