package app.mawared.alhayat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import app.mawared.alhayat.sendorder.AddressAdapter;
import app.mawared.alhayat.sendorder.BankAdapter;
import app.mawared.alhayat.sendorder.SentOrdersAdapter;

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

                ((MainActivity)getActivity()).setPaymentSuccess(false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
               intent.putExtra("order","rate");

               startActivity(intent);


            }
        });

        return v;
    }
}