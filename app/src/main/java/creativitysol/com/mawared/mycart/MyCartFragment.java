package creativitysol.com.mawared.mycart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import creativitysol.com.mawared.R;


public class MyCartFragment extends Fragment {



    RecyclerView cartRv;
    View v;

    MyCartAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_my_cart, container, false);;

        adapter = new MyCartAdapter();

        cartRv = v.findViewById(R.id.cart_rv);

        cartRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartRv.setAdapter(adapter);


        return v;
    }
}