package app.mawared.alhayat.update.mobile;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.activiation.ActivationActivity;
import app.mawared.alhayat.forgot.ForgotPasswordActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.update.UpdateViewModel;
import app.mawared.alhayat.update.model.SendCodeModel;
import io.paperdb.Paper;


public class MobileFragment extends Fragment {


    Button confirm;
    EditText phone_et;
    UpdateViewModel viewModel;
    LoginResponse mLoginResponse;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_mobile, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UpdateViewModel.class);
        confirm = v.findViewById(R.id.confirm_btn);
        phone_et = v.findViewById(R.id.phone_et);

        mLoginResponse = Paper.book().read("login", null);

        if (mLoginResponse != null)
            phone_et.setText(mLoginResponse.getUser().getMobile());


        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل رقم الجوال", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity) getActivity()).showDialog(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mobile", phone_et.getText().toString());
                jsonObject.addProperty("sms_token","3NAjIDnZDcG");

                viewModel.sendCode(jsonObject, "Bearer " + mLoginResponse.getUser().getToken());

            }
        });


        viewModel.sendCodeModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<SendCodeModel>() {
            @Override
            public void onChanged(SendCodeModel sendCodeModel) {
                ((MainActivity) getActivity()).showDialog(false);

                if (sendCodeModel != null) {
                    if (sendCodeModel.getSuccess()) {
                     startSMSListener();

                    } else
                        Toast.makeText(getActivity(), "الرقم غير صحيح", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "الرقم غير صحيح", Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(getActivity());
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

                Intent intent = new Intent(getActivity(), ActivationActivity.class);
                intent.putExtra("type", "update");
                intent.putExtra("mobNo", phone_et.getText().toString());

                startActivity(intent);

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
            }
        });
    }

}