package creativitysol.com.mawared.mycart;

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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.home.model.addmodel.AddCardModel;
import creativitysol.com.mawared.mycart.model.CardModel;
import creativitysol.com.mawared.mycart.model.Item;
import creativitysol.com.mawared.sendorder.SendOrdersFragment;
import io.paperdb.Paper;
import okhttp3.ResponseBody;


public class MyCartFragment extends Fragment implements MyCartAdapter.sumListener,MyCartAdapter.UpdateListener{


    RecyclerView cartRv;
    View v;
    CartViewModel viewModel;
    TextView total_sum;
    MyCartAdapter adapter;
    CircularProgressButton next;
    long cid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_cart, container, false);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);

        adapter = new MyCartAdapter(this,this);

        cartRv = v.findViewById(R.id.cart_rv);
        total_sum = v.findViewById(R.id.total_sum);
        next = v.findViewById(R.id.btn_next);

        cartRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartRv.setAdapter(adapter);


        cid = Paper.book().read("cid");
       // ((MainActivity)getActivity()).showDialog(true);
        next.startAnimation();
        viewModel.getCard(cid+"");




        viewModel.cardModelMutableLiveData.observe(getActivity(), new Observer<CardModel>() {
            @Override
            public void onChanged(CardModel cardModel) {
              //  ((MainActivity)getActivity()).showDialog(false);
                next.revertAnimation();


                adapter.setProducts((ArrayList<Item>) cardModel.getData().getItems());

            }
        });

        viewModel.addResponse.observe(getActivity(), new Observer<AddCardModel>() {
            @Override
            public void onChanged(AddCardModel responseBody) {
                viewModel.getCard(cid+"");

            }
        });

        viewModel.removeResponse.observe(getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                viewModel.getCard(cid+"");
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showDialog(true);

                ((MainActivity)getActivity()).fragmentStack.push(new SendOrdersFragment());
            }
        });



        return v;
    }

    @Override
    public void doSum(Double sum) {
        total_sum.setText(sum+" ر.س");
    }

    @Override
    public void increase(Item item, int qty) {
        next.startAnimation();
        viewModel.addToCard(item.getProductId()+"","1",null,cid+"","plus");

    }

    @Override
    public void decrease(Item item, int qty) {
        next.startAnimation();
        if (qty==0)
            viewModel.removeFromCard(item.getId().toString());
        else
             viewModel.addToCard(item.getProductId()+"","1",null,cid+"","minus");

    }
}