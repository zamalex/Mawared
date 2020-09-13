package creativitysol.com.mawared.activiation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
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
import creativitysol.com.mawared.helpers.SmsListener;
import creativitysol.com.mawared.register.RegisterBottomSheet;
import creativitysol.com.mawared.reset.ResetPassActivity;
import okhttp3.ResponseBody;

public class ActivationActivity extends AppCompatActivity implements SmsListener.OnSmsReceivedListener {

    ConstraintLayout btn_login;
    String phoneNumber;
    String type = null;
    TextView tv_mobileNumber;
    SquarePinField sq_verification;
    ActivationViewModel activationViewModel;
    private SmsListener receiver;
    KProgressHUD dialog;

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
        setContentView(R.layout.activity_activation);


        if (receiver == null)
            receiver = new SmsListener();
        receiver.setOnSmsReceivedListener(this);

        IntentFilter fp = new IntentFilter();
        fp.addAction("android.provider.Telephony.SMS_RECEIVED");
        fp.setPriority(18);

        registerReceiver(receiver, fp);


        type = getIntent().getStringExtra("type");
        phoneNumber = getIntent().getStringExtra("mobNo");
        tv_mobileNumber = findViewById(R.id.tv_mobileNumber);
        sq_verification = findViewById(R.id.sq_verification);
        tv_mobileNumber.setText(phoneNumber + "+");

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
                jsonObject.addProperty("mobile", phoneNumber);
                jsonObject.addProperty("code", codeVerification);
                dialog.show();

                activationViewModel.verifyMobile(jsonObject).observe(ActivationActivity.this, new Observer<ActivationiModel>() {
                    @Override
                    public void onChanged(ActivationiModel responseBody) {
                        dialog.dismiss();
                        if (responseBody != null) {
                            if (responseBody.getStatus() == 200) {
                                Toast.makeText(getApplicationContext(), responseBody.getMessage().getDescription(), Toast.LENGTH_LONG).show();

                                if (type.equals("forgot")){
                                    Intent intent = new Intent(ActivationActivity.this, ResetPassActivity.class);
                                    intent.putExtra("mobNo",phoneNumber);
                                    intent.putExtra("code",codeVerification);
                                    startActivity(intent);
                                    ActivationActivity.this.finish();


                                }else {
                                    RegisterBottomSheet registerBottomSheet = new RegisterBottomSheet();

                                    registerBottomSheet.setCode(codeVerification);

                                    registerBottomSheet.show(getSupportFragmentManager(), "tag");
                                }


                            }
                        }

                    }
                });
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (receiver == null)
            receiver = new SmsListener();
        receiver.setOnSmsReceivedListener(this);

        IntentFilter fp = new IntentFilter();
        fp.addAction("android.provider.Telephony.SMS_RECEIVED");
        fp.setPriority(18);

        registerReceiver(receiver, fp);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    @Override
    public void onSmsReceived(String otp) {
        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
    }
}