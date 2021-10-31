package app.mawared.alhayat.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.home.ProductDetailsFragment;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.notification.model.Notification;
import app.mawared.alhayat.notification.newmodel.ListItem;
import app.mawared.alhayat.notification.newmodel.NewNotifications;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragments extends Fragment {

    RecyclerView rv_notifications;
    NotificationsAdapter notificationsAdapter;
    NotificationViewModel notificationViewModel;
    int pageNumber = 1;
    ImageView iv_btnBack;
    FragmentStack fragmentStack;

    SwitchMaterial enable;

    Context context;

    boolean enabled = Paper.book().read("enabled",true);


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification_fragments, container, false);
        rv_notifications = view.findViewById(R.id.rv_notifications);
        iv_btnBack = view.findViewById(R.id.iv_btnBack);
        enable = view.findViewById(R.id.enable);


        enable.setChecked(enabled);

        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controlNotifications(isChecked);
            }
        });
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_notifications.setLayoutManager(gridLayoutManager);

        ((MainActivity)context).showDialog(true);

        notificationViewModel.getAllNotification(pageNumber).observe(getActivity(), new Observer<NewNotifications>() {
            @Override
            public void onChanged(NewNotifications notification) {
                ((MainActivity)context).showDialog(false);

                if (notification!=null){
                   /* if (notification.getStatus()==401){
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;

                    }*/
                    notificationsAdapter = new NotificationsAdapter(notification.getData().getList(),NotificationFragments.this);
                    rv_notifications.setAdapter(notificationsAdapter);
                }

            }
        });

        iv_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentStack = new FragmentStack(getActivity(),getActivity().getSupportFragmentManager(),R.id.main_container);
                fragmentStack.back();
            }
        });

        return view;
    }

    void goToProduct(ListItem notification){
        if (notification.getOptions()!=null){
            if (notification.getOptions().getType()!=null){
                if (notification.getOptions().getType().equals("product")){
                    Bundle bundle = new Bundle();
                    bundle.putString("product",notification.getOptions().getId()+"");
                    bundle.putString("city","1");
                    ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                    productDetailsFragment.setArguments(bundle);

                    ((MainActivity)context).fragmentStack.push(productDetailsFragment);

                }
            }
        }
    }

    void controlNotifications(boolean b){
        int en = 0;
        String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        if (b){
            en=1;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("enabled",en);
        if (UUID==null)
            UUID = "";

        jsonObject.addProperty("player_id", UUID);

        RetrofitClient.getApiInterface().controlNotification(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                enabled =!enabled;
                Paper.book().write("enabled",enabled);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                enable.setChecked(enabled);
            }
        });
    }
}