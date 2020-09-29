package creativitysol.com.mawared.support.chatlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.support.SupportFragment;
import creativitysol.com.mawared.support.chatlist.model.Chat;
import creativitysol.com.mawared.support.chatlist.model.ChatList;
import io.paperdb.Paper;


public class ChatListFragment extends Fragment {

    View v;
    RecyclerView chatsRecycler;
    ImageView go_support;
    ChatListViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_chat_list, container, false);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChatListViewModel.class);


        chatsRecycler = v.findViewById(R.id.chat_list_rv);
        go_support = v.findViewById(R.id.go_support);


        viewModel.getChats("Bearer "+Paper.book().read("token","none"));
        viewModel.chatListMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ChatList>() {
            @Override
            public void onChanged(ChatList chatList) {

            }
        });


        go_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new SupportFragment());
            }
        });

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).bottomNavVisibility(false);
    }
}