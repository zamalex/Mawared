package creativitysol.com.mawared.mycart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.sendorder.SendOrdersFragment;
import io.paperdb.Paper;


public class MyCartFragment extends Fragment implements MyCartAdapter.sumListener {


    RecyclerView cartRv;
    View v;

    TextView total_sum;
    MyCartAdapter adapter;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_cart, container, false);


        adapter = new MyCartAdapter(this);

        cartRv = v.findViewById(R.id.cart_rv);
        total_sum = v.findViewById(R.id.total_sum);
        next = v.findViewById(R.id.btn_next);

        cartRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartRv.setAdapter(adapter);


        ArrayList<Product> arrayList = Paper.book().read("cart", new ArrayList<Product>());


        for (Iterator<Product> it = arrayList.iterator(); it.hasNext(); ) {
            Product aDrugStrength = it.next();
            if (aDrugStrength.qty == 0) {
                it.remove();
            }
        }


        Paper.book().write("cart", arrayList);

        adapter.setProducts(arrayList);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new SendOrdersFragment());
            }
        });

        return v;
    }

    @Override
    public void doSum(Long sum) {
        total_sum.setText(sum+" ر.س");
    }
}