package app.mawared.alhayat.orderdetails;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import app.mawared.alhayat.BlankFragment;
import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.OrderDoneFragment;
import app.mawared.alhayat.R;
import app.mawared.alhayat.UpdateVisaFragment;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.orderdetails.model.OrderDetails;
import app.mawared.alhayat.orders.OrdersStatus;
import app.mawared.alhayat.sendorder.BankAdapter;
import app.mawared.alhayat.sendorder.PaymentsAdapter;
import app.mawared.alhayat.sendorder.SendOrderViewModel;
import app.mawared.alhayat.sendorder.model.Bank;
import app.mawared.alhayat.sendorder.model.BanksModel;
import app.mawared.alhayat.sendorder.model.PaymentMethod;
import app.mawared.alhayat.sendorder.model.PaymentModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.ConfirmModel;
import app.mawared.alhayat.sendorder.model.paymentmodel.visa.VisaModel;
import app.mawared.alhayat.support.SupportFragment;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetailsFragment extends Fragment implements PaymentsAdapter.paymentClickListener, BankAdapter.BankInterface {

    Button confirm_transfer;
    int orderId;
    OrderDetailsViewModel orderDetailsViewModel;
    RecyclerView rv_productDetails;
    OrderDetailsAdapter orderDetailsAdapter;
    TextView tv_orderDetailsNumber, tv_orderDetailsStatus, tv_totalPrice;
    ConstraintLayout cl_orderStatus;
    double totalPrice;
    ImageView go_support;
    ImageView ic_BackBtn;
    FragmentStack fragmentStack;
    ImageButton cancel_order;
    Dialog dialog;
    Dialog done;
    Button ccncl_btn;
    EditText reason_et,transfer_no;
    String status = "none";
    BottomSheetDialog rateDialog;
    RatingBar ratingBar,shipping_bar;
    String token = Paper.book().read("token");

    TextView one, two, three, four, five, six, seven,change, bank_details, bank_acc_no, bank_iban,mname,mmobile;
    Bank selected_bank = null;

    RecyclerView paymentsRV,banks_rv;

    PaymentsAdapter paymentsAdapter;
    SendOrderViewModel viewModel;
    MainActivity activity;

    BottomSheetDialog payDialog,confirmDialog;
    String selected_payment_method = "";


    ImageView copy_account, copy_iban;
    BankAdapter bankAdapter;

    JsonObject jsonObject = new JsonObject();



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        View view = inflater.inflate(R.layout.fragment_order_details_details, container, false);

        setPaymentDialog();
        dialog = new Dialog(getActivity());
        done = new Dialog(getActivity());
        dialog.setContentView(R.layout.reason_dialog);
        done.setContentView(R.layout.done_dialog);
        Window window1 = dialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rateDialog = new BottomSheetDialog(getActivity());

        done.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        rateDialog.setContentView(R.layout.rate_dialog);
        ratingBar = rateDialog.findViewById(R.id.stars);
        shipping_bar = rateDialog.findViewById(R.id.shipping_stars);

        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window3 = rateDialog.getWindow();
        window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paymentsRV = payDialog.findViewById(R.id.payment_rv);
        paymentsAdapter = new PaymentsAdapter(this);
        paymentsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        paymentsRV.setAdapter(paymentsAdapter);
        orderDetailsViewModel = new ViewModelProvider(OrderDetailsFragment.this).get(OrderDetailsViewModel.class);

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);
        viewModel.getPayment();
        setbanksDialog();

        viewModel.payment.observe(getViewLifecycleOwner(), new Observer<PaymentModel>() {
            @Override
            public void onChanged(PaymentModel paymentModel) {
                if (paymentModel!=null){
                    if (paymentModel.getSuccess()){
                        if (paymentModel.getPaymentMethods()!=null){
                            Log.e("size iss ",paymentModel.getPaymentMethods().size()+"");
                           // paymentModel.getPaymentMethods().removeIf(i->i.getGateway().equals("cash"));
                            paymentsAdapter.setList(paymentModel.getPaymentMethods());
                        }
                    }
                }
            }
        });


        dialog.findViewById(R.id.xreason).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rv_productDetails = view.findViewById(R.id.rv_productDetails);
        go_support = view.findViewById(R.id.imageView6);
        cl_orderStatus = view.findViewById(R.id.cl_orderDetailsStatus);
        tv_orderDetailsNumber = view.findViewById(R.id.tv_orderDetailsNumber);
        tv_orderDetailsStatus = view.findViewById(R.id.tv_orderDetailsStatus);
        tv_totalPrice = view.findViewById(R.id.tv_totalPrice);
        ic_BackBtn = view.findViewById(R.id.ic_BackBtn);
        cancel_order = view.findViewById(R.id.cancel_order);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        change = view.findViewById(R.id.change);
        mname = view.findViewById(R.id.mname);
        mmobile = view.findViewById(R.id.mmobile);



        reason_et = dialog.findViewById(R.id.reason_et);
        ccncl_btn = dialog.findViewById(R.id.ccncl_btn);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_productDetails.setLayoutManager(gridLayoutManager);

        orderId = getActivity().getSharedPreferences("mwared", Context.MODE_PRIVATE).getInt("orderId", 0);

        rateDialog.findViewById(R.id.xrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.dismiss();
            }
        });

        trackPaymentUpdate();
        rateDialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", orderId + "");
                jsonObject.addProperty("commnet", "");
                jsonObject.addProperty("stars", ratingBar.getRating() + "");
                jsonObject.addProperty("products_rating", ratingBar.getRating() + "");
                jsonObject.addProperty("driver_rating_stars", shipping_bar.getRating() + "");
                jsonObject.addProperty("driver_rating", shipping_bar.getRating() + "");
                RetrofitClient.getApiInterface().rateOrder("Bearer " + token, jsonObject,orderId+"").enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        rateDialog.dismiss();
                        done.show();


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        rateDialog.dismiss();

                    }
                });
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        payDialog.show();

                     }
        });

        ((MainActivity) getActivity()).showDialog(true);

        orderDetailsViewModel.getOrderDetails(orderId).observe(getActivity(), new Observer<OrderDetails>() {
            @Override
            public void onChanged(OrderDetails orderDetails) {
                if (getActivity()!=null)
                ((MainActivity) getActivity()).showDialog(false);

                if (orderDetails != null) {
                    if(orderDetails.getOrder().payment_status){
                       change.setVisibility(View.GONE);
                    }
                    if (orderDetails.getOrder().getDriver()!=null){
                        mname.setText(orderDetails.getOrder().getDriver().getName());
                        mmobile.setText(orderDetails.getOrder().getDriver().getMobile());
                    }

                    cancel_order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orderDetails.getOrder().can_cancel)
                                dialog.show();
                            else
                                Toast.makeText(getActivity(), "لا يمكن الغاء الطلب", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (orderDetails.getOrder().isShowRating())
                        //if (orderDetails.getOrder().getStatus().equals("تم التسليم"))
                        rateDialog.show();


                    orderDetailsAdapter = new OrderDetailsAdapter(orderDetails.getOrder().getProducts());
                    rv_productDetails.setAdapter(orderDetailsAdapter);
                    tv_orderDetailsNumber.setText("#تفاصيل طلب " + orderDetails.getOrder().getId());

                   /* if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب") || status.equals("جاري تجهيز طلبك"))
                        tv_orderDetailsStatus.setText("جاري تجهيز طلبك");
                    else*/
                        tv_orderDetailsStatus.setText(OrdersStatus.STATUS.get(Integer.parseInt(orderDetails.getOrder().getStatus())));

                    status = orderDetails.getOrder().getStatus();

                    if (getActivity()!=null){
                        if (status.equals("0")||status.equals("1")||status.equals("3")||status.equals("4")||status.equals("8")) {
                            cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.light_green_bg));
                        } else if (status.equals("5")||status.equals("6")||status.equals("9")) {
                            cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.malghi_bg));
                        } else {
                            cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.order_states_bg));

                        }
                    }

                    for (int i = 0; i < orderDetails.getOrder().getProducts().size(); i++) {
                        totalPrice += orderDetails.getOrder().getProducts().get(i).getTotal();
                    }

                    tv_totalPrice.setText(orderDetails.getOrder().getPricing().getTotalWithCouponVat() + " ر.س");

                    one.setText("#" + orderDetails.getOrder().getId());
                  /*  if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب") || status.equals("جاري تجهيز طلبك"))

                        two.setText("جاري تجهيز طلبك");
                    else*/
                        two.setText(OrdersStatus.STATUS.get(Integer.parseInt(orderDetails.getOrder().getStatus())));

                    three.setText(orderDetails.getOrder().getCustomerName());
                    four.setText(orderDetails.getOrder().getCustomerPhone());
                    five.setText(orderDetails.getOrder().getDeliveryDate());
                    six.setText(orderDetails.getOrder().getPaymentMethod());
                    seven.setText(orderDetails.getOrder().getAddress());


                }
            }
        });

        ic_BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentStack = new FragmentStack(getActivity(), getActivity().getSupportFragmentManager(), R.id.main_container);
                fragmentStack.back();

            }
        });


        go_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentStack.push(new SupportFragment());
            }
        });


        orderDetailsViewModel.cancelResponse.observe(getViewLifecycleOwner(), new Observer<ConfirmModel>() {
            @Override
            public void onChanged(ConfirmModel confirmModel) {
                ((MainActivity) getActivity()).showDialog(false);
                if (confirmModel != null) {
                    if (confirmModel.getSuccess()) {
                        ((MainActivity) getActivity()).fragmentStack.pop();
                    }
                }
            }
        });



        ccncl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (reason_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل سبب الالغاء", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((MainActivity) getActivity()).showDialog(true);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", orderId);
                jsonObject.addProperty("reason", reason_et.getText().toString());

                orderDetailsViewModel.cancelOrder(orderId+"", "Bearer " + Paper.book().read("token", "none"));

            }
        });

        return view;
    }

    private void setbanksDialog() {
        confirmDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        confirmDialog.setContentView(R.layout.confirm_dialog);
        copy_account = confirmDialog.findViewById(R.id.copy_account);
        copy_iban = confirmDialog.findViewById(R.id.copy_iban);
        bank_details = confirmDialog.findViewById(R.id.bank_details);
        bank_acc_no = confirmDialog.findViewById(R.id.bank_acc_no);
        bank_iban = confirmDialog.findViewById(R.id.bank_iban);
        banks_rv = confirmDialog.findViewById(R.id.banks_rv);
        confirm_transfer = confirmDialog.findViewById(R.id.cnonfirm_transfer);
        transfer_no = confirmDialog.findViewById(R.id.transfer_no);

        bankAdapter = new BankAdapter(this);

        banks_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        banks_rv.setAdapter(bankAdapter);

        confirm_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transfer_no.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل رقم العملية", Toast.LENGTH_SHORT).show();
                    return;
                }

                jsonObject.addProperty("bank_transfer_no", transfer_no.getText().toString());
                confirmOrder();
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
        viewModel.getBanks();
        viewModel.banks.observe(activity, new Observer<BanksModel>() {
            @Override
            public void onChanged(BanksModel banksModel) {
                if (getActivity()!=null)

                    activity.showDialog(false);

                if (banksModel.getSuccess())
                    bankAdapter.setBanks((ArrayList<Bank>) banksModel.getBanks());
            }
        });

    }

    @Override
    public void onClickPressed(PaymentMethod method) {
        jsonObject.addProperty("payment_method",method.getGateway());
        if (method.getGateway().equals("bank")){
            selected_payment_method = "bank";
            confirmDialog.show();
        }
        else if(method.getGateway().equals("cash")){
            selected_payment_method = "cash";

            payDialog.dismiss();

            confirmOrder();
        }
        else {
            selected_payment_method = "visa";

            confirmOrder();
        }

    }

    private void trackPaymentUpdate(){
        orderDetailsViewModel.updatePaymentResponseVisa= new MutableLiveData<>();
        orderDetailsViewModel.updatePaymentResponse= new MutableLiveData<>();
        orderDetailsViewModel.updatePaymentResponse.observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                activity.showDialog(false);
                if (selected_payment_method.equals("bank")||selected_payment_method.equals("cash")){
                    ((MainActivity)activity).fragmentStack.pop();

                }else {

                }
//                Log.e("update payment",responseBody.toString());

            }
        });

        orderDetailsViewModel.updatePaymentResponseVisa.observe(getViewLifecycleOwner(), new Observer<VisaModel>() {
            @Override
            public void onChanged(VisaModel visaModel) {
                activity.showDialog(false);

                if (selected_payment_method.equals("visa")){
                    if (visaModel != null) {
                        if (visaModel.getSuccess()) {
                            UpdateVisaFragment blankFragment = new UpdateVisaFragment();
                            Bundle b = new Bundle();

                            b.putString("html", visaModel.getData().getView());
                            blankFragment.setArguments(b);
                            activity.fragmentStack.push(blankFragment);


                        }
                    }
                }
            }
        });

    }

    private void confirmOrder() {

        if (selected_payment_method.equals("bank")) {
            if (!jsonObject.has("bank_id")) {
                Toast.makeText(getActivity(), "اختر البنك", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!jsonObject.has("bank_transfer_no")) {
                Toast.makeText(getActivity(), "ادخل رقم العملية", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        confirmDialog.dismiss();
        payDialog.dismiss();

        activity.showDialog(true);
        if (selected_payment_method.equals("visa")) {
            orderDetailsViewModel.updatePaymentVisa(orderId+"",jsonObject);
            /*Log.d("resooo", response.message());

            if (response.isSuccessful()) {
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

                activity.showDialog(false);*/
        } else {
            orderDetailsViewModel.updatePayment(orderId+"",jsonObject);

            /*RetrofitClient.getApiInterface().sendOrder(requestBodyMap, token).enqueue(new Callback<ConfirmModel>() {
                @Override
                public void onResponse(Call<ConfirmModel> call, Response<ConfirmModel> response) {
                    Log.d("resooo", response.message());
                    if (getActivity()!=null)
                        activity.showDialog(false);
                    if (response != null) {
                        if (response.code() == 200) {



                            activity.fragmentStack.replace(new OrderDoneFragment());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ConfirmModel> call, Throwable t) {
                    Log.d("resooo", t.getMessage());
                    if (getActivity()!=null)

                        activity.showDialog(false);

                }
            });*/
        }

    }

    void setPaymentDialog(){
        payDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);

        payDialog.setContentView(R.layout.payment_dialog);
        payDialog.findViewById(R.id.xpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.dismiss();
            }
        });

        Window window3 = payDialog.getWindow();
        window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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




    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }

    @Override
    public void onBankSelected(Bank bank, int p) {
        selected_bank = bank;
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

        bankAdapter.setBanks((ArrayList<Bank>) viewModel.banks.getValue().getBanks());


        jsonObject.addProperty("bank_id", selected_bank.getId()+"");
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
}