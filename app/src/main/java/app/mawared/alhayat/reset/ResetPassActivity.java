package app.mawared.alhayat.reset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.reset.model.ResetModel;
import io.paperdb.Paper;

public class ResetPassActivity extends AppCompatActivity {
    EditText pass_et,repass_et;
    Button confirm_btn;
    ResetViewModel viewModel;
    KProgressHUD dialog;
    String phoneNo,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ResetViewModel.class);


        pass_et = findViewById(R.id.pass_et);
        repass_et = findViewById(R.id.repass_et);
        confirm_btn = findViewById(R.id.confirm_btn);


        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        phoneNo = getIntent().getStringExtra("mobNo");
        code = getIntent().getStringExtra("code");


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_et.getText().toString().isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "ادخل كلمة المرور", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (repass_et.getText().toString().isEmpty()){
                    Toast.makeText(ResetPassActivity.this, "اعد ادخال كلمة المرور", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!repass_et.getText().toString().equals(pass_et.getText().toString())){
                    Toast.makeText(ResetPassActivity.this, "يجب ان تتطابق كلمة المرور", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("country_code","sa");
                jsonObject.addProperty("mobile",phoneNo);
                jsonObject.addProperty("method","mobile");
                jsonObject.addProperty("password",pass_et.getText().toString());
                jsonObject.addProperty("password_confirmation",repass_et.getText().toString());
                jsonObject.addProperty("token",code);
                dialog.show();
                viewModel.resetPass(jsonObject);


            }
        });

        viewModel.res.observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse resetModel) {
                dialog.dismiss();
                if (resetModel!=null){
                    Toast.makeText(ResetPassActivity.this, resetModel.getMessage().getDescription(), Toast.LENGTH_SHORT).show();

                    if (resetModel.getStatus()==200){
                       // startActivity(new Intent(ResetPassActivity.this, LoginActivity.class));
                       // ResetPassActivity.this.finish();
                        Paper.book().write("token", resetModel.getUser().getToken());
                        Paper.book().write("login", resetModel);
                        startActivity(new Intent(ResetPassActivity.this, MainActivity.class));
                        ResetPassActivity.this.finish();
                    }
                }
            }
        });

    }
}