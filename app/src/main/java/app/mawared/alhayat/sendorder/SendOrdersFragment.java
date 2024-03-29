package app.mawared.alhayat.sendorder;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.CompoundButton;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.mawared.alhayat.AddressModel;
import app.mawared.alhayat.BlankFragment;
import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.MapsActivity;
import app.mawared.alhayat.OrderDoneFragment;
import app.mawared.alhayat.R;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.mycart.CartViewModel;
import app.mawared.alhayat.mycart.model.CardModel;
import app.mawared.alhayat.mycart.model.Product;
import app.mawared.alhayat.registeration.terms.TermsBottomSheet;
import app.mawared.alhayat.sendorder.model.Bank;
import app.mawared.alhayat.sendorder.model.BanksModel;
import app.mawared.alhayat.sendorder.model.OrderShippingAddress;
import app.mawared.alhayat.sendorder.model.PaymentMethod;
import app.mawared.alhayat.sendorder.model.PaymentModel;
import app.mawared.alhayat.sendorder.model.Time;
import app.mawared.alhayat.sendorder.model.TimesModel;
import app.mawared.alhayat.sendorder.model.copon.CoponModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.ConfirmModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.visa.VisaModel;
import app.mawared.alhayat.sendorder.model.points.PointsModel;
import app.mawared.alhayat.sendorder.newaddress.AddressNewResponse;
import app.mawared.alhayat.sendorder.newaddress.DataItem;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendOrdersFragment extends Fragment implements OnMapReadyCallback, AddressAdapter.AddressInterface, BankAdapter.BankInterface, PaymentsAdapter.paymentClickListener {


    SendOrderViewModel viewModel;
    CartViewModel cartViewModel;
    MainActivity activity;
    View v;

    Dialog addCoponDialog, timeDialog, pts_dialog;
    RecyclerView address_rv, products_rv, banks_rv;

    BottomSheetDialog address_dialog, map_dailog, payDialog, ordersDialog, confirmDialog, datePickerDialog;

    AddressAdapter adapter;
    SentOrdersAdapter ordersAdapter;
    BankAdapter bankAdapter;

    MapView mapView;
    GoogleMap map;

    Button snd_order, add_copon_btn, add_pts, confirm_transfer;

    ImageView show_map, dismiss_map;

    TextView orders_total_dialog_txt, terms_txt, bank_details, bank_acc_no, bank_iban, final_total_txt;
    TextView selected_address, selected_address_type, selected_payment, selected_copon, selected_date;
    EditText copon_et, transfer_no;
    String token = "";
    Spinner time_spinner, map_spinner, pts_spinner;
    Button day_spinner, btn_add_loc, confirm_time;
    ProgressBar mapprogressBar;
    ConstraintLayout add_copon, add_time, add_address, add_payment, show_products;

    MutableLiveData<String> paymentMethod = new MutableLiveData<>();

    ArrayList<Product> items = new ArrayList<>();

    ImageView back, set_loc;
    Map<String, RequestBody> requestBodyMap = new HashMap<>();

    Location selectedLocation = null;

    String[] types = new String[]{"شخصي", "مسجد", "مستلم اخر"};
    String[] types_e = new String[]{"personal", "mosque", "other"};
    String selectedShippingType = null;
    String convertedAddress = null;
    ArrayList<String> times;
    ArrayList<String> pts_amounts = new ArrayList<>();
    Double coponDiscount = 0d;
    MutableLiveData<Double> total = new MutableLiveData<>();
    Double ptsDiscount = 0d;
    Double total_before = 0d;
    Double vat = 0d;

    TextView semi_final_txt, vat_txt, discount_txt, pts_c_txt, count_tv, done_date,rsed;
    DatePicker datePicker;
    RecyclerView payments_rv;

    ImageView copy_account, copy_iban;

    SwitchMaterial pts_switch;

    Bank selected_bank = null;

    String date = "", time = "";

    String selected_payment_method = "";

    Double vat_without_pts = null;
    Double price_without_pts = null;
    EditText rec_phone, rec_name;

    double balance = 0;
    boolean isBalance = false;

    PaymentsAdapter paymentsAdapter;

    AddressModel addressModel = Paper.book().read("address",null);


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_send_orders, container, false);

            cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);

            back = v.findViewById(R.id.imageView);
            final_total_txt = v.findViewById(R.id.final_total_txt);
            terms_txt = v.findViewById(R.id.terms_txt);
            rsed = v.findViewById(R.id.rsed);
            pts_switch = v.findViewById(R.id.imageView42);

            selected_address = v.findViewById(R.id.selected_address);
            if (addressModel!=null)
                selected_address.setText(addressModel.getAddress());

            selected_address_type = v.findViewById(R.id.selected_address_type);
            selected_copon = v.findViewById(R.id.selected_copon);
            selected_date = v.findViewById(R.id.selected_date);
            selected_payment = v.findViewById(R.id.selected_payment);
            semi_final_txt = v.findViewById(R.id.semi_final_txt);
            vat_txt = v.findViewById(R.id.vat_txt);
            discount_txt = v.findViewById(R.id.discount_txt);
            pts_c_txt = v.findViewById(R.id.pts_c_txt);
            count_tv = v.findViewById(R.id.count_tv);


            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);
            token = Paper.book().read("token", "");

            if (!token.isEmpty()) {
                viewModel.getAddresses("Bearer " + token);
                viewModel.getPoints("Bearer " + token);

            }

            // Toast.makeText(getActivity(),token+ "", Toast.LENGTH_SHORT).show();

            viewModel.getBanks();
             viewModel.getPayment();


            addCoponDialog = new Dialog(getActivity());
            timeDialog = new Dialog(getActivity());
            pts_dialog = new Dialog(getActivity());

            adapter = new AddressAdapter(this);
            ordersAdapter = new SentOrdersAdapter();
            bankAdapter = new BankAdapter(this);
            paymentsAdapter = new PaymentsAdapter(this);

            addCoponDialog.setContentView(R.layout.copon_dialog);

            addCoponDialog.findViewById(R.id.xcopon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCoponDialog.dismiss();
                }
            });


            pts_dialog.setContentView(R.layout.pts_dialog);

            pts_dialog.setCancelable(false);

            pts_dialog.findViewById(R.id.xpts).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pts_switch.setChecked(false);
                    pts_dialog.dismiss();
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
            datePickerDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            map_dailog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            payDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            ordersDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            confirmDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);


            datePickerDialog.setContentView(R.layout.date_picker_bottom);
            done_date = datePickerDialog.findViewById(R.id.done_date);
            datePicker = datePickerDialog.findViewById(R.id.datePicker);


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

            copy_account = confirmDialog.findViewById(R.id.copy_account);
            copy_iban = confirmDialog.findViewById(R.id.copy_iban);

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
            bank_acc_no = confirmDialog.findViewById(R.id.bank_acc_no);
            bank_iban = confirmDialog.findViewById(R.id.bank_iban);

            set_loc = map_dailog.findViewById(R.id.set_loc);
            map_spinner = map_dailog.findViewById(R.id.map_spinner);
            rec_phone = map_dailog.findViewById(R.id.editText2);
            rec_name = map_dailog.findViewById(R.id.editText3);
            btn_add_loc = map_dailog.findViewById(R.id.btn_add_loc);
            address_rv = address_dialog.findViewById(R.id.address_rv);
            banks_rv = confirmDialog.findViewById(R.id.banks_rv);
            transfer_no = confirmDialog.findViewById(R.id.transfer_no);
            confirm_transfer = confirmDialog.findViewById(R.id.cnonfirm_transfer);
            add_copon_btn = addCoponDialog.findViewById(R.id.add_copon);
            copon_et = addCoponDialog.findViewById(R.id.copon_et);

            time_spinner = timeDialog.findViewById(R.id.time_spinner);
            products_rv = ordersDialog.findViewById(R.id.products_rv);
            payments_rv = payDialog.findViewById(R.id.payment_rv);
            orders_total_dialog_txt = ordersDialog.findViewById(R.id.orders_total);

            pts_spinner = pts_dialog.findViewById(R.id.pts_spinner);
            add_pts = pts_dialog.findViewById(R.id.add_pts);

            products_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            products_rv.setAdapter(ordersAdapter);

            if (getArguments() != null) {
                items = getArguments().getParcelableArrayList("clist");
                total.setValue(getArguments().getDouble("total"));

                ordersAdapter.setProducts(items);

                semi_final_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(calculateTotal(items)) + " ر.س ");
                vat = (Double) (Math.round((total.getValue() - calculateTotal(items)) * 100) / 100.00);
                vat = (Double) (Math.round((total.getValue() - calculateTotal(items)) * 100) / 100.00);
                //new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(calculateTotal(items))
                vat_without_pts = vat;
                vat_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(vat) + " ر.س ");
                discount_txt.setText("0 ر.س");
                orders_total_dialog_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(total.getValue()) + " ر.س ");
                count_tv.setText(calculateCount(items) + "");
                price_without_pts = (Double) (Math.round((total.getValue()) * 100) / 100.00);
                total_before = (Double) (Math.round(calculateTotal(items) * 100) / 100.00);
            }


            ArrayAdapter<String> typesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
            map_spinner.setAdapter(typesadapter);

            map_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    selectedShippingType = types[position];
                    if (position == 0) {
                        rec_phone.setVisibility(View.GONE);
                        rec_name.setVisibility(View.GONE);
                    } else {
                        rec_phone.setVisibility(View.VISIBLE);
                        rec_name.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            banks_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            banks_rv.setAdapter(bankAdapter);


            payments_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            payments_rv.setAdapter(paymentsAdapter);

            show_map = address_dialog.findViewById(R.id.show_map);
            address_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            address_rv.setAdapter(adapter);

            mapView = map_dailog.findViewById(R.id.mapview);
            mapprogressBar = map_dailog.findViewById(R.id.mapprogressBar);
            dismiss_map = map_dailog.findViewById(R.id.dismiss_map);
            mapView.onCreate(savedInstanceState);

            paymentMethod.setValue("يمكنك الدفع الآن أو لاحقًا على التطبيق عند وصول المندوب");
            mapView.getMapAsync(this);


            setAlertDialogs();

            addCoponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pts_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            add_copon = v.findViewById(R.id.add_copon);
            add_time = v.findViewById(R.id.add_time);
            add_address = v.findViewById(R.id.add_address);
            add_payment = v.findViewById(R.id.add_payment);
            show_products = v.findViewById(R.id.show_products);
            snd_order = v.findViewById(R.id.snd_order);

            if (addressModel!=null){
                RequestBody delivery_type = RequestBody.create(MediaType.parse("text/plain"), addressModel.getType());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), addressModel.getAddress());
                RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), addressModel.getLat() + "");
                RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), addressModel.getLng() + "");


                requestBodyMap.put("address[delivery_type]", delivery_type);
                requestBodyMap.put("address[address]", address);
                requestBodyMap.put("address[lat]", lat);
                requestBodyMap.put("address[lng]", lng);

                RequestBody username = RequestBody.create(MediaType.parse("text/plain"), addressModel.getUsername());

                requestBodyMap.put("address[username]", username);

                RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), addressModel.getMobile());

                requestBodyMap.put("address[mobile]", phone);
            }

            viewModel.banks.observe(getActivity(), new Observer<BanksModel>() {
                @Override
                public void onChanged(BanksModel banksModel) {
                    if (getActivity()!=null)

                        activity.showDialog(false);

                    if (banksModel.getSuccess())
                        bankAdapter.setBanks((ArrayList<Bank>) banksModel.getBanks());
                }
            });

            viewModel.points.observe(getViewLifecycleOwner(), new Observer<PointsModel>() {
                @Override
                public void onChanged(PointsModel pointsModel) {
                    if (pointsModel != null) {
                        if (pointsModel.getSuccess()) {
                            balance = pointsModel.getData().getTotalPoints();
                            rsed.setText("الرصيد "+balance);

                            Log.e("balance",balance+"");
                            /*if (pointsModel.getData().getToExchange().size() > 0) {
                                pts_amounts = new ArrayList<>();
                                pts_c_txt.setText(pointsModel.getData().getTotalPoints() + " نقطة ");
                                for (Long l : pointsModel.getData().getToExchange()) {
                                    pts_amounts.add(l.toString());
                                }
                                ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pts_amounts);
                                pts_spinner.setAdapter(aarrdapter);
                            }*/
                        }
                    }
                }
            });

            viewModel.addresses.observe(getActivity(), new Observer<AddressNewResponse>() {
                @Override
                public void onChanged(AddressNewResponse addressModel) {
                    if (getActivity()!=null)

                        activity.showDialog(false);

                    if (addressModel.isSuccess()) {
                        adapter.setAddresses((ArrayList<DataItem>) addressModel.getData().getData());
                    } /*else if (addressModel.getStatus() == 401) {
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }*/
                }
            });

            snd_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // activity.fragmentStack.push(new OrderDoneFragment());


                    if (selected_payment_method.equals("visa")) {
                        if (activity.isPaymentSuccess()) {
                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("order", "order");

                            startActivity(intent);
                        } else {
                            confirmOrder();
                        }
                    } else
                        confirmOrder();


                }
            });


            pts_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    isBalance = isChecked;

                    if (balance==0){
                        Toast.makeText(getActivity(), "ليس لديك رصيد", Toast.LENGTH_SHORT).show();
                        pts_switch.setChecked(false);
                        isBalance=false;
                        return;
                    }

                    if (balance>0){
                        doCalculations();
                    }




                   /* if (viewModel.points != null) {
                        if (viewModel.points.getValue() != null) {
                            if (viewModel.points.getValue().getSuccess()) {
                                if (viewModel.points.getValue().getData().getExpireDate() <= 0) {
                                    Toast.makeText(getActivity(), "ليس لديك نقاط صالحة للاستبدال", Toast.LENGTH_SHORT).show();
                                    pts_switch.setChecked(false);
                                    pts_switch.setClickable(false);
                                    return;
                                } else {
                                    pts_switch.setClickable(true);

                                }
                            }

                        }
                    }
                    if (isChecked) {
                        if (pts_amounts.size() > 0) {
                            pts_dialog.show();
                        } else {
                            Toast.makeText(getActivity(), "ليس لديك نقاط كافية", Toast.LENGTH_SHORT).show();
                            pts_switch.setChecked(false);
                        }

                        if (pts_amounts.size() > 0)
                            ptsDiscount = (Double.parseDouble(pts_amounts.get(pts_spinner.getSelectedItemPosition()))) / 50 * .05;

                        doCalculations();


                    } else {

                        ptsDiscount = 0d;

                        doCalculations();

                    }*/


                }
            });


            total.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    final_total_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(aDouble)+ " ر.س ");
                }
            });

            viewModel.copon.observe(getViewLifecycleOwner(), new Observer<CoponModel>() {
                @Override
                public void onChanged(CoponModel coponModel) {

                    activity.showDialog(false);
                    if (coponModel != null) {
                        if (coponModel.getSuccess()) {
                            selected_copon.setText(coponModel.getPromocode().getCode());

                            coponDiscount = Double.parseDouble(coponModel.getPromocode().getAmount());

                            doCalculations();

                            RequestBody coupon = RequestBody.create(MediaType.parse("text/plain"), coponModel.getPromocode().getCode());

                            requestBodyMap.put("coupon", coupon);
                            requestBodyMap.put("promocode", coupon);

                        } else {
                            Toast.makeText(getActivity(), "الكود خاطئ", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getActivity(), "الكود خاطئ", Toast.LENGTH_SHORT).show();

                }
            });

            viewModel.payment.observe(getViewLifecycleOwner(), new Observer<PaymentModel>() {
                @Override
                public void onChanged(PaymentModel paymentModel) {
                        if (paymentModel!=null){
                            Log.e("ppppppppp",paymentModel.getPaymentMethods().size()+"");
                            if (paymentModel.getSuccess()){
                                if (paymentModel.getPaymentMethods()!=null){
                                    paymentsAdapter.setList(paymentModel.getPaymentMethods());
                                }
                            }
                        }
                }
            });


            viewModel.times.observe(getActivity(), new Observer<TimesModel>() {
                @Override
                public void onChanged(TimesModel timesModel) {
                    if (isAdded()) {
                        if (getActivity()!=null)

                            activity.showDialog(false);
                       /* if (timesModel != null) {
                            if (timesModel.getStatus() == 401) {
                                Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                return;
                            }
                        }*/
                        if (timesModel != null) {
                            if (timesModel.getSuccess()) {
                                times = new ArrayList<>();
                                for (Time t : timesModel.getTimes())
                                    times.add(t.getName());
                                ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, times);
                                time_spinner.setAdapter(aarrdapter);
                            }
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


            bank_acc_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClipboard(getActivity(), bank_acc_no.getText().toString());
                    Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();
                }
            });

            bank_iban.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClipboard(getActivity(), bank_iban.getText().toString());
                    Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();

                }
            });
            add_pts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pts_dialog.dismiss();

                    if (pts_amounts.size() > 0) {


                        ptsDiscount = (Double.parseDouble(pts_amounts.get(pts_spinner.getSelectedItemPosition()))) / 50 * .05;


                        doCalculations();


                        RequestBody points = RequestBody.create(MediaType.parse("text/plain"), pts_amounts.get(pts_spinner.getSelectedItemPosition()));

                        requestBodyMap.put("points", points);

                    }
                }
            });


            confirm_transfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (transfer_no.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "ادخل رقم العملية", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RequestBody trans_no = RequestBody.create(MediaType.parse("text/plain"), transfer_no.getText().toString());

                    requestBodyMap.put("bank_transfer_no", trans_no);
                    confirmOrder();
                }
            });
            add_copon_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (copon_et.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "ادخل الكوبون", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getActivity()!=null)

                        activity.showDialog(true);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("coupon", copon_et.getText().toString());
                    jsonObject.addProperty("promocode", copon_et.getText().toString());
                    jsonObject.addProperty("total", total.getValue());
                    viewModel.checkCopon(jsonObject, "Bearer " + token);

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


                    selected_address.setText(convertedAddress);
                    selected_address_type.setText(types[map_spinner.getSelectedItemPosition()]);

                    map_dailog.dismiss();
                    address_dialog.dismiss();

                    RequestBody delivery_type = RequestBody.create(MediaType.parse("text/plain"), types_e[map_spinner.getSelectedItemPosition()]);
                    RequestBody address = RequestBody.create(MediaType.parse("text/plain"), convertedAddress);
                    RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), selectedLocation.getLatitude() + "");
                    RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), selectedLocation.getLongitude() + "");


                    requestBodyMap.put("address[delivery_type]", delivery_type);
                    requestBodyMap.put("address[address]", address);
                    requestBodyMap.put("address[lat]", lat);
                    requestBodyMap.put("address[lng]", lng);

                }
            });


            pts_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (times != null) {
                        if (times.size() > 0) {
                            String stimw = times.get(position);

                            String[] tarr = stimw.split("-");
                            RequestBody delivery_start_time = RequestBody.create(MediaType.parse("text/plain"), tarr[0].replace(" ", "") /*+ ":00"*/);
                            RequestBody delivery_end_time = RequestBody.create(MediaType.parse("text/plain"), tarr[1].replace(" ", "") /*+ ":00"*/);
                            time = stimw;
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
                    Calendar rightNow = Calendar.getInstance();
                    int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                    Calendar cal = Calendar.getInstance();


                    if (currentHourIn24Format>=18){
                        cal.add(Calendar.DAY_OF_YEAR, 2);
                        datePicker.setMinDate(cal.getTimeInMillis());
                    }else{
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                        datePicker.setMinDate(cal.getTimeInMillis());
                    }



                  //  datePicker.setMinDate(System.currentTimeMillis() - 1000);

                    datePickerDialog.show();
                }


            });




            done_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int year = datePicker.getYear();
                    int dayOfMonth = datePicker.getDayOfMonth();
                    int monthOfYear = datePicker.getMonth();

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
                    activity.showDialog(true);

                    RequestBody delivery_date = RequestBody.create(MediaType.parse("text/plain"), year + "-" + month + "-" + day);

                    requestBodyMap.put("delivery_date", delivery_date);

                    date = year + "-" + month + "-" + day;
                    jsonObject.addProperty("delivery_date", year + "-" + month + "-" + day);
                    viewModel.getTimes(jsonObject, "Bearer " + Paper.book().read("token").toString());
                    datePickerDialog.dismiss();
                }
            });

            confirm_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeDialog.dismiss();
                    if (date != null && time != null) {
                        if (!date.isEmpty() && !time.isEmpty()) {
                            selected_date.setText(date + " . " + time);
                        }
                    }
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
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
                    //address_dialog.show();
                    if (addressModel==null){
                        startActivity(new Intent(activity,MapsActivity.class));
                    activity.finish();}
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


           /* p_bank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentMethod.setValue("تحويل بنكي");
                    selected_payment_method = "bank";
                    confirmDialog.show();
                }
            });*/
           /* p_visa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentMethod.setValue("بطاقة مدى / visa");
                    selected_payment_method = "visa";

                    confirmOrder();

                }
            });*/

           /* p_deliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentMethod.setValue("الدفع عن الإستلام");
                    selected_payment_method = "cash";

                    payDialog.dismiss();
                }
            });*/

            paymentMethod.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    selected_payment.setText(s);
                }
            });
            setDialogsFullScreen();
        }


        return v;
    }

    private void confirmOrder() {


        if (pts_switch.isChecked()) {
            if (!requestBodyMap.containsKey("points")) {
                RequestBody points = RequestBody.create(MediaType.parse("text/plain"), pts_amounts.get(pts_spinner.getSelectedItemPosition()));

                requestBodyMap.put("points", points);
            }

        } else {
            if (requestBodyMap.containsKey("points"))
                requestBodyMap.remove("points");
        }

        if (!requestBodyMap.containsKey("address[address]") || !requestBodyMap.containsKey("address[lng]") || !requestBodyMap.containsKey("address[lat]")) {
            Toast.makeText(getActivity(), "ادخل عنوان التوصيل", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!requestBodyMap.containsKey("address[delivery_type]")) {
            Toast.makeText(getActivity(), "ادخل نوع المستلم", Toast.LENGTH_SHORT).show();
            return;
        }
       /* if (selectedShippingType != null) {
            if (map_spinner.getSelectedItemPosition() != 0) {
                if (rec_name.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل اسم المستلم", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    RequestBody username = RequestBody.create(MediaType.parse("text/plain"), rec_name.getText().toString());

                    requestBodyMap.put("address[username]", username);
                }
                if (rec_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل رقم الجوال للمستلم", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), rec_phone.getText().toString());

                    requestBodyMap.put("address[mobile]", phone);
                }
            }

          //  Toast.makeText(getContext(), types_e[map_spinner.getSelectedItemPosition()], Toast.LENGTH_SHORT).show();
        }*/
        if (!requestBodyMap.containsKey("delivery_date")) {
            Toast.makeText(getActivity(), "ادخل تاريخ الاستلام", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!requestBodyMap.containsKey("delivery_start_time") || !requestBodyMap.containsKey("delivery_end_time")) {
            Toast.makeText(getActivity(), "ادخل موعد الاستلام", Toast.LENGTH_SHORT).show();
            return;
        }


        if (selected_payment_method.isEmpty()) {
            Toast.makeText(getActivity(), "اختر طريقة الدفع", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pts_switch.isChecked()) {
            requestBodyMap.remove("points");
        }


        RequestBody payment_method = RequestBody.create(MediaType.parse("text/plain"), selected_payment_method);

        requestBodyMap.put("payment_method", payment_method);


        if (selected_payment_method.equals("bank")) {
            if (!requestBodyMap.containsKey("bank_id")) {
                Toast.makeText(getActivity(), "اختر البنك", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!requestBodyMap.containsKey("bank_transfer_no")) {
                Toast.makeText(getActivity(), "ادخل رقم العملية", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (int i = 0; i < items.size(); i++) {
            RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), items.get(i).getId().toString());
            RequestBody quantity1 = RequestBody.create(MediaType.parse("text/plain"), items.get(i).getInCartQuantity() + "");
            requestBodyMap.put("products[" + i + "][id]", id1);
            requestBodyMap.put("products[" + i + "][quantity]", quantity1);
            if (items.get(i).getCity_id() != null) {
                RequestBody city_id1 = RequestBody.create(MediaType.parse("text/plain"), items.get(i).getCity_id());
                requestBodyMap.put("products[" + i + "][city_id]", city_id1);


            }
        }


        String token = "Bearer " + Paper.book().read("token");

        confirmDialog.dismiss();
        payDialog.dismiss();

        activity.showDialog(true);
        if (selected_payment_method.equals("visa")) {
            RetrofitClient.getApiInterface().sendOrderVisa(requestBodyMap, token).enqueue(new Callback<VisaModel>() {
                @Override
                public void onResponse(Call<VisaModel> call, Response<VisaModel> response) {
                    Log.d("resooo", response.message());

                    if (response.isSuccessful()) {

                       /* BlankFragment blankFragment = new BlankFragment();
                        Bundle b = new Bundle();

                        try {
                            b.putString("html", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        blankFragment.setArguments(b);
                        activity.fragmentStack.push(blankFragment);*/
                        VisaModel visaModel = response.body();

                        if (visaModel != null) {
                            if (visaModel.getSuccess()) {
                                BlankFragment blankFragment = new BlankFragment();
                                Bundle b = new Bundle();

                                b.putString("html", visaModel.getData().getView());
                                blankFragment.setArguments(b);
                                activity.fragmentStack.push(blankFragment);


                            }
                        }
                    } else
                    if (getActivity()!=null)

                        activity.showDialog(false);


                }

                @Override
                public void onFailure(Call<VisaModel> call, Throwable t) {
                    Log.d("resooo", t.getMessage());
                    if (getActivity()!=null)

                        activity.showDialog(false);

                }
            });
        } else {
            RetrofitClient.getApiInterface().sendOrder(requestBodyMap, token).enqueue(new Callback<ConfirmModel>() {
                @Override
                public void onResponse(Call<ConfirmModel> call, Response<ConfirmModel> response) {
                    Log.d("resooo", response.message());
                    if (getActivity()!=null)
                    activity.showDialog(false);
                    if (response.body()!=null)
                    Toast.makeText(activity, response.body().message, Toast.LENGTH_SHORT).show();
                    if (response.code() == 200) {

                        activity.fragmentStack.replace(new OrderDoneFragment());
                    }
                }

                @Override
                public void onFailure(Call<ConfirmModel> call, Throwable t) {
                    Log.d("resooo", t.getMessage());
                    if (getActivity()!=null)

                        activity.showDialog(false);

                }
            });
        }

    }

    Double calculateTotal(ArrayList<Product> products) {
        Double sum = 0.0;

        for (Product p : products) {
            sum += (Double.parseDouble(p.getInCartQuantity().toString()) * (p.getPrice()));
        }

        return sum;
    }

    int calculateCount(ArrayList<Product> products) {
        int sum = 0;

        for (Product p : products) {
            sum += p.getInCartQuantity();
        }

        return sum;
    }

    private void setAlertDialogs() {
        Window window1 = addCoponDialog.getWindow();
        Window window2 = timeDialog.getWindow();
        Window window22 = pts_dialog.getWindow();
        Window window3 = payDialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window22.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        super.onResume();

        if (mapView!=null)

            mapView.onResume();

    }


    @Override
    public void onPause() {

            super.onPause();
        if (mapView!=null)
        mapView.onPause();
    }

    @Override
    public void onDestroy() {


            super.onDestroy();
        if (mapView!=null)

            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {

            super.onLowMemory();
        if (mapView!=null)

            mapView.onLowMemory();
    }

    @Override
    public void setAddress(String type, DataItem address) {
        address_dialog.dismiss();
        // selected_address.setText(address);
        selectedLocation = new Location("");
        selectedLocation.setLatitude(Double.parseDouble(address.getLat()));
        selectedLocation.setLongitude(Double.parseDouble(address.getLng()));

        ///////////////////////////////////////////////////////////////////// new scenario
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("lat", Double.parseDouble(address.getLat()));
        intent.putExtra("lng", Double.parseDouble(address.getLng()));
        intent.putExtra("id", address.getId());
        intent.putExtra("address", address.getAddress());
        Log.e("addddddd",address.getAddress());

        startActivity(intent);



        ////////////////////////////////////////////////////////////////////


        /*try {
            convertedAddress = getAddress(selectedLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("sele", address.getLat() + " + " + address.getLng());
        map_dailog.show();

        if (map!=null)
        map.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude()));

        mp.title("my position");
        if (map!=null)
        map.addMarker(mp);

        if (map!=null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude()), 16));*/
    }

    @Override
    public void onDelete(DataItem address) {

    }

    void doCalculations() {
        discount_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format((total_before * (ptsDiscount + (coponDiscount / 100)))) + " ر.س ");


        vat_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format((vat - (vat * (ptsDiscount + (coponDiscount / 100))))) + " ر.س ");
        Double v = vat - ((Double) (Math.round((vat * (ptsDiscount + (coponDiscount / 100))) * 100) / 100.00));

        Double sum = total_before - (total_before * (ptsDiscount + (coponDiscount / 100))) + v;

        if (isBalance){
            if (balance>=sum){
                sum=0d;
            }else {
                sum=sum-balance;
            }
        }
        total.setValue(sum);


    }

    @Override
    public void onStart() {
        super.onStart();

       String cid = Paper.book().read("cid");
        // activity.showDialog(true);
        //  next.startAnimation();
        cartViewModel.getCard(cid + "");

        cartViewModel.cardModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<CardModel>() {
            @Override
            public void onChanged(CardModel cardModel) {
                if (cardModel!=null){
                    if (cardModel.getSuccess()){
                        if (cardModel.getData().getItemsCount()==0){
                            if (getActivity()!=null){
                                activity.fragmentStack.replace(new OrderDoneFragment());

                            }

                        }
                        Log.e("cart","cart is "+cardModel.getData().getItemsCount().toString());
                    }
                }
            }
        });


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
                                            if (map!=null)
                                            map.clear();

                                            MarkerOptions mp = new MarkerOptions();

                                            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                                            mp.title("my position");

                                            if (map!=null)
                                                map.addMarker(mp);

                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(location.getLatitude(), location.getLongitude()), 16));


                                            selectedLocation = location;
                                            try {
                                                convertedAddress = getAddress(selectedLocation);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }catch (NullPointerException e){
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
        bank_details.setText(bank.getName());
        bank_acc_no.setText(bank.getAccountNumber());
        bank_iban.setText(bank.getIban());

        for (int i = 0; i < viewModel.banks.getValue().getBanks().size(); i++) {
            if (viewModel.banks.getValue().getBanks().get(i).getId() == bank.getId()) {
                viewModel.banks.getValue().getBanks().get(i).is = 1;

            } else {
                viewModel.banks.getValue().getBanks().get(i).is = 0;

            }

        }

        copy_iban.setVisibility(View.VISIBLE);
        copy_account.setVisibility(View.VISIBLE);

        copy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bank_acc_no.getText().toString().isEmpty()) {
                    setClipboard(getActivity(), bank_acc_no.getText().toString());
                    Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        copy_iban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bank_iban.getText().toString().isEmpty()) {
                    setClipboard(getActivity(), bank_iban.getText().toString());
                    Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selected_bank = bank;
        bankAdapter.setBanks((ArrayList<Bank>) viewModel.banks.getValue().getBanks());

        RequestBody bank_id = RequestBody.create(MediaType.parse("text/plain"), selected_bank.getId().toString());

        requestBodyMap.put("bank_id", bank_id);
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getActivity()!=null)
        activity.setPaymentSuccess(false);

    }


    @Override
    public void onClickPressed(PaymentMethod method) {
        if (method.getGateway().equals("bank")){
            paymentMethod.setValue("تحويل بنكي");
            selected_payment_method = "bank";
            confirmDialog.show();
        }
        else if(method.getGateway().equals("cash")){
            paymentMethod.setValue("الدفع عن الإستلام");
            selected_payment_method = "cash";

            payDialog.dismiss();
        }
        else {
            paymentMethod.setValue("بطاقة مدى / visa");
            selected_payment_method = "visa";

            confirmOrder();
        }
    }
}