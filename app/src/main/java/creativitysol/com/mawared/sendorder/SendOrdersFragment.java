package creativitysol.com.mawared.sendorder;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import creativitysol.com.mawared.R;


public class SendOrdersFragment extends Fragment {


    View v;

    Dialog addCoponDialog,timeDialog;

    ConstraintLayout add_copon,add_time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_send_orders, container, false);

        addCoponDialog = new Dialog(getActivity());
        timeDialog = new Dialog(getActivity());

        addCoponDialog.setContentView(R.layout.copon_dialog);
        timeDialog.setContentView(R.layout.time_dialog);


        Window window1 = addCoponDialog.getWindow();
        Window window2 = timeDialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addCoponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add_copon = v.findViewById(R.id.add_copon);
        add_time = v.findViewById(R.id.add_time);


        add_copon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCoponDialog.show();
            }
        });

        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.show();
            }
        });
        return v;
    }
}