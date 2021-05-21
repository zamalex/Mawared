package app.mawared.alhayat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

import app.mawared.alhayat.sendorder.AddressAdapter;
import app.mawared.alhayat.sendorder.BankAdapter;
import app.mawared.alhayat.sendorder.SentOrdersAdapter;

public class OrderDoneFragment extends Fragment {

    View v;
    Button go_orders;
    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activity.showDialog(false);
        if (getActivity()!=null){
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            Bundle bb = new Bundle();
            bb.putString("screen", "order placed screen Android");
            mFirebaseAnalytics.logEvent("user_location", bb);

            Map<String,Object> eventValues = new HashMap<>();
            eventValues.put(AFInAppEventParameterName.DESCRIPTION, "order_placed_screen_Android");

            AppsFlyerLib.getInstance().logEvent(getActivity(), "order_placed",eventValues);


        }



        v = inflater.inflate(R.layout.fragment_order_done, container, false);
        go_orders = v.findViewById(R.id.go_orders);




        go_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();

                ((MainActivity)getActivity()).setPaymentSuccess(false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
               //intent.putExtra("order","rate");

               startActivity(intent);


            }
        });

        return v;
    }
}