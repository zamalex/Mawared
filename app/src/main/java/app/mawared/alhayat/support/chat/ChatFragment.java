package app.mawared.alhayat.support.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
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
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.support.chat.model.SendMsgModel;
import app.mawared.alhayat.support.chat.model.received.Message;
import app.mawared.alhayat.support.chat.model.received.ReceivedChat;
import app.mawared.alhayat.support.chatlist.model.Chat;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment {

    BottomSheetDialog rateDialog;

    View v;
    ChatViewModel viewModel;
    RecyclerView chat_rv;
    ImageView snd_btn;
    ImageView bck;
    TextView closed,rating_title,mandob_title;
    ChatAdapter chatAdapter;
    EditText msg_et;
    Chat chat = null;
    ConstraintLayout chat_box;
    RatingBar ratingBar,shipping_bar;

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

        rateDialog = new BottomSheetDialog(getActivity());

        rateDialog.setContentView(R.layout.rate_dialog);
        ratingBar = rateDialog.findViewById(R.id.stars);
        shipping_bar = rateDialog.findViewById(R.id.shipping_stars);
        mandob_title = rateDialog.findViewById(R.id.mandob_title);
        rating_title = rateDialog.findViewById(R.id.rating_title);
        rating_title.setText("من فضلك قيم المحادثة");
        shipping_bar.setVisibility(View.GONE);
        mandob_title.setVisibility(View.GONE);

        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        rateDialog.findViewById(R.id.xrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.dismiss();
            }
        });

        rateDialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat!=null){
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("conversation_id", chat.getId());
                    jsonObject.addProperty("rating", ratingBar.getRating() +"");
                    RetrofitClient.getApiInterface().rateChat(jsonObject).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            rateDialog.dismiss();


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            rateDialog.dismiss();

                        }
                    });
                }

            }
        });



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
                        if (chat.getHide_rate()==0)
                       rateDialog.show();
                        if (receivedChat.getData().getConversationStatus().equals("1")){
                            closed.setVisibility(View.INVISIBLE);
                            chat_box.setVisibility(View.VISIBLE);

                        }else {
                           // rateDialog.show();
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
                        if (viewModel.receivedChatMutableLiveData.getValue().getData().getConversationStatus().equals("0")){
                            Toast.makeText(getActivity(), "تم اغلاق المحادثة", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                JsonObject body = new JsonObject();

                body.addProperty("message",msg_et.getText().toString());
                body.addProperty("conversation_id",chat.getId());
                if (chat.getOrderId()!=null)
                body.addProperty("order_id",chat.getOrderId()+"");
                body.addProperty("title",chat.getTitle());
                viewModel.sendNsg(body, "Bearer " + Paper.book().read("token","none"));

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