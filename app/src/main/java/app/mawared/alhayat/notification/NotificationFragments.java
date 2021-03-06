package app.mawared.alhayat.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.notification.model.Notification;


public class NotificationFragments extends Fragment {

    RecyclerView rv_notifications;
    NotificationsAdapter notificationsAdapter;
    NotificationViewModel notificationViewModel;
    int pageNumber = 1;
    ImageView iv_btnBack;
    FragmentStack fragmentStack;


    Context context;

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


        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_notifications.setLayoutManager(gridLayoutManager);

        ((MainActivity)context).showDialog(true);

        notificationViewModel.getAllNotification(pageNumber).observe(getActivity(), new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                ((MainActivity)context).showDialog(false);

                if (notification!=null){
                    if (notification.getStatus()==401){
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;

                    }
                    notificationsAdapter = new NotificationsAdapter(notification.getNotifications_messages());
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
}