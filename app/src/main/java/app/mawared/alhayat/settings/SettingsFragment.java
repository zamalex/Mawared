package app.mawared.alhayat.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;
import java.util.List;


import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.about.AboutMawaredFragment;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.notification.NotificationViewModel;
import app.mawared.alhayat.notification.model.Notification;
import app.mawared.alhayat.registeration.terms.TermsBottomSheet;
import app.mawared.alhayat.sendorder.SendOrderViewModel;
import app.mawared.alhayat.sendorder.model.points.PointsModel;
import app.mawared.alhayat.update.email.EmailFragment;
import app.mawared.alhayat.update.mobile.MobileFragment;
import app.mawared.alhayat.update.name.NameFragment;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment implements SettingsAdapter.SeetingsListener {

    Settings settingsModel;
    List<Settings> settingsList;
    RecyclerView rv_settings;
    TextView tv_collectPoints, tv_profileName, tv_userPhone, tv_userMail, tv_profilePoints;
    SettingsAdapter settingsAdapter;
    NotificationViewModel viewModel;
    SendOrderViewModel sendOrderViewModel;
    SharedPreferences pref;
    ReviewManager manager;
    ReviewInfo reviewInfo = null;
    String token = Paper.book().read("token", "none");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager = ReviewManagerFactory.create(getActivity());
        manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                }
            }
        });


        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(NotificationViewModel.class);
        sendOrderViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        tv_collectPoints = view.findViewById(R.id.tv_collectPoints);
        tv_profilePoints = view.findViewById(R.id.tv_profilePoints);
        tv_collectPoints.setPaintFlags(tv_collectPoints.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rv_settings = view.findViewById(R.id.rv_settings);
        tv_userPhone = view.findViewById(R.id.tv_userPhone);
        tv_userMail = view.findViewById(R.id.tv_userMail);
        tv_profileName = view.findViewById(R.id.tv_profileName);
        settingsList = new ArrayList<>();
        settingsModel = new Settings(1, R.drawable.bell, "التنبيهات", "0");
        settingsList.add(settingsModel);
        settingsModel = new Settings(2, R.drawable.phonecall, "تواصل معنا", "3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(3, R.drawable.share2, "شارك التطبيق", "3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(4, R.drawable.star, "قيم التطبيق", "3");
        settingsList.add(settingsModel);
        settingsModel = new Settings(5, R.drawable.combinedshape, "نبذة عن مياه موارد", "3");
        settingsList.add(settingsModel);
        if (token.equals("none")) {
            settingsModel = new Settings(10, R.drawable.logout, "تسجيل دخول", "3");

        } else settingsModel = new Settings(6, R.drawable.logout, "تسجيل خروج", "3");

        settingsList.add(settingsModel);
        settingsAdapter = new SettingsAdapter(settingsList, getActivity(), this,getActivity());

        VerifyLoginResponse loginResponse = Paper.book().read("login", null);

        if (loginResponse != null) {
            tv_profileName.setText(loginResponse.getUser().getName());
            tv_userPhone.setText(loginResponse.getUser().getPhone());
            tv_userMail.setText(loginResponse.getUser().getEmail());
            sendOrderViewModel.getPoints("Bearer " + loginResponse.getAccessToken());
        }


        tv_userMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentStack.push(new EmailFragment());
            }
        });

        tv_profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentStack.push(new NameFragment());
            }
        });

        tv_userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentStack.push(new MobileFragment());
            }
        });


        sendOrderViewModel.points.observe(getViewLifecycleOwner(), new Observer<PointsModel>() {
            @Override
            public void onChanged(PointsModel pointsModel) {
                if (pointsModel != null) {
                    if (pointsModel.getSuccess()) {
                        if (pointsModel.getData().getExpireDate() > 0) {
                            tv_profilePoints.setText(pointsModel.getData().getTotalPoints() + " نقطة ");
                        }
                    }
                }
            }
        });

        ((MainActivity) getActivity()).showDialog(true);
        viewModel.getAllNotification(1).observe(getViewLifecycleOwner(), new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                ((MainActivity) getActivity()).showDialog(false);
                if (notification != null) {
                    if (notification.getStatus() == 401) {
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    if (notification.getSuccess()) {
                        if (notification.getNotifications_messages() != null) {
                            settingsList.get(0).setNotificationCount(notification.getNotifications_messages().size() + "");
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

                termsBottomSheet.show(getActivity().getSupportFragmentManager(), "tag");

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

    String appPackageName = "app.mawared.alhayat"; // getPackageName() from Context or Activity object

    @Override
    public void onSettingsClick(Settings settings) {
        if (settings.itemId == 5)
            ((MainActivity) getActivity()).fragmentStack.push(new AboutMawaredFragment());
        else if (settings.itemId == 6)
            logout();

        else if (settings.itemId == 3) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (settings.itemId == 4) {
            if (reviewInfo != null) {
                manager.launchReviewFlow(getActivity(), reviewInfo).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.e("TAG", "onSuccess: ");

                    }
                });
            }
        }
        else if (settings.itemId==10){
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }

    }

    void logout() {
        Paper.book().delete("token");
        Paper.book().delete("login");
        Paper.book().delete("cid");

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("ccc");

        editor.apply();


        getActivity().finishAffinity();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}