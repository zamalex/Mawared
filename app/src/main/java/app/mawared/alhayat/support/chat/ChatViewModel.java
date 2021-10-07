package app.mawared.alhayat.support.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.support.chat.model.SendMsgModel;
import app.mawared.alhayat.support.chat.model.received.ReceivedChat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
   public MutableLiveData<SendMsgModel> sendMsgModelMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ReceivedChat> receivedChatMutableLiveData = new MutableLiveData<>();

   public void sendNsg(JsonObject body, String token){
        RetrofitClient.getApiInterface().sendMessage(body,token).enqueue(new Callback<SendMsgModel>() {
            @Override
            public void onResponse(Call<SendMsgModel> call, Response<SendMsgModel> response) {
                if (response.isSuccessful()){
                    sendMsgModelMutableLiveData.setValue(response.body());

                }else {
                    sendMsgModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SendMsgModel> call, Throwable t) {
                sendMsgModelMutableLiveData.setValue(null);

            }
        });
    }


    void receiveChat(String conversation ,String order ,String token){
        RetrofitClient.getApiInterface().receivedChat(conversation,order,token).enqueue(new Callback<ReceivedChat>() {
            @Override
            public void onResponse(Call<ReceivedChat> call, Response<ReceivedChat> response) {
                if (response.isSuccessful()){
                    receivedChatMutableLiveData.setValue(response.body());

                }else {
                    receivedChatMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ReceivedChat> call, Throwable t) {
                receivedChatMutableLiveData.setValue(null);
                System.out.println("error is "+t.getLocalizedMessage());
            }
        });
    }


}
