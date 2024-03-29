package app.mawared.alhayat.update.name;

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


public class NameFragment extends Fragment {



    Button confirm;
    EditText name_et;
    UpdateViewModel viewModel;
    VerifyLoginResponse mLoginResponse;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_name, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UpdateViewModel.class);

        confirm = v.findViewById(R.id.confirm_btn);
        name_et = v.findViewById(R.id.name_et);

        mLoginResponse = Paper.book().read("login", null);

        if (mLoginResponse != null)
            name_et.setText(mLoginResponse.getUser().getName());


        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل الاسم", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity)getActivity()).showDialog(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", name_et.getText().toString());
                viewModel.updateName(jsonObject,"Bearer "+ mLoginResponse.getAccessToken());

            }
        });


        viewModel.updateEmailRes.observe(getViewLifecycleOwner(), new Observer<UpdateModel>() {
            @Override
            public void onChanged(UpdateModel loginResponse) {
                ((MainActivity)getActivity()).showDialog(false);

                if (loginResponse != null) {
                    if (loginResponse.getSuccess()) {
                        Toast.makeText(getActivity(), "تم تحديث الاسم", Toast.LENGTH_SHORT).show();
                        mLoginResponse.getUser().setName(loginResponse.getUser().getName());

                        Paper.book().write("login",mLoginResponse);
                    }
                }
            }
        });

        return v;
    }
}