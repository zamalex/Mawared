package app.mawared.alhayat.support.chat;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.support.chat.model.SendMsgModel;
import app.mawared.alhayat.support.chat.model.received.Message;
import app.mawared.alhayat.support.chat.model.received.ReceivedChat;
import app.mawared.alhayat.support.chatlist.model.Chat;
import io.paperdb.Paper;


public class ChatFragment extends Fragment {


    View v;
    ChatViewModel viewModel;
    RecyclerView chat_rv;
    ImageView snd_btn;
    ImageView bck;
    TextView closed;
    ChatAdapter chatAdapter;
    EditText msg_et;
    Chat chat = null;
    ConstraintLayout chat_box;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_chat, container, false);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChatViewModel.class);


        chat_rv = v.findViewById(R.id.chat_rv);
        msg_et = v.findViewById(R.id.msg_et);
        snd_btn = v.findViewById(R.id.snd_msg);
        bck = v.findViewById(R.id.imageView);
        closed = v.findViewById(R.id.closed);
        chat_box = v.findViewById(R.id.constraintLayout12);
        closed.setVisibility(View.INVISIBLE);
        chat_box.setVisibility(View.VISIBLE);

        chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getArguments() != null){
            if (getArguments().getSerializable("chat") != null){
                chat = (Chat) getArguments().getSerializable("chat");
                connectPusher(chat.getChannelId());
            }
        }
        chatAdapter = new ChatAdapter();

        chat_rv.setAdapter(chatAdapter);


        viewModel.receiveChat(""+chat.getId(), chat.getOrderId()+"", "Bearer "+Paper.book().read("token","none"));



        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });


        viewModel.receivedChatMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ReceivedChat>() {
            @Override
            public void onChanged(ReceivedChat receivedChat) {
                if (receivedChat != null) {
                    if (receivedChat.getSuccess()) {

                        if (receivedChat.getData().getStatus().equals("1")){
                            closed.setVisibility(View.INVISIBLE);
                            chat_box.setVisibility(View.VISIBLE);

                        }else {
                            closed.setVisibility(View.VISIBLE);
                            chat_box.setVisibility(View.INVISIBLE);
                        }
                        chatAdapter.setMessages((ArrayList<Message>) receivedChat.getData().getMessages());

                        if (chatAdapter.messages.size()>0){
                            chat_rv.scrollToPosition(chatAdapter.messages.size()-1);
                        }


                    }
                }
            }
        });

        snd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg_et.getText().toString().isEmpty()) {
                    return;
                }

                if (viewModel.receivedChatMutableLiveData.getValue()!=null){
                    if (viewModel.receivedChatMutableLiveData.getValue().getSuccess()){
                        if (viewModel.receivedChatMutableLiveData.getValue().getData().getStatus().equals("0")){
                            Toast.makeText(getActivity(), "تم اغلاق المحادثة", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                viewModel.sendNsg(msg_et.getText().toString(), ""+chat.getId(), chat.getOrderId()+"", chat.getTitle(), "Bearer " + Paper.book().read("token","none"));

                msg_et.setText("");
                viewModel.sendMsgModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<SendMsgModel>() {
                    @Override
                    public void onChanged(SendMsgModel sendMsgModel) {
                        if (sendMsgModel != null) {
                            if (sendMsgModel.getSuccess()) {

                            }
                        }
                    }
                });
            }
        });

        return v;
    }

    void connectPusher(String chanel_id){
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");

        Pusher pusher = new Pusher("6fb78eff4fbbe0cd5095", options);//app key
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.e("pusher: State", " changed to " + change.getCurrentState());

            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("pusher:problem", " connecting! msg:" + message);

            }
        }, ConnectionState.ALL);


        Channel channel = pusher.subscribe(chanel_id, new ChannelEventListener() {//3e402d6eb0b608b7ada8033eae30c467d9980d67
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data: " + event.getData());

            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("Subscribed to channel: " + channelName);
            }

            // Other ChannelEventListener methods
        });


        channel.bind("talk-send-message", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {

                System.out.println("Received event with data: " + event.getData());
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            Message message = new Message();
                            JSONObject jsonObject = new JSONObject(event.getData());
                            message.setMessage(jsonObject.getString("message"));
                            message.setUserId(jsonObject.getString("user_id"));
                            message.setCreatedAt(jsonObject.getString("created_at"));

                            chatAdapter.messages.add(message);
                            chatAdapter.notifyDataSetChanged();
                            System.out.println("data is "+event.getData());

                            if (chatAdapter.messages.size()>0){
                                chat_rv.scrollToPosition(chatAdapter.messages.size()-1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }


        });
    }
}