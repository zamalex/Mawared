package app.mawared.alhayat.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.forgot.ForgotPasswordActivity;
import app.mawared.alhayat.home.HomeViewModel;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.registeration.RegisterationActivity;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    LoginViewModel viewModel;
    HomeViewModel homeViewModel;


    EditText phone_et;

    EditText pass_et;

    Button login;

    TextView forgot, go_register;

    KProgressHUD dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(LoginViewModel.class);
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(HomeViewModel.class);


        phone_et = findViewById(R.id.phone_et);

        pass_et = findViewById(R.id.pass_et);

        login = findViewById(R.id.login_btn);
        forgot = findViewById(R.id.forgot);
        go_register = findViewById(R.id.go_register);


        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
                LoginActivity.this.finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phone_et.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "ادخل رقم الجوال", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass_et.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "ادخل كلمة المرور", Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("mobile", phone_et.getText() + "");
                jsonObject.addProperty("password", pass_et.getText() + "");
                jsonObject.addProperty("country_code", "SA");

                dialog.show();
                viewModel.login(jsonObject);
            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        viewModel.loginResponse.observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                dialog.dismiss();
                if (loginResponse != null) {
                    if (loginResponse.getStatus() == 200) {
                        Paper.book().write("token", loginResponse.getUser().getToken());
                        Paper.book().write("login", loginResponse);
                        // viewModel.checkUserCart(loginResponse.getUser().getId().toString());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();

                    }
                } else {
                    Toast.makeText(LoginActivity.this, "البيانات التي ادخلتها غير صحيحة", Toast.LENGTH_SHORT).show();
                }

            }
        });


       /* viewModel.checkCardModelMutableLiveData.observe(this, new Observer<CheckCardModel>() {
            @Override
            public void onChanged(CheckCardModel checkCardModel) {
                dialog.dismiss();


                if (checkCardModel!=null){
                    if (checkCardModel.getStatus()==200){
                        Paper.book().write("cid",checkCardModel.getData().getCartId());
                    }
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }
        });
*/

    }
}