package creativitysol.com.mawared.support;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.support.chat.ChatFragment;


public class SupportFragment extends Fragment {

    ArrayList<String> orders;
    View v;
    Spinner spinner;
    Button sendSupport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_support, container, false);
        spinner = v.findViewById(R.id.order_spinner);
        sendSupport = v.findViewById(R.id.sendSupport);


        orders = new ArrayList<>();
        orders.add("رقم الطلب");

        ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, orders);
        spinner.setAdapter(aarrdapter);


        sendSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new ChatFragment());
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