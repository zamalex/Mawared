package creativitysol.com.mawared.sendorder;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import creativitysol.com.mawared.R;


public class SendOrdersFragment extends Fragment implements OnMapReadyCallback {


    View v;

    Dialog addCoponDialog,timeDialog;

    RecyclerView address_rv,products_rv;

    BottomSheetDialog address_dialog,map_dailog,payDialog,ordersDialog;

    AddressAdapter adapter;
    SentOrdersAdapter ordersAdapter;

    MapView mapView;
    GoogleMap map;

    ImageView show_map,dismiss_map;

    ConstraintLayout add_copon,add_time,add_address,add_payment,show_products;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_send_orders, container, false);

        addCoponDialog = new Dialog(getActivity());
        timeDialog = new Dialog(getActivity());

        adapter = new AddressAdapter();
        ordersAdapter = new SentOrdersAdapter();

        addCoponDialog.setContentView(R.layout.copon_dialog);
        timeDialog.setContentView(R.layout.time_dialog);
        address_dialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
        map_dailog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
        payDialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
        ordersDialog = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);

        payDialog.setContentView(R.layout.payment_dialog);
        map_dailog.setContentView(R.layout.map_dialog);
        map_dailog.setCancelable(false);

        address_dialog.setContentView(R.layout.address_dialog);
        ordersDialog.setContentView(R.layout.products_dialog);

        address_rv = address_dialog.findViewById(R.id.address_rv);

        products_rv = ordersDialog.findViewById(R.id.products_rv);

        products_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        products_rv.setAdapter(ordersAdapter);

        show_map = address_dialog.findViewById(R.id.show_map);
        address_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        address_rv.setAdapter(adapter);

        mapView = map_dailog.findViewById(R.id.mapview);
        dismiss_map = map_dailog.findViewById(R.id.dismiss_map);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);


        setAlertDialogs();

        addCoponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add_copon = v.findViewById(R.id.add_copon);
        add_time = v.findViewById(R.id.add_time);
        add_address = v.findViewById(R.id.add_address);
        add_payment = v.findViewById(R.id.add_payment);
        show_products = v.findViewById(R.id.show_products);


        show_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersDialog.show();
            }
        });

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

        add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.show();
            }
        });

        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_dailog.show();
            }
        });

        dismiss_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_dailog.dismiss();
            }
        });

       setDialogsFullScreen();


        return v;
    }

    private void setAlertDialogs() {
        Window window1 = addCoponDialog.getWindow();
        Window window2 = timeDialog.getWindow();
        Window window3 = payDialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setDialogsFullScreen() {
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

        ordersDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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

        payDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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

        map_dailog.setOnShowListener(new DialogInterface.OnShowListener() {
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
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
//        map.setMyLocationEnabled(true);


        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 15);
        map.animateCamera(cameraUpdate);
      //  map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}