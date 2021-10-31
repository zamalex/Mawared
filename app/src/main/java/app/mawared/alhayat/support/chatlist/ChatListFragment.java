package app.mawared.alhayat.support.chatlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.home.HomeViewModel;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.support.SupportFragment;
import app.mawared.alhayat.support.chat.ChatFragment;
import app.mawared.alhayat.support.chatlist.model.Chat;
import app.mawared.alhayat.support.chatlist.model.ChatList;
import io.paperdb.Paper;


public class ChatListFragment extends Fragment implements ChatListAdapter.ChatListener {

    View v;
    RecyclerView chatsRecycler;
    ImageView go_support;
    TextView msg_txt;
    ChatListViewModel viewModel;
    ChatListAdapter adapter;
    HomeViewModel homeViewModel;
    String previous_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat_list, container, false);

        if (getActivity()!=null){
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            Bundle bb = new Bundle();
            bb.putString("screen", "Chat screen Android");
            mFirebaseAnalytics.logEvent("user_location", bb);
        }

        Map<String,Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.DESCRIPTION, "Chat screen Android");

        AppsFlyerLib.getInstance().logEvent(getActivity(), "chat_opened",eventValues);


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChatListViewModel.class);
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);

        adapter = new ChatListAdapter(this);

        if (getArguments()!=null)
            previous_id = getArguments().getString("id",null);

        chatsRecycler = v.findViewById(R.id.chat_list_rv);
        go_support = v.findViewById(R.id.go_support);
        msg_txt = v.findViewById(R.id.msg_txt);


        chatsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsRecycler.setAdapter(adapter);

        ((MainActivity) getActivity()).showDialog(true);
        viewModel.getChats("Bearer " + Paper.book().read("token", "none"));
        viewModel.chatListMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ChatList>() {
            @Override
            public void onChanged(ChatList chatList) {
                ((MainActivity) getActivity()).showDialog(false);

                if (chatList != null) {
                   /* if (chatList.getStatus() == 401) {
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;

                    }*/
                    if (chatList.getSuccess()) {
                        adapter.setMessages((ArrayList<Chat>) chatList.getData());
                        if (chatList.getData().size() > 0) {
                            msg_txt.setVisibility(View.INVISIBLE);

                            if (getArguments() != null) {
                                if (getArguments().getString("conversation") != null) {
                                    for (Chat chat : chatList.getData()) {
                                        if (chat.getId().toString().equals(getArguments().getString("conversation"))) {
                                            onChatClick(chat);
                                            getArguments().remove("conversation");
                                            break;
                                        }
                                    }
                                }
                            }

                         if (previous_id!=null){
                             for (int i=0;i<chatList.getData().size();i++){

                                 if (chatList.getData().get(i).getOrderId().toString().equals(previous_id)){
                                     onChatClick(chatList.getData().get(i));

                                     if (getArguments() != null) {
                                         if (getArguments().getString("id") != null) {
                                                     getArguments().remove("id");
                                         }
                                     }
                                     break;
                                 }
                             }
                         }

                        } else {
                            msg_txt.setVisibility(View.VISIBLE);
                        }

                    } else {
                        msg_txt.setVisibility(View.VISIBLE);
                    }
                } else
                    msg_txt.setVisibility(View.VISIBLE);
            }
        });


        go_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentStack.push(new SupportFragment());
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).bottomNavVisibility(true);

        String token = Paper.book().read("token", "none");

        if (!token.equals("none")) {
            homeViewModel.getNotifyCount("Bearer " + token);
            homeViewModel.getOrdersCount("Bearer " + token);

        }

        homeViewModel.notifyCount.observe(this, new Observer<NotifyCountModel>() {
            @Override
            public void onChanged(NotifyCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess()) {
                        if (notifyCountModel.getCount() > 0 && getActivity() != null)
                            ((MainActivity) getActivity()).navigationView.getOrCreateBadge(R.id.support).setNumber(Integer.parseInt(notifyCountModel.getCount()+""));
                        else {
                            if (getActivity() != null)
                                ((MainActivity) getActivity()).navigationView.removeBadge(R.id.support);
                        }
                    }
                }
            }
        });

        homeViewModel.ordersCount.observe(this, new Observer<OrdersCountModel>() {
            @Override
            public void onChanged(OrdersCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess()) {
                        if (notifyCountModel.getData().getHasNewUpdates() && getActivity() != null)
                            ((MainActivity) getActivity()).navigationView.getOrCreateBadge(R.id.orders).setNumber(notifyCountModel.getData().getCount());
                        else {
                            if (getActivity() != null)
                                ((MainActivity) getActivity()).navigationView.removeBadge(R.id.orders);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).bottomNavVisibility(false);
    }

    @Override
    public void onChatClick(Chat chat) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle b = new Bundle();

        b.putSerializable("chat", chat);
        chatFragment.setArguments(b);
        ((MainActivity) getActivity()).fragmentStack.push(chatFragment);
    }
}