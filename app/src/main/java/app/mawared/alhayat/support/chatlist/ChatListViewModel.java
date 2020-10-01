package app.mawared.alhayat.support.chatlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.support.chatlist.model.ChatList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListViewModel extends ViewModel {

    MutableLiveData<ChatList> chatListMutableLiveData = new MutableLiveData<>();

    void getChats(String token){
        RetrofitClient.getApiInterface().getChats(token).enqueue(new Callback<ChatList>() {
            @Override
            public void onResponse(Call<ChatList> call, Response<ChatList> response) {
                if (response.isSuccessful()){
                    chatListMutableLiveData.setValue(response.body());

                }else {
                    chatListMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChatList> call, Throwable t) {
                chatListMutableLiveData.setValue(null);

            }
        });
    }
}
