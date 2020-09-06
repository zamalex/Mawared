package creativitysol.com.mawared.forgot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import creativitysol.com.mawared.R;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText phone_et;

    Button confirm;
    ImageView back;

    ForgotViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phone_et = findViewById(R.id.phone_et);
        confirm = findViewById(R.id.confirm_btn);
        back = findViewById(R.id.back);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(ForgotViewModel.class);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_et.getText().length()<4){
                    Toast.makeText(ForgotPasswordActivity.this, "ادخل رقم صحيح", Toast.LENGTH_SHORT).show();
                }
                else {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("country_code","sa");
                    jsonObject.addProperty("mobile",phone_et.getText().toString());
                    jsonObject.addProperty("method","mobile");

                    viewModel.forgotPass(jsonObject);
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