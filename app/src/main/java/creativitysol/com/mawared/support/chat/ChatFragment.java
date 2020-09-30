package creativitysol.com.mawared.support.chat;

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
import android.widget.EditText;
import android.widget.ImageView;

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

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.OrderDoneFragment;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.support.chat.model.SendMsgModel;
import creativitysol.com.mawared.support.chat.model.received.Message;
import creativitysol.com.mawared.support.chat.model.received.ReceivedChat;
import creativitysol.com.mawared.support.chatlist.model.Chat;
import io.paperdb.Paper;


public class ChatFragment extends Fragment {


    View v;
    ChatViewModel viewModel;
    RecyclerView chat_rv;
    ImageView snd_btn;
    ImageView bck;

    ChatAdapter chatAdapter;
    EditText msg_et;
    Chat chat = null;

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

        chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getArguments() != null)
            if (getArguments().getSerializable("chat") != null)
                chat = (Chat) getArguments().getSerializable("chat");
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
                        chatAdapter.setMessages((ArrayList<Message>) receivedChat.getData().getMessages());
                        connectPusher(receivedChat.getData().getChannelId());

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

                            chatAdapter.messages.add(message);
                            chatAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }


        });
    }
}