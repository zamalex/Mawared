package creativitysol.com.mawared.activiation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import creativitysol.com.mawared.registeration.RegisterationActivity;
import creativitysol.com.mawared.reset.ResetPassActivity;
import okhttp3.ResponseBody;

public class ActivationActivity extends AppCompatActivity implements SmsListener.OnSmsReceivedListener {

    ConstraintLayout btn_login;
    String phoneNumber;
    String type = null;
    TextView tv_mobileNumber,counter;
    SquarePinField sq_verification;
    ActivationViewModel activationViewModel;
    private SmsListener receiver;
    KProgressHUD dialog;
    CountDownTimer countDownTimer;


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
        counter = findViewById(R.id.counter);
        tv_mobileNumber.setText(phoneNumber + "+");

        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        countDownTimer= new CountDownTimer(59000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText("00:" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counter.setText("00:00");
                Toast.makeText(ActivationActivity.this, "حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivationActivity.this, RegisterationActivity.class));
                ActivationActivity.this.finish();
            }
        }.start();


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

                                if (type == null) {

                                    countDownTimer.cancel();
                                    RegisterBottomSheet registerBottomSheet = new RegisterBottomSheet();

                                    registerBottomSheet.setCode(codeVerification);

                                    registerBottomSheet.show(getSupportFragmentManager(), "tag");
                                } else if (type.equals("forgot")) {
                                    Intent intent = new Intent(ActivationActivity.this, ResetPassActivity.class);
                                    intent.putExtra("mobNo", phoneNumber);
                                    intent.putExtra("code", codeVerification);
                                    startActivity(intent);
                                    ActivationActivity.this.finish();


                                }

                            } else
                                Toast.makeText(ActivationActivity.this, "كود التحقق خاطئ", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(ActivationActivity.this, "كود التحقق خاطئ", Toast.LENGTH_SHORT).show();

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
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onSmsReceived(String otp) {
        String number  = otp.replaceAll("[^0-9]", "");
       // Toast.makeText(this, number, Toast.LENGTH_SHORT).show();

        if (sq_verification==null)
            sq_verification = findViewById(R.id.sq_verification);
        if (number.length()==4)
            sq_verification.setText(number);
    }
}