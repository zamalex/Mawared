package creativitysol.com.mawared;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import creativitysol.com.mawared.login.LoginActivity;
import io.paperdb.Paper;


public class SplashActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        timer = new Timer();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String token = Paper.book().read("token", "none");
                if (token.equals("none")) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                }



                this.cancel();
            }
        },3000);


    }

}
