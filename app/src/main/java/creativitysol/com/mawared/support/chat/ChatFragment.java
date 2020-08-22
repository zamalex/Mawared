package creativitysol.com.mawared.support.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import creativitysol.com.mawared.R;


public class ChatFragment extends Fragment {


    View v;

    RecyclerView chat_rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v  =  inflater.inflate(R.layout.fragment_chat, container, false);

        chat_rv = v.findViewById(R.id.chat_rv);

        chat_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        chat_rv.setAdapter(new ChatAdapter());

        return v;
    }
}