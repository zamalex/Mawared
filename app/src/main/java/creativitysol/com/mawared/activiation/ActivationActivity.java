package creativitysol.com.mawared.activiation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.activiation.model.ActivationiModel;
import creativitysol.com.mawared.register.RegisterBottomSheet;
import okhttp3.ResponseBody;

public class ActivationActivity extends AppCompatActivity {

    ConstraintLayout btn_login;
    String phoneNumber;
    TextView tv_mobileNumber;
    SquarePinField sq_verification;
    ActivationViewModel activationViewModel;

    KProgressHUD dialog;

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
        setContentView(R.layout.activity_activation);
        phoneNumber = getIntent().getStringExtra("mobNo");
        tv_mobileNumber = findViewById(R.id.tv_mobileNumber);
        sq_verification = findViewById(R.id.sq_verification);
        tv_mobileNumber.setText(phoneNumber+"+");

        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        activationViewModel = new ViewModelProvider(this).get(ActivationViewModel.class);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        sq_verification.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(@NotNull String enteredText) {
                String codeVerification = enteredText;
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mobile",phoneNumber);
                jsonObject.addProperty("code",codeVerification);
                dialog.show();
                activationViewModel.verifyMobile(jsonObject).observe(ActivationActivity.this, new Observer<ActivationiModel>() {
                    @Override
                    public void onChanged(ActivationiModel responseBody) {
                        dialog.dismiss();
                        if(responseBody != null){
                            if (responseBody.getStatus()==200){
                                Toast.makeText(getApplicationContext(),responseBody.getMessage().getDescription(),Toast.LENGTH_LONG).show();
                                RegisterBottomSheet registerBottomSheet = new RegisterBottomSheet();

                                registerBottomSheet.setCode(codeVerification);

                                registerBottomSheet.show(getSupportFragmentManager(),"tag");

                            }
                        }

                    }
                });
                return true;
            }
        });

    }
}