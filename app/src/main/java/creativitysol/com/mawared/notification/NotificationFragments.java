package creativitysol.com.mawared.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.notification.model.Notification;


public class NotificationFragments extends Fragment {

    RecyclerView rv_notifications;
    NotificationsAdapter notificationsAdapter;
    NotificationViewModel notificationViewModel;
    int pageNumber = 1;
    ImageView iv_btnBack;
    FragmentStack fragmentStack;


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

        ((MainActivity)getActivity()).showDialog(true);

        notificationViewModel.getAllNotification(pageNumber).observe(getActivity(), new Observer<Notification>() {
            @Override
            public void onChanged(Notification notification) {
                ((MainActivity)getActivity()).showDialog(false);

                notificationsAdapter = new NotificationsAdapter(notification.getNotifications_messages());
                rv_notifications.setAdapter(notificationsAdapter);
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