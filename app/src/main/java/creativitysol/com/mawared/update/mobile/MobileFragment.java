package creativitysol.com.mawared.update.mobile;

import android.Manifest;
import android.content.Intent;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.activiation.ActivationActivity;
import creativitysol.com.mawared.forgot.ForgotPasswordActivity;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.update.UpdateViewModel;
import creativitysol.com.mawared.update.model.SendCodeModel;
import creativitysol.com.mawared.update.model.UpdateModel;
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
        checkSmsPermission();
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

                viewModel.sendCode(jsonObject, "Bearer " + mLoginResponse.getUser().getToken());

            }
        });


        viewModel.sendCodeModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<SendCodeModel>() {
            @Override
            public void onChanged(SendCodeModel sendCodeModel) {
                ((MainActivity) getActivity()).showDialog(false);

                if (sendCodeModel != null) {
                    if (sendCodeModel.getSuccess()) {
                        Intent intent = new Intent(getActivity(), ActivationActivity.class);
                        intent.putExtra("type", "update");
                        intent.putExtra("mobNo", phone_et.getText().toString());

                        startActivity(intent);

                    } else
                        Toast.makeText(getActivity(), "الرقم غير صحيح", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "الرقم غير صحيح", Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }


    void checkSmsPermission() {
        Dexter.withContext(getActivity())
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