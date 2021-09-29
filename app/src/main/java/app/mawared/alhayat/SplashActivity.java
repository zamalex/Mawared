package app.mawared.alhayat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

import app.mawared.alhayat.defaultaddress.DefaultAddressResponse;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.registeration.RegisterationViewModel;
import io.paperdb.Paper;


public class SplashActivity extends AppCompatActivity {

    Timer timer;
    LatLng latLng;
    AddressViewModel viewModel;
    VerifyLoginResponse loginResponse = Paper.book().read("login",null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        timer = new Timer();


        if (loginResponse==null)
            viewModel.getDafaultAddress("");
        else
        viewModel.getDafaultAddress(loginResponse.getAccessToken());

        viewModel.defaultAddressResponseMutableLiveData.observe(this, new Observer<DefaultAddressResponse>() {
            @Override
            public void onChanged(DefaultAddressResponse defaultAddressResponse) {
                if (defaultAddressResponse!=null){
                    if (defaultAddressResponse.isSuccess()){
                        if (defaultAddressResponse.getData()!=null){
                            Paper.book().write("latlng",new LatLng(Double.parseDouble(defaultAddressResponse.getData().getLat()),Double.parseDouble(defaultAddressResponse.getData().getLng())));
                            Paper.book().write("address",new AddressModel(Double.parseDouble(defaultAddressResponse.getData().getLat()),Double.parseDouble(defaultAddressResponse.getData().getLng()),defaultAddressResponse.getData().getMobile(),defaultAddressResponse.getData().getAddress(),defaultAddressResponse.getData().getUsername(),"personal",true,defaultAddressResponse.getData().getCityId()));

                        }
                    }
                }
                latLng = Paper.book().read("latlng",null);

                if(latLng==null)
                    startActivity(new Intent(SplashActivity.this, MapsActivity.class));

                else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));

                SplashActivity.this.finish();
            }
        });


       /* timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String token = Paper.book().read("token", "none");

                if(latLng==null)
                startActivity(new Intent(SplashActivity.this, MapsActivity.class));

               else
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

               SplashActivity.this.finish();



                this.cancel();
            }
        }, 3000);*/


    }

}
