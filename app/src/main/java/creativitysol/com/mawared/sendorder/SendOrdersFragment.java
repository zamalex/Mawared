package creativitysol.com.mawared.sendorder;

import android.Manifest;
import android.app.DatePickerDialog;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.OrderDoneFragment;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.mycart.model.Item;
import creativitysol.com.mawared.registeration.terms.TermsBottomSheet;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.Bank;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import creativitysol.com.mawared.sendorder.model.CustomerShippingAddress;
import creativitysol.com.mawared.sendorder.model.Time;
import creativitysol.com.mawared.sendorder.model.TimesModel;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendOrdersFragment extends Fragment implements OnMapReadyCallback, AddressAdapter.AddressInterface {


    SendOrderViewModel viewModel;

    View v;

    Dialog addCoponDialog, timeDialog;
    RecyclerView address_rv, products_rv, banks_rv;

    BottomSheetDialog address_dialog, map_dailog, payDialog, ordersDialog, confirmDialog;

    AddressAdapter adapter;
    SentOrdersAdapter ordersAdapter;
    BankAdapter bankAdapter;

    MapView mapView;
    GoogleMap map;

    Button snd_order, p_bank,p_deliver,p_visa;

    ImageView show_map, dismiss_map;

    TextView orders_total_dialog_txt,terms_txt;
    TextView selected_address,selected_address_type,selected_payment,selected_copon,selected_date;

    String token = "";
    Spinner time_spinner;
    Button day_spinner;
    ProgressBar mapprogressBar;
    ConstraintLayout add_copon, add_time, add_address, add_payment, show_products;

    MutableLiveData<String> paymentMethod = new MutableLiveData<>();

    ArrayList<Item> items = new ArrayList<>();

    ImageView back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_send_orders, container, false);

        back = v.findViewById(R.id.imageView);
        terms_txt = v.findViewById(R.id.terms_txt);

        selected_address = v.findViewById(R.id.selected_address);
        selected_address_type = v.findViewById(R.id.selected_address_type);
        selected_copon = v.findViewById(R.id.selected_copon);
        selected_date = v.findViewById(R.id.selected_date);
        selected_payment = v.findViewById(R.id.selected_payment);


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);
        token = Paper.book().read("token","");

        if (!token.isEmpty())
            viewModel.getAddresses("Bearer " + token);

       // Toast.makeText(getActivity(),token+ "", Toast.LENGTH_SHORT).show();

        viewModel.getBanks();


        addCoponDialog = new Dialog(getActivity());
        timeDialog = new Dialog(getActivity());

        adapter = new AddressAdapter(this);
        ordersAdapter = new SentOrdersAdapter();
        bankAdapter = new BankAdapter();

        addCoponDialog.setContentView(R.layout.copon_dialog);

        addCoponDialog.findViewById(R.id.xcopon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCoponDialog.dismiss();
            }
        });


        timeDialog.setContentView(R.layout.time_dialog);
        timeDialog.findViewById(R.id.xtime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.dismiss();
            }
        });
        address_dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        map_dailog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        payDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        ordersDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        confirmDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);

        payDialog.setContentView(R.layout.payment_dialog);
        payDialog.findViewById(R.id.xpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.dismiss();
            }
        });
        confirmDialog.setContentView(R.layout.confirm_dialog);
        map_dailog.setContentView(R.layout.map_dialog);
        map_dailog.setCancelable(false);

        address_dialog.setContentView(R.layout.address_dialog);
        address_dialog.findViewById(R.id.xaddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_dialog.dismiss();
            }
        });
        ordersDialog.setContentView(R.layout.products_dialog);
        ordersDialog.findViewById(R.id.xorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersDialog.dismiss();
            }
        });

        day_spinner = timeDialog.findViewById(R.id.day_spinner);

        p_bank = payDialog.findViewById(R.id.p_bank);
        p_deliver = payDialog.findViewById(R.id.p_deliver);
        p_visa = payDialog.findViewById(R.id.p_visa);

        address_rv = address_dialog.findViewById(R.id.address_rv);
        banks_rv = confirmDialog.findViewById(R.id.banks_rv);

        time_spinner = timeDialog.findViewById(R.id.time_spinner);
        products_rv = ordersDialog.findViewById(R.id.products_rv);
        orders_total_dialog_txt = ordersDialog.findViewById(R.id.orders_total);

        products_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        products_rv.setAdapter(ordersAdapter);

        if (getArguments()!=null){
            items = getArguments().getParcelableArrayList("clist");
            ordersAdapter.setProducts(items);

            orders_total_dialog_txt.setText((Double)(Math.round(calculateTotal(items) * 100) / 100.00)+" ر.س ");
        }


        banks_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        banks_rv.setAdapter(bankAdapter);

        show_map = address_dialog.findViewById(R.id.show_map);
        address_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        address_rv.setAdapter(adapter);

        mapView = map_dailog.findViewById(R.id.mapview);
        mapprogressBar = map_dailog.findViewById(R.id.mapprogressBar);
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
        snd_order = v.findViewById(R.id.snd_order);

        viewModel.banks.observe(getActivity(), new Observer<BanksModel>() {
            @Override
            public void onChanged(BanksModel banksModel) {
                ((MainActivity) getActivity()).showDialog(false);

                if (banksModel.getStatus() == 200)
                    bankAdapter.setBanks((ArrayList<Bank>) banksModel.getBanks());
            }
        });


        viewModel.addresses.observe(getActivity(), new Observer<AddressModel>() {
            @Override
            public void onChanged(AddressModel addressModel) {
                ((MainActivity) getActivity()).showDialog(false);

                if (addressModel.getStatus() == 200) {
                    adapter.setAddresses((ArrayList<CustomerShippingAddress>) addressModel.getCustomerShippingAddresses());
                }
            }
        });

        snd_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((MainActivity)getActivity()).fragmentStack.push(new OrderDoneFragment());

                Map<String, RequestBody> requestBodyMap = new HashMap<>();

                RequestBody delivery_type = RequestBody.create(MediaType.parse("text/plain"), "personal");
                RequestBody delivery_date = RequestBody.create(MediaType.parse("text/plain"), "2020-09-01");
                RequestBody delivery_start_time = RequestBody.create(MediaType.parse("text/plain"), "12:00:00");
                RequestBody delivery_end_time = RequestBody.create(MediaType.parse("text/plain"), "15:00:00");
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), "ada");
                RequestBody payment_method = RequestBody.create(MediaType.parse("text/plain"), "visa");
                RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "24.17750185783278");
                RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), "47.29694739189757");


                requestBodyMap.put("delivery_type", delivery_type);
                requestBodyMap.put("delivery_date", delivery_date);
                requestBodyMap.put("delivery_start_time", delivery_start_time);
                requestBodyMap.put("delivery_end_time", delivery_end_time);
                requestBodyMap.put("address", address);
                requestBodyMap.put("payment_method", payment_method);
                requestBodyMap.put("lat", lat);
                requestBodyMap.put("lng", lng);

                RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), "10");
                RequestBody quantity1 = RequestBody.create(MediaType.parse("text/plain"), "10");
                RequestBody id2 = RequestBody.create(MediaType.parse("text/plain"), "12");
                RequestBody quantity2 = RequestBody.create(MediaType.parse("text/plain"), "10");

                requestBodyMap.put("products[0][id]", id1);
                requestBodyMap.put("products[0][quantity]", quantity1);

                requestBodyMap.put("products[1][id]", id2);
                requestBodyMap.put("products[1][quantity]", quantity2);


                String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNTk4ODc1NjM1LCJleHAiOjE1OTk0ODA0MzUsIm5iZiI6MTU5ODg3NTYzNSwianRpIjoicGY2WER3azhuWVhPeVRHMiJ9.MVOfr3r2d_IlX4IxlThRuC847_DomNrVeT7gUBk6oB8";


                RetrofitClient.getApiInterface().sendOrder(requestBodyMap, token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("resooo", response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("resooo", t.getMessage());
                    }
                });
            }
        });



        viewModel.times.observe(getActivity(), new Observer<TimesModel>() {
            @Override
            public void onChanged(TimesModel timesModel) {
                if (isAdded()){
                    ((MainActivity) getActivity()).showDialog(false);
                    if (timesModel.getSuccess()){
                        ArrayList<String> times = new ArrayList<>();
                        for (Time t : timesModel.getTimes())
                            times.add(t.getName());
                        ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, times);
                        time_spinner.setAdapter(aarrdapter);
                    }
                }
            }
        });

        terms_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsBottomSheet termsBottomSheet = new TermsBottomSheet();

                termsBottomSheet.show(getActivity().getSupportFragmentManager(),"tag");
            }
        });

        day_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        day_spinner.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                        JsonObject jsonObject = new JsonObject();
                        String month = (monthOfYear+1)+"";
                        String day = dayOfMonth+"";
                        if (month.length()==1)
                            month = "0"+month;
                        if (day.length()==1)
                            day = "0"+day;
                        ((MainActivity) getActivity()).showDialog(true);

                        jsonObject.addProperty("delivery_date",year+"-"+month+"-"+day);
                        viewModel.getTimes(jsonObject,"Bearer "+Paper.book().read("token").toString());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                StartTime.show();
                }
            });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });
        show_products.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                ordersDialog.show();
            }
            });

        add_copon.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                addCoponDialog.show();
            }
            });

        add_time.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                timeDialog.show();
            }
            });

        add_address.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                address_dialog.show();
            }
            });

        add_payment.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                payDialog.show();
            }
            });

        show_map.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){

                    checkLocPermission();
            }
            });

        dismiss_map.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                map_dailog.dismiss();
            }
            });


        p_bank.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                    paymentMethod.setValue("تحويل بنكي");
                confirmDialog.show();
            }
            });
        p_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod.setValue("بطاقة مدى / visa");

            }
        });

        p_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod.setValue("الدفع عن الإستلام");
                payDialog.dismiss();
            }
        });

        paymentMethod.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                selected_payment.setText(s);
            }
        });
            setDialogsFullScreen();


        return v;
        }

    Double calculateTotal(ArrayList<Item>products) {
        Double sum = 0.0;

        for (Item p : products) {
            sum += (Double.parseDouble(p.getAmount()) * (p.getProduct().getPriceWithVat()));
        }

        return sum;
    }

        private void setAlertDialogs () {
            Window window1 = addCoponDialog.getWindow();
            Window window2 = timeDialog.getWindow();
            Window window3 = payDialog.getWindow();
            window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void setDialogsFullScreen () {
            address_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                    BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
                    // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
                    FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

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
                    FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

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
                    FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

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
                    FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    setupFullHeight(bottomSheet);
                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });

            confirmDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                    BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
                    // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
                    FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    setupFullHeight(bottomSheet);
                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }

        private void setupFullHeight (View bottomSheet){
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);
        }

        @Override
        public void onMapReady (GoogleMap googleMap){
            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(true);
//        map.setMyLocationEnabled(true);


            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 15);
            map.animateCamera(cameraUpdate);
            //  map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));

        }

        @Override
        public void onResume () {
            mapView.onResume();
            super.onResume();
        }


        @Override
        public void onPause () {
            super.onPause();
            mapView.onPause();
        }

        @Override
        public void onDestroy () {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory () {
            super.onLowMemory();
            mapView.onLowMemory();
        }

    @Override
    public void setAddress(String type, String address) {
        address_dialog.dismiss();
        selected_address.setText(address);
    }

    void checkLocPermission(){
        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            mapprogressBar.setVisibility(View.VISIBLE);
                            SmartLocation.with(getActivity()).location()
                                    //.oneFix()
                                    .start(new OnLocationUpdatedListener() {
                                        @Override
                                        public void onLocationUpdated(Location location) {


                                            mapprogressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "loc is "+location.getLatitude(), Toast.LENGTH_LONG).show();
                                            map.clear();

                                            MarkerOptions mp = new MarkerOptions();

                                            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                                            mp.title("my position");

                                            map.addMarker(mp);

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(location.getLatitude(), location.getLongitude()), 16));


                                        }
                                    });
                            map_dailog.show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getActivity(), "قم بالسماح للتطبيق للوصول الى موقعك من خلال الاعدادات", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();


                    }
                })
                .onSameThread()
                .check();
    }


}