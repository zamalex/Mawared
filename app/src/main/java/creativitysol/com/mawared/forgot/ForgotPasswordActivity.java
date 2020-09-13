package creativitysol.com.mawared.forgot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.activiation.ActivationActivity;
import creativitysol.com.mawared.forgot.model.ForgotModel;

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

                    dialog.show();
                    viewModel.forgotPass(jsonObject);
                }
            }
        });


        viewModel.res.observe(this, new Observer<ForgotModel>() {
            @Override
            public void onChanged(ForgotModel forgotModel) {
                dialog.dismiss();
                if (forgotModel != null) {
                    if (forgotModel.getStatus() == 200) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, ActivationActivity.class);
                        intent.putExtra("type", "forgot");
                        intent.putExtra("mobNo", phone_et.getText().toString());

                        startActivity(intent);

                    } else
                        Toast.makeText(ForgotPasswordActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}