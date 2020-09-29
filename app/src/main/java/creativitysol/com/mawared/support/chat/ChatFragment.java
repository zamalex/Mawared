package creativitysol.com.mawared.support.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import creativitysol.com.mawared.OrderDoneFragment;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.support.chat.model.SendMsgModel;
import creativitysol.com.mawared.support.chat.model.received.Message;
import creativitysol.com.mawared.support.chat.model.received.ReceivedChat;
import io.paperdb.Paper;


public class ChatFragment extends Fragment {


    View v;
    ChatViewModel viewModel;
    RecyclerView chat_rv;
    ImageView snd_btn;

    ChatAdapter chatAdapter;
    EditText msg_et;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v  =  inflater.inflate(R.layout.fragment_chat, container, false);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChatViewModel.class);


        chat_rv = v.findViewById(R.id.chat_rv);
        msg_et = v.findViewById(R.id.msg_et);
        snd_btn = v.findViewById(R.id.snd_msg);

        chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        chatAdapter = new ChatAdapter();

        chat_rv.setAdapter(chatAdapter);


        viewModel.receiveChat("3","16224","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNjAxMjgxMTMzLCJleHAiOjE2MDE4ODU5MzMsIm5iZiI6MTYwMTI4MTEzMywianRpIjoiZ1VIdlZvYkhBZVZFRmVhZyJ9.10M19cugUpQjqwqMbFmdXFYAkxeV92WluqJSYFgqH_w");


        viewModel.receivedChatMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ReceivedChat>() {
            @Override
            public void onChanged(ReceivedChat receivedChat) {
                if (receivedChat!=null){
                    if (receivedChat.getSuccess()){
                        chatAdapter.setMessages((ArrayList<Message>) receivedChat.getData().getMessages());
                    }
                }
            }
        });

        snd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg_et.getText().toString().isEmpty()){
                    return;
                }
                viewModel.sendNsg(msg_et.getText().toString(),"3","16224","16224","Bearer "+ Paper.book().read("token","none"));

                viewModel.sendMsgModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<SendMsgModel>() {
                    @Override
                    public void onChanged(SendMsgModel sendMsgModel) {
                        if (sendMsgModel!=null){
                            if (sendMsgModel.getSuccess()){

                            }
                        }
                    }
                });
            }
        });

        return v;
    }
}