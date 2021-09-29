package app.mawared.alhayat.mycart;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.home.model.addmodel.AddCardModel;
import app.mawared.alhayat.mycart.model.CardModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {

   public MutableLiveData<CardModel> cardModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<AddCardModel> addResponse = new MutableLiveData<>();
    public MutableLiveData<ResponseBody> removeResponse = new MutableLiveData<>();

    public void addToCard(String product_id, String amount, String device_id, String cart_id,String math_type,String city_id,String lat,String lng) {
        RetrofitClient.getApiInterface().addToCard(product_id, amount, device_id, cart_id,math_type,city_id,lat,lng).enqueue(new Callback<AddCardModel>() {
            @Override
            public void onResponse(Call<AddCardModel> call, Response<AddCardModel> response) {
                addResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddCardModel> call, Throwable t) {
                addResponse.setValue(null);
                Log.d("carde",t.getMessage());

            }
        });
    }


    public void removeFromCard(String cid,String pid) {
        RetrofitClient.getApiInterface().removeFromCard(cid,pid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                removeResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                removeResponse.setValue(null);
            }
        });
    }

    public void getCard(String card_id) {
        RetrofitClient.getApiInterface().getCard(card_id).enqueue(new Callback<CardModel>() {
            @Override
            public void onResponse(Call<CardModel> call, Response<CardModel> response) {
                cardModelMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CardModel> call, Throwable t) {
                cardModelMutableLiveData.setValue(null);
                Log.d("carde",t.getMessage());
            }
        });
    }
}
