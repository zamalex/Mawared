package app.mawared.alhayat.registeration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Locale;

import app.mawared.alhayat.R;
import app.mawared.alhayat.activiation.ActivationActivity;
import app.mawared.alhayat.registeration.model.LoginRegistration;
import app.mawared.alhayat.registeration.terms.TermsBottomSheet;

public class RegisterationActivity extends AppCompatActivity {

    TextView tv_conditions;
    EditText et_phoneNumber;
    ConstraintLayout btn_login;
    RegisterationViewModel registerationViewModel;
    String phoneNumber;
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
        setContentView(R.layout.activity_registeration);
        tv_conditions = findViewById(R.id.tv_conditions);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        registerationViewModel = new ViewModelProvider(this).get(RegisterationViewModel.class);
        btn_login = findViewById(R.id.btn_login);
        tv_conditions.setPaintFlags(tv_conditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        checkSmsPermission();

        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        tv_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TermsBottomSheet termsBottomSheet = new TermsBottomSheet();

                termsBottomSheet.show(getSupportFragmentManager(),"tag");
            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = et_phoneNumber.getText().toString();
                JsonObject jObj = new JsonObject();

                    jObj.addProperty("mobile",phoneNumber);
                    dialog.show();
                    registerationViewModel.checkMobile(jObj).observe(RegisterationActivity.this, new Observer<LoginRegistration>() {
                        @Override
                        public void onChanged(LoginRegistration loginRegistration) {
                            dialog.dismiss();
                            if (loginRegistration != null) {
                                if (loginRegistration.getStatus() == 200) {
                                    getSharedPreferences("mwared", Context.MODE_PRIVATE).edit().putString("mobNum",phoneNumber).commit();
                                    Intent activationIntent = new Intent(RegisterationActivity.this,
                                            ActivationActivity.class);
                                    activationIntent.putExtra("mobNo",phoneNumber);
                                    startActivity(activationIntent);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"الرقم الذي أدخلته غير صحيح",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        });
    }

    void checkSmsPermission(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                           // Toast.makeText(getActivity(), "قم بالسماح للتطبيق للوصول الى موقعك من خلال الاعدادات", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();


                    }
                })
                .onSameThread()
                .check();
    }
}