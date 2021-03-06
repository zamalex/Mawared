package app.mawared.alhayat.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.cities.Cities;
import app.mawared.alhayat.helpers.CustomeSpinnerAdapter;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.register.model.RegisterBody;
import app.mawared.alhayat.register.model.RegisterModel;
import io.paperdb.Paper;

public class RegisterBottomSheet extends BottomSheetDialogFragment {

    ConstraintLayout btn_confirm;
    Dialog dialog;
    RegisterViewModel registerViewModel;
    Spinner city_spinner;


    List<String> cityNameList;
    List<String> cityIdLis;
    CustomeSpinnerAdapter adapter;
    String selectedCityName;
    String selectedCityId;
    String code;
    RegisterBody registerBody;
    EditText et_regiFullName,et_emailAddress,et_regiPassword;

    KProgressHUD load;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String languageToLoad  = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());

        View view = inflater.inflate(R.layout.register_bottom_sheet,container,false);

        load = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        cityNameList = new ArrayList<>();
        cityIdLis = new ArrayList<>();
        registerBody = new RegisterBody();
        et_regiFullName = view.findViewById(R.id.et_regiFullName);
        et_emailAddress = view.findViewById(R.id.et_emailAddress);
        et_regiPassword = view.findViewById(R.id.et_regiPassword);

        city_spinner = view.findViewById(R.id.city_spinnerSheet);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.getGetCities().observe(getActivity(), new Observer<Cities>() {
            @Override
            public void onChanged(Cities cities) {
                for (int i = 0; i < cities.getData().size(); i++) {
                    cityNameList.add(cities.getData().get(i).getName());
                    cityIdLis.add(cities.getData().get(i).getId());
                }
            }
        });
        btn_confirm = view.findViewById(R.id.btn_confirmTerms);

        cityNameList.add("المدينة");
        cityIdLis.add("0");

        adapter = new CustomeSpinnerAdapter(getActivity(),cityNameList,cityIdLis);

        city_spinner.setAdapter(adapter);

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            if (cityNameList != null && cityNameList.size() >= 1) {
                selectedCityName = (String) cityNameList.get(position);
                selectedCityId = (String) cityIdLis.get(position);
            }
            //Log.e("carCity",selectedCityId+"");
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });



        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_emailAddress.getText().toString() != null && et_regiFullName.getText().toString() != null && et_regiPassword.getText().toString() != null) {
                    String mobileNumber = getActivity().getSharedPreferences("mwared", Context.MODE_PRIVATE).getString("mobNum", "");
                    registerBody.setCity_id(selectedCityId);
                    registerBody.setCode(code);
                    registerBody.setEmail(et_emailAddress.getText().toString());
                    registerBody.setPassword(et_regiPassword.getText().toString());
                    registerBody.setMobile(mobileNumber);
                    registerBody.setCountry_code("sa");
                    registerBody.setPassword_confirmation(et_regiPassword.getText().toString());
                    registerBody.setName(et_regiFullName.getText().toString());

                    load.show();
                    registerViewModel.setNewAccount(registerBody).observe(getActivity(), new Observer<LoginResponse>() {
                        @Override
                        public void onChanged(LoginResponse responseBody) {
                           // Log.e("happen", responseBody.toString() );
                            load.dismiss();
                            if (responseBody!=null){
                                Toast.makeText(getActivity(), responseBody.getMessage().getDescription(), Toast.LENGTH_SHORT).show();
                                if (responseBody.getStatus()==200){
                                    Paper.book().write("login",responseBody);
                                    Paper.book().write("token", responseBody.getUser().getToken());

                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });

                }
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupRatio(bottomSheetDialog);
            }
        });
        return  dialog;
    }

    private void setupRatio(BottomSheetDialog bottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        FrameLayout bottomSheet = (FrameLayout)
                bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = getBottomSheetDialogDefaultHeight();
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private int getBottomSheetDialogDefaultHeight() {
        return getWindowHeight() * 85 / 100;
    }
    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
