package app.mawared.alhayat.orderdetails;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.orderdetails.model.OrderDetails;
import app.mawared.alhayat.sendorder.model.paymentmodel.ConfirmModel;
import app.mawared.alhayat.support.SupportFragment;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetailsFragment extends Fragment {

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
    EditText reason_et;
    String status = "none";
    BottomSheetDialog rateDialog;
    RatingBar ratingBar,shipping_bar;
    String token = Paper.book().read("token");

    TextView one, two, three, four, five, six, seven;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details_details, container, false);

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

        rateDialog.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", orderId + "");
                jsonObject.addProperty("stars", ratingBar.getRating() + "");
                jsonObject.addProperty("driver_rating_stars", shipping_bar.getRating() + "");
                RetrofitClient.getApiInterface().rateOrder("Bearer " + token, jsonObject).enqueue(new Callback<ResponseBody>() {
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

        orderDetailsViewModel = new ViewModelProvider(OrderDetailsFragment.this).get(OrderDetailsViewModel.class);

        ((MainActivity) getActivity()).showDialog(true);

        orderDetailsViewModel.getOrderDetails(orderId).observe(getActivity(), new Observer<OrderDetails>() {
            @Override
            public void onChanged(OrderDetails orderDetails) {
                ((MainActivity) getActivity()).showDialog(false);

                if (orderDetails != null) {


                    if (orderDetails.getOrder().isShowRating())
                        rateDialog.show();


                    orderDetailsAdapter = new OrderDetailsAdapter(orderDetails.getOrder().getProducts());
                    rv_productDetails.setAdapter(orderDetailsAdapter);
                    tv_orderDetailsNumber.setText("#تفاصيل طلب " + orderDetails.getOrder().getFormatedNumber());

                    if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب") || status.equals("جاري تجهيز طلبك"))
                        tv_orderDetailsStatus.setText("جاري تجهيز طلبك");
                    else
                        tv_orderDetailsStatus.setText(orderDetails.getOrder().getStatus());

                    status = orderDetails.getOrder().getStatus();
                    if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب") || orderDetails.getOrder().getStatus().equals("جاري تجهيز طلبك")) {
                        cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.light_green_bg));
                    } else if (orderDetails.getOrder().getStatus().equals("ملغي")) {
                        cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.malghi_bg));
                    } else {
                        cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.order_states_bg));

                    }
                    for (int i = 0; i < orderDetails.getOrder().getProducts().size(); i++) {
                        totalPrice += orderDetails.getOrder().getProducts().get(i).getTotal();
                    }

                    tv_totalPrice.setText(orderDetails.getOrder().getPricing().getTotalWithCouponVat() + " ر.س");

                    one.setText("#" + orderDetails.getOrder().getFormatedNumber());
                    if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب") || status.equals("جاري تجهيز طلبك"))

                        two.setText("جاري تجهيز طلبك");
                    else
                        two.setText(orderDetails.getOrder().getStatus());

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

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("تم استلام الطلب") || status.equals("جاري تجهيز طلبك"))
                    dialog.show();
                else
                    Toast.makeText(getActivity(), "لا يمكن الغاء الطلب", Toast.LENGTH_SHORT).show();
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

                orderDetailsViewModel.cancelOrder(jsonObject, "Bearer " + Paper.book().read("token", "none"));

            }
        });

        return view;
    }
}