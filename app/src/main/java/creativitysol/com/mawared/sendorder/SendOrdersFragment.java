package creativitysol.com.mawared.sendorder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import creativitysol.com.mawared.R;


public class SendOrdersFragment extends Fragment {


    View v;

    Dialog addCoponDialog,timeDialog;

    RecyclerView address_rv;

    BottomSheetDialog address_dialog;

    AddressAdapter adapter;

    ConstraintLayout add_copon,add_time,add_address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_send_orders, container, false);

        addCoponDialog = new Dialog(getActivity());
        timeDialog = new Dialog(getActivity());

        adapter = new AddressAdapter();

        addCoponDialog.setContentView(R.layout.copon_dialog);
        timeDialog.setContentView(R.layout.time_dialog);
        address_dialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);

        address_dialog.setContentView(R.layout.address_dialog);

        address_rv = address_dialog.findViewById(R.id.address_rv);

        address_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        address_rv.setAdapter(adapter);

        Window window1 = addCoponDialog.getWindow();
        Window window2 = timeDialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addCoponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add_copon = v.findViewById(R.id.add_copon);
        add_time = v.findViewById(R.id.add_time);
        add_address = v.findViewById(R.id.add_address);


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

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_dialog.show();
            }
        });

        address_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                    BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
                    // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
                    FrameLayout bottomSheet =  dialogc.findViewById(R.id.design_bottom_sheet);

                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    setupFullHeight(bottomSheet);
                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


        return v;
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }
}