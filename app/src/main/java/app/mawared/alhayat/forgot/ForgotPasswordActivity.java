package app.mawared.alhayat.forgot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import app.mawared.alhayat.R;
import app.mawared.alhayat.activiation.ActivationActivity;
import app.mawared.alhayat.forgot.model.ForgotModel;
import app.mawared.alhayat.registeration.RegisterationActivity;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText phone_et;

    Button confirm;
    ImageView back;
    KProgressHUD dialog;
    ForgotViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phone_et = findViewById(R.id.phone_et);
        confirm = findViewById(R.id.confirm_btn);
        back = findViewById(R.id.back);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ForgotViewModel.class);



        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_et.getText().length() < 4) {
                    Toast.makeText(ForgotPasswordActivity.this, "ادخل رقم صحيح", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("country_code", "sa");
                    jsonObject.addProperty("mobile", phone_et.getText().toString());
                    jsonObject.addProperty("method", "mobile");
                    jsonObject.addProperty("sms_token","NLeMjm76BfQ");

                    dialog.show();
                    viewModel.forgotPass(jsonObject);
                }
            }
        });


        viewModel.res.observe(this, new Observer<ForgotModel>() {
            @Override
            public void onChanged(ForgotModel forgotModel) {
                dialog.dismiss();
              //  startSMSListener();
                if (forgotModel != null) {
                    if (forgotModel.getStatus() == 200) {

                        startSMSListener();

                    } else
                        Toast.makeText(ForgotPasswordActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                }else                         Toast.makeText(ForgotPasswordActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

               /* Intent intent = new Intent(ForgotPasswordActivity.this, ActivationActivity.class);
                intent.putExtra("type", "forgot");
                intent.putExtra("mobNo", phone_et.getText().toString());

                startActivity(intent);*/

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
            }
        });
        Intent intent = new Intent(ForgotPasswordActivity.this, ActivationActivity.class);
        intent.putExtra("type", "forgot");
        intent.putExtra("mobNo", phone_et.getText().toString());

        startActivity(intent);
    }

}