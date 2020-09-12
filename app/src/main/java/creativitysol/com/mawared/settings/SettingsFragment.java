package creativitysol.com.mawared.settings;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import creativitysol.com.mawared.login.model.LoginResponse;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment {

    Settings settingsModel;
    List<Settings> settingsList;
    RecyclerView rv_settings;
    TextView tv_collectPoints,tv_profileName,tv_userPhone;
    SettingsAdapter settingsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tv_collectPoints = view.findViewById(R.id.tv_collectPoints);
        tv_collectPoints.setPaintFlags(tv_collectPoints.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rv_settings = view.findViewById(R.id.rv_settings);
        tv_userPhone = view.findViewById(R.id.tv_userPhone);
        tv_profileName = view.findViewById(R.id.tv_profileName);
        settingsList = new ArrayList<>();
        settingsModel = new Settings(1,R.drawable.bell,"التنبيهات","3");
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
        settingsAdapter = new SettingsAdapter(settingsList,getActivity());

        LoginResponse loginResponse = Paper.book().read("login",null);

        if (loginResponse!=null){
            tv_profileName.setText(loginResponse.getUser().getName());
            tv_userPhone.setText(loginResponse.getUser().getMobile());
        }


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_settings.setLayoutManager(gridLayoutManager);
        rv_settings.setAdapter(settingsAdapter);




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
}