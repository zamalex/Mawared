package app.mawared.alhayat.activiation;

import androidx.annotation.NonNull;
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

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.SmsBroadcastReceiver;
import app.mawared.alhayat.activiation.model.ActivationiModel;
import app.mawared.alhayat.helpers.SmsListener;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.register.RegisterBottomSheet;
import app.mawared.alhayat.registeration.RegisterationActivity;
import app.mawared.alhayat.reset.ResetPassActivity;
import app.mawared.alhayat.update.UpdateViewModel;
import app.mawared.alhayat.update.model.UpdateModel;
import io.paperdb.Paper;

public class ActivationActivity extends AppCompatActivity implements SmsBroadcastReceiver.OtpReceivedInterface{

    ConstraintLayout btn_login;
    String phoneNumber;
    String type = null;
    TextView tv_mobileNumber, counter;
    SquarePinField sq_verification;
    ActivationViewModel activationViewModel;

    UpdateViewModel updateViewModel;

    KProgressHUD dialog;
    CountDownTimer countDownTimer;
    String token = null;

    SmsBroadcastReceiver mSmsBroadcastReceiver;


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

        token = Paper.book().read("token", null);





        mSmsBroadcastReceiver = new SmsBroadcastReceiver();



        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);



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

        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(String.format("%02d:%02d",
                        ((millisUntilFinished/1000) % 3600) / 60, ((millisUntilFinished/1000) % 60)));

            }

            public void onFinish() {
                counter.setText("00:00");
                Toast.makeText(ActivationActivity.this, "حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivationActivity.this, RegisterationActivity.class));
                ActivationActivity.this.finish();
            }
        }.start();


        activationViewModel = new ViewModelProvider(this).get(ActivationViewModel.class);
        updateViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

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
              //  dialog.show();

                activationViewModel.verifyMobile(jsonObject).observe(ActivationActivity.this, new Observer<ActivationiModel>() {
                    @Override
                    public void onChanged(ActivationiModel responseBody) {
                      //  dialog.dismiss();
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


                                } else if (type.equals("update")) {

                                    JsonObject jsonObject1 = new JsonObject();

                                    jsonObject.addProperty("mobile", phoneNumber);
                                    jsonObject.addProperty("code", codeVerification);


                                    if (token != null) {
                                        dialog.show();
                                        updateViewModel.updateMob(jsonObject, "Bearer " + token);

                                    }


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


        updateViewModel.updateEmailRes.observe(this, new Observer<UpdateModel>() {
            @Override
            public void onChanged(UpdateModel updateModel) {
                dialog.dismiss();
                if (updateModel != null) {
                    if (updateModel.getSuccess()) {
                        Toast.makeText(ActivationActivity.this, "تم تحديث رقم الجوال", Toast.LENGTH_SHORT).show();
                        LoginResponse loginResponse = Paper.book().read("login");
                        loginResponse.getUser().setMobile(phoneNumber);

                        Paper.book().write("login",loginResponse);

                    }
                } else
                    Toast.makeText(ActivationActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivationActivity.this, MainActivity.class));
                ActivationActivity.this.finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if(mSmsBroadcastReceiver!=null){
            try {
                //unregisterReceiver(mSmsBroadcastReceiver);

            }catch (Exception e){}
        }
    }

   /* @Override
    public void onSmsReceived(String otp) {
        String number = otp.replaceAll("[^0-9]", "");
        // Toast.makeText(this, number, Toast.LENGTH_SHORT).show();

        if (sq_verification == null)
            sq_verification = findViewById(R.id.sq_verification);
        if (number.length() == 4)
            sq_verification.setText(number);
    }*/


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onOtpReceived(String otp) {
        String number = otp.replaceAll("[^0-9]", "");
        // Toast.makeText(this, number, Toast.LENGTH_SHORT).show();


        for (String s : otp.split(" ")) {
            if (s.replace(" ","").length() == 4) {
                if (android.text.TextUtils.isDigitsOnly(s)) {
                    if (sq_verification == null)
                        sq_verification = findViewById(R.id.sq_verification);
                    Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
                    sq_verification.setText(s);
                }
            }
        }
    }


    @Override
    public void onOtpTimeout() {

    }
}