package creativitysol.com.mawared.settings;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.about.AboutMawaredFragment;
import creativitysol.com.mawared.login.LoginActivity;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.notification.NotificationViewModel;
import creativitysol.com.mawared.notification.model.Notification;
import creativitysol.com.mawared.registeration.terms.TermsBottomSheet;
import creativitysol.com.mawared.update.email.EmailFragment;
import creativitysol.com.mawared.update.name.NameFragment;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment implements SettingsAdapter.SeetingsListener {

    Settings settingsModel;
    List<Settings> settingsList;
    RecyclerView rv_settings;
    TextView tv_collectPoints,tv_profileName,tv_userPhone,tv_userMail;
    SettingsAdapter settingsAdapter;
    NotificationViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(NotificationViewModel.class);




        tv_collectPoints = view.findViewById(R.id.tv_collectPoints);
        tv_collectPoints.setPaintFlags(tv_collectPoints.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rv_settings = view.findViewById(R.id.rv_settings);
        tv_userPhone = view.findViewById(R.id.tv_userPhone);
        tv_userMail = view.findViewById(R.id.tv_userMail);
        tv_profileName = view.findViewById(R.id.tv_profileName);
        settingsList = new ArrayList<>();
        settingsModel = new Settings(1,R.drawable.bell,"التنبيهات","0");
        settingsList.add(settingsModel);
        settingsModel = new Settings(2,R.drawable.phonecall,"تواصل معنا","3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(3,R.drawable.share2,"شارك التطبيق","3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(4,R.drawable.star,"قيم التطبيق","3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(5,R.drawable.combinedshape,"نبذة عن مياة موارد","3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(6,R.drawable.logout,"تسجيل خروج","3");
        settingsList.add(settingsModel);
        settingsAdapter = new SettingsAdapter(settingsList,getActivity(),this);

        LoginResponse loginResponse = Paper.book().read("login",null);

        if (loginResponse!=null){
            tv_profileName.setText(loginResponse.getUser().getName());
            tv_userPhone.setText(loginResponse.getUser().getMobile());
            tv_userMail.setText(loginResponse.getUser().getEmail());
        }


        tv_userMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new EmailFragment());
            }
        });

        tv_profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new NameFragment());
            }
        });


        ((MainActivity)getActivity()).showDialog(true);
        viewModel.getAllNotification(1).observe(getViewLifecycleOwner(), new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                ((MainActivity)getActivity()).showDialog(false);
                if (notification!=null){
                    if (notification.getSuccess()){
                        if (notification.getNotifications_messages()!=null){
                            settingsList.get(0).setNotificationCount(notification.getNotifications_messages().size()+"");
                            settingsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });



        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_settings.setLayoutManager(gridLayoutManager);
        rv_settings.setAdapter(settingsAdapter);


        tv_collectPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsBottomSheet termsBottomSheet = new TermsBottomSheet();

                termsBottomSheet.show(getActivity().getSupportFragmentManager(),"tag");

            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onSettingsClick(Settings settings) {
        if (settings.itemId==5)
            ((MainActivity)getActivity()).fragmentStack.push(new AboutMawaredFragment());
        else if (settings.itemId==6)
            logout();
    }

    void logout(){
        Paper.book().delete("token");
        Paper.book().delete("login");
        Paper.book().delete("cid");

        getActivity().finishAffinity();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}