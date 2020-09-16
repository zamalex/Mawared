package creativitysol.com.mawared.sendorder;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.OrderDoneFragment;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.api.RetrofitClient;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.mycart.model.Product;
import creativitysol.com.mawared.registeration.terms.TermsBottomSheet;
import creativitysol.com.mawared.sendorder.model.AddressModel;
import creativitysol.com.mawared.sendorder.model.Bank;
import creativitysol.com.mawared.sendorder.model.BanksModel;
import creativitysol.com.mawared.sendorder.model.OrderShippingAddress;
import creativitysol.com.mawared.sendorder.model.Time;
import creativitysol.com.mawared.sendorder.model.TimesModel;
import creativitysol.com.mawared.sendorder.model.copon.CoponModel;
import creativitysol.com.mawared.sendorder.model.points.PointsModel;
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


public class SendOrdersFragment extends Fragment implements OnMapReadyCallback, AddressAdapter.AddressInterface, BankAdapter.BankInterface {


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

    Button snd_order, p_bank, p_deliver, p_visa, add_copon_btn;

    ImageView show_map, dismiss_map;

    TextView orders_total_dialog_txt, terms_txt, bank_details, final_total_txt;
    TextView selected_address, selected_address_type, selected_payment, selected_copon, selected_date;
    EditText copon_et;
    String token = "";
    Spinner time_spinner, map_spinner;
    Button day_spinner, btn_add_loc, confirm_time;
    ProgressBar mapprogressBar;
    ConstraintLayout add_copon, add_time, add_address, add_payment, show_products;

    MutableLiveData<String> paymentMethod = new MutableLiveData<>();

    ArrayList<Product> items = new ArrayList<>();

    ImageView back, set_loc;
    Map<String, RequestBody> requestBodyMap = new HashMap<>();

    Location selectedLocation = null;

    String[] types = new String[]{"شخصي", "مسجد", "مستلم اخر"};
    String selectedShippingType = null;
    String convertedAddress = null;
    ArrayList<String> times;

    Double total = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_send_orders, container, false);

        back = v.findViewById(R.id.imageView);
        final_total_txt = v.findViewById(R.id.final_total_txt);
        terms_txt = v.findViewById(R.id.terms_txt);

        selected_address = v.findViewById(R.id.selected_address);
        selected_address_type = v.findViewById(R.id.selected_address_type);
        selected_copon = v.findViewById(R.id.selected_copon);
        selected_date = v.findViewById(R.id.selected_date);
        selected_payment = v.findViewById(R.id.selected_payment);


        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);
        token = Paper.book().read("token", "");

        if (!token.isEmpty()) {
            viewModel.getAddresses("Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI3LCJpc3MiOiJodHRwOi8vbWF3YXJlZC5iYWRlZS5jb20uc2EvYXBpL3YxL2xvZ2luIiwiaWF0IjoxNjAwMTY5OTY0LCJleHAiOjE2MDA3NzQ3NjQsIm5iZiI6MTYwMDE2OTk2NCwianRpIjoiQlZaWUNpZ3JnYWpNUjNFMyJ9.T6JidbfjPNvySuKQ4A6kMgQCejtzSyikFG3O_H_XXKw");
            viewModel.getPoints("Bearer " + token);

        }

        // Toast.makeText(getActivity(),token+ "", Toast.LENGTH_SHORT).show();

        viewModel.getBanks();
        // viewModel.getPayment();


        addCoponDialog = new Dialog(getActivity());
        timeDialog = new Dialog(getActivity());

        adapter = new AddressAdapter(this);
        ordersAdapter = new SentOrdersAdapter();
        bankAdapter = new BankAdapter(this);

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
        confirm_time = timeDialog.findViewById(R.id.confirm_time);
        bank_details = confirmDialog.findViewById(R.id.bank_details);
        p_bank = payDialog.findViewById(R.id.p_bank);
        p_deliver = payDialog.findViewById(R.id.p_deliver);
        p_visa = payDialog.findViewById(R.id.p_visa);
        set_loc = map_dailog.findViewById(R.id.set_loc);
        map_spinner = map_dailog.findViewById(R.id.map_spinner);
        btn_add_loc = map_dailog.findViewById(R.id.btn_add_loc);
        address_rv = address_dialog.findViewById(R.id.address_rv);
        banks_rv = confirmDialog.findViewById(R.id.banks_rv);
        add_copon_btn = addCoponDialog.findViewById(R.id.add_copon);
        copon_et = addCoponDialog.findViewById(R.id.copon_et);

        time_spinner = timeDialog.findViewById(R.id.time_spinner);
        products_rv = ordersDialog.findViewById(R.id.products_rv);
        orders_total_dialog_txt = ordersDialog.findViewById(R.id.orders_total);

        products_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        products_rv.setAdapter(ordersAdapter);

        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("clist");
            total = getArguments().getDouble("total");
            final_total_txt.setText(total + " ر.س ");
            ordersAdapter.setProducts(items);

            //orders_total_dialog_txt.setText((Double)(Math.round(calculateTotal(items) * 100) / 100.00)+" ر.س ");
        }


        ArrayAdapter<String> typesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        map_spinner.setAdapter(typesadapter);

        map_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedShippingType = types[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


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

        viewModel.points.observe(getViewLifecycleOwner(), new Observer<PointsModel>() {
            @Override
            public void onChanged(PointsModel pointsModel) {
                if (pointsModel != null) {
                    Toast.makeText(getActivity(), pointsModel.getData().getTotalPoints().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.addresses.observe(getActivity(), new Observer<AddressModel>() {
            @Override
            public void onChanged(AddressModel addressModel) {
                ((MainActivity) getActivity()).showDialog(false);

                if (addressModel.getStatus() == 200) {
                    adapter.setAddresses((ArrayList<OrderShippingAddress>) addressModel.getOrderShippingAddresses());
                }
            }
        });

        snd_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((MainActivity)getActivity()).fragmentStack.push(new OrderDoneFragment());


                RequestBody payment_method = RequestBody.create(MediaType.parse("text/plain"), "visa");


                requestBodyMap.put("payment_method", payment_method);

                if (!requestBodyMap.containsKey("address") || !requestBodyMap.containsKey("lng") || !requestBodyMap.containsKey("lat")) {
                    Toast.makeText(getActivity(), "ادخل عنوان التوصيل", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!requestBodyMap.containsKey("delivery_type")) {
                    Toast.makeText(getActivity(), "ادخل نوع المستلم", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!requestBodyMap.containsKey("delivery_date")) {
                    Toast.makeText(getActivity(), "ادخل تاريخ الاستلام", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!requestBodyMap.containsKey("delivery_start_time") || !requestBodyMap.containsKey("delivery_end_time")) {
                    Toast.makeText(getActivity(), "ادخل موعد الاستلام", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!requestBodyMap.containsKey("payment_method")) {
                    Toast.makeText(getActivity(), "اختر طريقة الدفع", Toast.LENGTH_SHORT).show();
                    return;
                }


                RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), "10");
                RequestBody quantity1 = RequestBody.create(MediaType.parse("text/plain"), "10");
                RequestBody id2 = RequestBody.create(MediaType.parse("text/plain"), "12");
                RequestBody quantity2 = RequestBody.create(MediaType.parse("text/plain"), "10");

                requestBodyMap.put("products[0][id]", id1);
                requestBodyMap.put("products[0][quantity]", quantity1);

                requestBodyMap.put("products[1][id]", id2);
                requestBodyMap.put("products[1][quantity]", quantity2);


                String token = "Bearer " + Paper.book().read("token");


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


        viewModel.copon.observe(getViewLifecycleOwner(), new Observer<CoponModel>() {
            @Override
            public void onChanged(CoponModel coponModel) {

                ((MainActivity)getActivity()).showDialog(false);
                if (coponModel != null) {
                    if (coponModel.getSuccess()) {
                        Toast.makeText(getActivity(), "copon is "+coponModel.getPromocode().getAmount(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "الكود خاطئ", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getActivity(), "الكود خاطئ", Toast.LENGTH_SHORT).show();

            }
        });

        viewModel.times.observe(getActivity(), new Observer<TimesModel>() {
            @Override
            public void onChanged(TimesModel timesModel) {
                if (isAdded()) {
                    ((MainActivity) getActivity()).showDialog(false);
                    if (timesModel.getSuccess()) {
                        times = new ArrayList<>();
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

                termsBottomSheet.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });


        add_copon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copon_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل الكوبون", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity)getActivity()).showDialog(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("coupon", copon_et.getText().toString());
                jsonObject.addProperty("total", total);
                viewModel.checkCopon(jsonObject, "Bearer "+token);

                addCoponDialog.dismiss();
            }
        });


        btn_add_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedShippingType == null) {
                    Toast.makeText(getActivity(), "ادحل نوع المستلم", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedLocation == null) {
                    Toast.makeText(getActivity(), "اضف العنوان", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (convertedAddress == null) {
                    Toast.makeText(getActivity(), "اعد تحديد العنوان", Toast.LENGTH_SHORT).show();

                    return;
                }




                   /* viewModel.addAddress(loginResponse.getUser().getName(),loginResponse.getUser().getMobile()
                            ,selectedLocation.getLatitude()+"",selectedLocation.getLongitude()+"",
                           convertedAddress,selectedShippingType,"Bearer "+loginResponse.getUser().getToken());*/
                Toast.makeText(getActivity(), "selected address is " + convertedAddress, Toast.LENGTH_SHORT).show();


                selected_address.setText(convertedAddress);
                selected_address_type.setText(types[map_spinner.getSelectedItemPosition()]);

                map_dailog.dismiss();
                address_dialog.dismiss();

                RequestBody delivery_type = RequestBody.create(MediaType.parse("text/plain"), "personal");
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), convertedAddress);
                RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), selectedLocation.getLatitude() + "");
                RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), selectedLocation.getLongitude() + "");


                requestBodyMap.put("delivery_type", delivery_type);
                requestBodyMap.put("address", address);
                requestBodyMap.put("lat", lat);
                requestBodyMap.put("lng", lng);

            }
        });

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (times != null) {
                    if (times.size() > 0) {
                        String stimw = times.get(position);

                        String[] tarr = stimw.split("-");
                        RequestBody delivery_start_time = RequestBody.create(MediaType.parse("text/plain"), tarr[0].replace(" ", "") + ":00");
                        RequestBody delivery_end_time = RequestBody.create(MediaType.parse("text/plain"), tarr[1].replace(" ", "") + ":00");

                        requestBodyMap.put("delivery_start_time", delivery_start_time);
                        requestBodyMap.put("delivery_end_time", delivery_end_time);

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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

                        day_spinner.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        JsonObject jsonObject = new JsonObject();
                        String month = (monthOfYear + 1) + "";
                        String day = dayOfMonth + "";
                        if (month.length() == 1)
                            month = "0" + month;
                        if (day.length() == 1)
                            day = "0" + day;
                        ((MainActivity) getActivity()).showDialog(true);

                        RequestBody delivery_date = RequestBody.create(MediaType.parse("text/plain"), year + "-" + month + "-" + day);

                        requestBodyMap.put("delivery_date", delivery_date);


                        jsonObject.addProperty("delivery_date", year + "-" + month + "-" + day);
                        viewModel.getTimes(jsonObject, "Bearer " + Paper.book().read("token").toString());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                StartTime.show();
            }
        });

        confirm_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
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
                selectedLocation = null;
                checkLocPermission();
            }
        });

        set_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLocation = null;
                checkLocPermission();
            }
        });

        dismiss_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_dailog.dismiss();
            }
        });


        p_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    /* Double calculateTotal(ArrayList<Product>products) {
         Double sum = 0.0;

         for (Product p : products) {
             sum += (Double.parseDouble(p.getAmount()) * (p.getProduct().getPriceWithVat()));
         }

         return sum;
     }
 */
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

    @Override
    public void setAddress(String type, OrderShippingAddress address) {
        address_dialog.dismiss();
        // selected_address.setText(address);
        selectedLocation = new Location("");
        selectedLocation.setLatitude(Double.parseDouble(address.getLat()));
        selectedLocation.setLongitude(Double.parseDouble(address.getLng()));


        try {
            convertedAddress = getAddress(selectedLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("sele", address.getLat() + " + " + address.getLng());
        map_dailog.show();

        map.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude()));

        mp.title("my position");

        map.addMarker(mp);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude()), 16));
    }

    void checkLocPermission() {
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
                                            Toast.makeText(getActivity(), "loc is " + location.getLatitude(), Toast.LENGTH_LONG).show();
                                            map.clear();

                                            MarkerOptions mp = new MarkerOptions();

                                            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                                            mp.title("my position");

                                            map.addMarker(mp);

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(location.getLatitude(), location.getLongitude()), 16));


                                            selectedLocation = location;
                                            try {
                                                convertedAddress = getAddress(selectedLocation);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

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


    String getAddress(Location loc) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();


        return address;
    }

    @Override
    public void onBankSelected(Bank bank, int position) {
        bank_details.setText(bank.getName() + "\n" + " رقم الحساب " + bank.getAccountNumber() + "\n" + " ابان " + bank.getIban());

        for (int i = 0; i < viewModel.banks.getValue().getBanks().size(); i++) {
            if (viewModel.banks.getValue().getBanks().get(i).getId() == bank.getId()) {
                viewModel.banks.getValue().getBanks().get(i).is = 1;

            } else {
                viewModel.banks.getValue().getBanks().get(i).is = 0;

            }

        }
        bankAdapter.setBanks((ArrayList<Bank>) viewModel.banks.getValue().getBanks());
    }
}