package app.mawared.alhayat.update.email;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.update.UpdateViewModel;
import app.mawared.alhayat.update.model.UpdateModel;
import io.paperdb.Paper;


public class EmailFragment extends Fragment {

    Button confirm;
    EditText email_et;
    UpdateViewModel viewModel;
    VerifyLoginResponse mLoginResponse;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_email, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UpdateViewModel.class);

        confirm = v.findViewById(R.id.confirm_btn);
        email_et = v.findViewById(R.id.mail_et);

        mLoginResponse = Paper.book().read("login", null);

        if (mLoginResponse != null)
            email_et.setText(mLoginResponse.getUser().getEmail());


        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل البريد الالكتروني", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity)getActivity()).showDialog(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email_et.getText().toString());
                viewModel.updateEmail(jsonObject,"Bearer "+ mLoginResponse.getAccessToken());

            }
        });


        viewModel.updateEmailRes.observe(getViewLifecycleOwner(), new Observer<UpdateModel>() {
            @Override
            public void onChanged(UpdateModel loginResponse) {
                ((MainActivity)getActivity()).showDialog(false);

                if (loginResponse != null) {
                    if (loginResponse.getStatus() == 200) {
                        Toast.makeText(getActivity(), "تم تحديث البريد الالكتروني", Toast.LENGTH_SHORT).show();
                        mLoginResponse.getUser().setEmail(loginResponse.getUser().getEmail());

                        Paper.book().write("login",mLoginResponse);
                    }
                }
            }
        });

        return v;
    }
}