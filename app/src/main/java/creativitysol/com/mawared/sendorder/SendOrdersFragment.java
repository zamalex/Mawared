package creativitysol.com.mawared.sendorder;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.CameraPosition;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import creativitysol.com.mawared.BlankFragment;
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
import creativitysol.com.mawared.sendorder.model.paymentmodel.ConfirmModel;
import creativitysol.com.mawared.sendorder.model.paymentmodel.visa.VisaModel;
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

    Dialog addCoponDialog, timeDialog, pts_dialog;
    RecyclerView address_rv, products_rv, banks_rv;

    BottomSheetDialog address_dialog, map_dailog, payDialog, ordersDialog, confirmDialog,datePickerDialog;

    AddressAdapter adapter;
    SentOrdersAdapter ordersAdapter;
    BankAdapter bankAdapter;

    MapView mapView;
    GoogleMap map;

    Button snd_order, p_bank, p_deliver, p_visa, add_copon_btn, add_pts, confirm_transfer;

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
    String selectedShippingType = null;
    String convertedAddress = null;
    ArrayList<String> times;
    ArrayList<String> pts_amounts = new ArrayList<>();
    Double coponDiscount = 0d;
    MutableLiveData<Double> total = new MutableLiveData<>();
    Double ptsDiscount = 0d;
    Double total_before = 0d;
    Double vat = 0d;

    TextView semi_final_txt, vat_txt, discount_txt, pts_c_txt, count_tv,done_date;
    DatePicker datePicker;

    SwitchMaterial pts_switch;

    Bank selected_bank = null;

    String date = "", time = "";

    String selected_payment_method = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_send_orders, container, false);

            back = v.findViewById(R.id.imageView);
            final_total_txt = v.findViewById(R.id.final_total_txt);
            terms_txt = v.findViewById(R.id.terms_txt);
            pts_switch = v.findViewById(R.id.imageView42);

            selected_address = v.findViewById(R.id.selected_address);
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
            // viewModel.getPayment();


            addCoponDialog = new Dialog(getActivity());
            timeDialog = new Dialog(getActivity());
            pts_dialog = new Dialog(getActivity());

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
            p_bank = payDialog.findViewById(R.id.p_bank);
            p_deliver = payDialog.findViewById(R.id.p_deliver);
            p_visa = payDialog.findViewById(R.id.p_visa);
            set_loc = map_dailog.findViewById(R.id.set_loc);
            map_spinner = map_dailog.findViewById(R.id.map_spinner);
            btn_add_loc = map_dailog.findViewById(R.id.btn_add_loc);
            address_rv = address_dialog.findViewById(R.id.address_rv);
            banks_rv = confirmDialog.findViewById(R.id.banks_rv);
            transfer_no = confirmDialog.findViewById(R.id.transfer_no);
            confirm_transfer = confirmDialog.findViewById(R.id.cnonfirm_transfer);
            add_copon_btn = addCoponDialog.findViewById(R.id.add_copon);
            copon_et = addCoponDialog.findViewById(R.id.copon_et);

            time_spinner = timeDialog.findViewById(R.id.time_spinner);
            products_rv = ordersDialog.findViewById(R.id.products_rv);
            orders_total_dialog_txt = ordersDialog.findViewById(R.id.orders_total);

            pts_spinner = pts_dialog.findViewById(R.id.pts_spinner);
            add_pts = pts_dialog.findViewById(R.id.add_pts);

            products_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            products_rv.setAdapter(ordersAdapter);

            if (getArguments() != null) {
                items = getArguments().getParcelableArrayList("clist");
                total.setValue(getArguments().getDouble("total"));

                ordersAdapter.setProducts(items);

                semi_final_txt.setText((Double) (Math.round(calculateTotal(items) * 100) / 100.00) + " ر.س ");
                vat = (Double) (Math.round((total.getValue() - calculateTotal(items)) * 100) / 100.00);
                vat_txt.setText(vat + " ر.س ");
                discount_txt.setText("0 ر.س");
                orders_total_dialog_txt.setText((Double) (Math.round((total.getValue()) * 100) / 100.00) + " ر.س ");
                count_tv.setText(calculateCount(items) + "");

                total_before = (Double) (Math.round(calculateTotal(items) * 100) / 100.00);
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
            pts_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                        if (pointsModel.getSuccess()) {
                            if (pointsModel.getData().getToExchange().size() > 0) {
                                pts_amounts = new ArrayList<>();
                                pts_c_txt.setText(pointsModel.getData().getTotalPoints() + " نقطة ");
                                for (Long l : pointsModel.getData().getToExchange()) {
                                    pts_amounts.add(l.toString());
                                }
                                ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pts_amounts);
                                pts_spinner.setAdapter(aarrdapter);
                            }
                        }
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



                    if (selected_payment_method.equals("visa")) {
                        if (((MainActivity)getActivity()).isPaymentSuccess()){
                            getActivity().finishAffinity();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("order","order");

                            startActivity(intent);
                        }else {
                            confirmOrder();
                        }
                    } else
                        confirmOrder();


                }
            });


            pts_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (viewModel.points != null) {
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
                    }


                }
            });


            total.observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double aDouble) {
                    final_total_txt.setText((Double) (Math.round(aDouble * 100) / 100.00) + " ر.س ");
                }
            });

            viewModel.copon.observe(getViewLifecycleOwner(), new Observer<CoponModel>() {
                @Override
                public void onChanged(CoponModel coponModel) {

                    ((MainActivity) getActivity()).showDialog(false);
                    if (coponModel != null) {
                        if (coponModel.getSuccess()) {
                            selected_copon.setText(coponModel.getPromocode().getCode());

                            coponDiscount = Double.parseDouble(coponModel.getPromocode().getAmount());
                            discount_txt.setText((Double) (Math.round((total.getValue() * coponDiscount / 100) * 100) / 100.00) + " ر.س ");

                            Double sum = total.getValue() - (total.getValue() * coponDiscount / 100);
                            total.setValue(sum);
                            vat_txt.setText(vat - ((Double) (Math.round((vat * coponDiscount / 100) * 100) / 100.00)) + " ر.س ");
                            vat = vat - ((Double) (Math.round((vat * coponDiscount / 100) * 100) / 100.00));

                            RequestBody coupon = RequestBody.create(MediaType.parse("text/plain"), coponModel.getPromocode().getCode());

                            requestBodyMap.put("coupon", coupon);

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

                        Toast.makeText(getActivity(), ptsDiscount.toString(), Toast.LENGTH_SHORT).show();
                        discount_txt.setText((Double) (Math.round((total.getValue() * ptsDiscount) * 100) / 100.00) + " ر.س ");

                        Double sum = total.getValue() - (total.getValue() * ptsDiscount);
                        total.setValue(sum);
                        vat_txt.setText(vat - ((Double) (Math.round((vat * ptsDiscount) * 100) / 100.00)) + " ر.س ");
                        vat = vat - ((Double) (Math.round((vat * ptsDiscount) * 100) / 100.00));


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
                    ((MainActivity) getActivity()).showDialog(true);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("coupon", copon_et.getText().toString());
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
                            RequestBody delivery_start_time = RequestBody.create(MediaType.parse("text/plain"), tarr[0].replace(" ", "") + ":00");
                            RequestBody delivery_end_time = RequestBody.create(MediaType.parse("text/plain"), tarr[1].replace(" ", "") + ":00");
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


                    datePickerDialog.show();
                }



            });
            datePicker.setMinDate(System.currentTimeMillis() - 1000);
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
                    ((MainActivity) getActivity()).showDialog(true);

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
                    selected_payment_method = "bank";
                    confirmDialog.show();
                }
            });
            p_visa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentMethod.setValue("بطاقة مدى / visa");
                    selected_payment_method = "visa";

                    confirmOrder();

                }
            });

            p_deliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentMethod.setValue("الدفع عن الإستلام");
                    selected_payment_method = "cash";

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
        }


        return v;
    }

    private void confirmOrder() {
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


        if (selected_payment_method.isEmpty()) {
            Toast.makeText(getActivity(), "اختر طريقة الدفع", Toast.LENGTH_SHORT).show();
            return;
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
        }


        String token = "Bearer " + Paper.book().read("token");

        confirmDialog.dismiss();
        payDialog.dismiss();

        ((MainActivity) getActivity()).showDialog(true);
        if (selected_payment_method.equals("visa")) {
            RetrofitClient.getApiInterface().sendOrderVisa(requestBodyMap, token).enqueue(new Callback<VisaModel>() {
                @Override
                public void onResponse(Call<VisaModel> call, Response<VisaModel> response) {
                    Log.d("resooo", response.message());
                    ((MainActivity) getActivity()).showDialog(false);

                    if (response.isSuccessful()) {
                        VisaModel visaModel = response.body();

                        if (visaModel != null) {
                            if (visaModel.getSuccess()) {
                                BlankFragment blankFragment = new BlankFragment();
                                Bundle b = new Bundle();

                                b.putString("html", visaModel.getData().getView());
                                blankFragment.setArguments(b);
                                ((MainActivity) getActivity()).fragmentStack.push(blankFragment);


                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<VisaModel> call, Throwable t) {
                    Log.d("resooo", t.getMessage());
                    ((MainActivity) getActivity()).showDialog(false);

                }
            });
        } else {
            RetrofitClient.getApiInterface().sendOrder(requestBodyMap, token).enqueue(new Callback<ConfirmModel>() {
                @Override
                public void onResponse(Call<ConfirmModel> call, Response<ConfirmModel> response) {
                    Log.d("resooo", response.message());
                    ((MainActivity) getActivity()).showDialog(false);
                    if (response != null) {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        if (response.code() == 200) {


                            ((MainActivity) getActivity()).fragmentStack.replace(new OrderDoneFragment());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ConfirmModel> call, Throwable t) {
                    Log.d("resooo", t.getMessage());
                    ((MainActivity) getActivity()).showDialog(false);

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
        ((MainActivity) getActivity()).setPaymentSuccess(false);

    }


}