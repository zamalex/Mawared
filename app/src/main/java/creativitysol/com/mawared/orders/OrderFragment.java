package creativitysol.com.mawared.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.helpers.EndlessRecyclerViewScrollListener;
import creativitysol.com.mawared.helpers.FragmentStack;
import creativitysol.com.mawared.helpers.OrderClickListener;
import creativitysol.com.mawared.home.OrderViewModel;
import creativitysol.com.mawared.orderdetails.OrderDetailsFragment;
import creativitysol.com.mawared.orders.model.AllOrder;


public class OrderFragment extends Fragment implements OrderClickListener {

    RecyclerView rv_orders;
    OrdersAdapter ordersAdapter;
    int pageNum = 1;
    EditText et_searchOrder;
    FragmentStack fragmentStack;
    SharedPreferences.Editor orderIdPref;
    OrderViewModel orderViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        rv_orders = view.findViewById(R.id.rv_orders);
        et_searchOrder = view.findViewById(R.id.et_searchOrder);
        orderIdPref = getActivity().getSharedPreferences("mwared", Context.MODE_PRIVATE).edit();
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        rv_orders.setLayoutManager(gridLayoutManager);
        ordersAdapter = new OrdersAdapter(this);

        orderViewModel.getAllOrders(pageNum).observe(getActivity(), new Observer<AllOrder>() {
            @Override
            public void onChanged(AllOrder allOrder) {
                if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {

                    ordersAdapter.setList(allOrder.getOrders());
                }
            }
        });

        rv_orders.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
            @Override
            public RecyclerView.LayoutManager getLayoutManager() {
                return gridLayoutManager;
            }

            @Override
            public void onLoadMore() {
                if (pageNum >= 1) {
                    pageNum++;
                    orderViewModel.getAllOrders(pageNum).observe(getActivity(), new Observer<AllOrder>() {
                        @Override
                        public void onChanged(AllOrder allOrder) {
                            if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {
                                ordersAdapter.setList(allOrder.getOrders());
                            }
                        }
                    });
                }
            }
        });

        et_searchOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if(et_searchOrder.getText().toString() != null && !et_searchOrder.getText().toString().equals("")) {
                        int orderId = Integer.parseInt(et_searchOrder.getText().toString());

                        orderViewModel.searchOrder(orderId).observe(getActivity(), new Observer<AllOrder>() {
                            @Override
                            public void onChanged(AllOrder allOrder) {
                                if (allOrder != null) {
                                   OrdersAdapter orderAdapter = new OrdersAdapter(OrderFragment.this);
                                    orderAdapter.setList(allOrder.getOrders());
                                    rv_orders.setAdapter(orderAdapter);
                                }
                            }
                        });
                    }else {
                        pageNum = 1;

                        orderViewModel.getAllOrders(pageNum).observe(getActivity(), new Observer<AllOrder>() {
                            @Override
                            public void onChanged(AllOrder allOrder) {
                                if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {

                                    ordersAdapter.setList(allOrder.getOrders());
                                    rv_orders.setAdapter(ordersAdapter);
                                }
                            }
                        });
                    }


                }
                return false;
            }
        });


        rv_orders.setAdapter(ordersAdapter);
        return view;
    }

    @Override
    public void onClickPressed(int orderId) {
        pageNum = 1;
        orderIdPref.putInt("orderId",orderId).apply();
        fragmentStack = new FragmentStack(getActivity(),getActivity().getSupportFragmentManager(),R.id.main_container);
        fragmentStack.push(new OrderDetailsFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).bottomNavVisibility(false);
    }
}