package creativitysol.com.mawared.orderdetails;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.orderdetails.model.OrderDetails;


public class OrderDetailsFragment extends Fragment {

    int orderId;
    OrderDetailsViewModel orderDetailsViewModel;
    RecyclerView rv_productDetails;
    OrderDetailsAdapter orderDetailsAdapter;
    TextView tv_orderDetailsNumber,tv_orderDetailsStatus,tv_totalPrice;
    ConstraintLayout cl_orderStatus;
    double totalPrice;
    ImageView ic_BackBtn;
    FragmentStack fragmentStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details_details, container, false);

        rv_productDetails = view.findViewById(R.id.rv_productDetails);
        cl_orderStatus = view.findViewById(R.id.cl_orderDetailsStatus);
        tv_orderDetailsNumber = view.findViewById(R.id.tv_orderDetailsNumber);
        tv_orderDetailsStatus = view.findViewById(R.id.tv_orderDetailsStatus);
        tv_totalPrice = view.findViewById(R.id.tv_totalPrice);
        ic_BackBtn = view.findViewById(R.id.ic_BackBtn);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_productDetails.setLayoutManager(gridLayoutManager);

        orderId = getActivity().getSharedPreferences("mwared", Context.MODE_PRIVATE).getInt("orderId",0);

        orderDetailsViewModel = new ViewModelProvider(OrderDetailsFragment.this).get(OrderDetailsViewModel.class);

        ((MainActivity)getActivity()).showDialog(true);

        orderDetailsViewModel.getOrderDetails(orderId).observe(getActivity(), new Observer<OrderDetails>() {
            @Override
            public void onChanged(OrderDetails orderDetails) {
                ((MainActivity)getActivity()).showDialog(false);

                if(orderDetails != null) {
                    orderDetailsAdapter = new OrderDetailsAdapter(orderDetails.getOrder().getProducts());
                    rv_productDetails.setAdapter(orderDetailsAdapter);
                    tv_orderDetailsNumber.setText("#تفاصيل طلب "+orderDetails.getOrder().getId());
                    tv_orderDetailsStatus.setText(orderDetails.getOrder().getStatus());

                    if (orderDetails.getOrder().getStatus().equals("تم استلام الطلب")){
                       cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.order_states_bg));
                    }else if(orderDetails.getOrder().getStatus().equals("ملغي")){
                        cl_orderStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.malghi_bg));
                    }
                    for (int i = 0; i < orderDetails.getOrder().getProducts().size(); i++) {
                        totalPrice += orderDetails.getOrder().getProducts().get(i).getTotal();
                    }

                    tv_totalPrice.setText(orderDetails.getOrder().getPricing().getTotal()+" ر.س");
                }
            }
        });

        ic_BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentStack = new FragmentStack(getActivity(),getActivity().getSupportFragmentManager(),R.id.main_container);
                fragmentStack.back();

            }
        });


        return view;
    }
}