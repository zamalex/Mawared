package app.mawared.alhayat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OrderDoneFragment extends Fragment {

    View v;
    Button go_orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_order_done, container, false);
        go_orders = v.findViewById(R.id.go_orders);


        go_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
                Intent intent = new Intent(getActivity(), MainActivity.class);
               // intent.putExtra("order","order");

               startActivity(intent);

            }
        });

        return v;
    }
}