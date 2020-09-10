package creativitysol.com.mawared.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.forgot.ForgotPasswordActivity;
import creativitysol.com.mawared.login.model.LoginResponse;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    LoginViewModel viewModel;

    EditText phone_et;

    EditText pass_et;

    Button login;

    TextView forgot;

    ACProgressFlower dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(LoginViewModel.class);


        phone_et = findViewById(R.id.phone_et);

        pass_et = findViewById(R.id.pass_et);

        login = findViewById(R.id.login_btn);
        forgot = findViewById(R.id.forgot);


        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                dialog.cancel();
                if (loginResponse.getStatus() == 200) {
                    Paper.book().write("token", loginResponse.getUser().getToken());
                    Paper.book().write("login", loginResponse);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            }
        });


    }
}