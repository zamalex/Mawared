package app.mawared.alhayat.orders;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.helpers.EndlessRecyclerViewScrollListener;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.helpers.OrderClickListener;
import app.mawared.alhayat.home.HomeViewModel;
import app.mawared.alhayat.home.OrderViewModel;
import app.mawared.alhayat.home.notifymodel.NotifyCountModel;
import app.mawared.alhayat.home.orderscount.OrdersCountModel;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.orderdetails.OrderDetailsFragment;
import app.mawared.alhayat.orders.model.AllOrder;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends Fragment implements OrderClickListener {

    RecyclerView rv_orders;
    OrdersAdapter ordersAdapter;
    int pageNum = 1;
    EditText et_searchOrder;
    FragmentStack fragmentStack;
    SharedPreferences.Editor orderIdPref;
    OrderViewModel orderViewModel;
    HomeViewModel homeViewModel;

    String token = Paper.book().read("token");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        rv_orders = view.findViewById(R.id.rv_orders);
        et_searchOrder = view.findViewById(R.id.et_searchOrder);
        orderIdPref = getActivity().getSharedPreferences("mwared", Context.MODE_PRIVATE).edit();
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

        rv_orders.setLayoutManager(gridLayoutManager);
        ordersAdapter = new OrdersAdapter(this);


        ((MainActivity) getActivity()).showDialog(true);
        orderViewModel.getAllOrders(pageNum).observe(getActivity(), new Observer<AllOrder>() {
            @Override
            public void onChanged(AllOrder allOrder) {
                ((MainActivity) getActivity()).showDialog(false);

                if (allOrder != null) {
                    if (allOrder.getStatus() == 401) {
                        Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;

                    }
                    if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {

                        ordersAdapter.setList(allOrder.getOrders());

                        if (getArguments()!=null)
                            if (getArguments().getString("has",null).equals("has")){
                                if (allOrder.getOrders().size()>0){

                                    getArguments().putString("has","clear");
                                    orderIdPref.putInt("orderId", allOrder.getOrders().get(0).getId()).apply();
                                    fragmentStack = new FragmentStack(getActivity(), getActivity().getSupportFragmentManager(), R.id.main_container);

                                    ((MainActivity)getActivity()).navigationView.setSelectedItemId(R.id.orders);
                                    fragmentStack.push(new OrderDetailsFragment());
                                }

                            }

                    }
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
                            if (allOrder != null) {
                                if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {
                                    ordersAdapter.setList(allOrder.getOrders());
                                }
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
                    if (et_searchOrder.getText().toString() != null && !et_searchOrder.getText().toString().equals("")) {
                        long orderId = Long.parseLong(et_searchOrder.getText().toString());

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
                    } else {
                        pageNum = 1;

                        orderViewModel.getAllOrders(pageNum).observe(getActivity(), new Observer<AllOrder>() {
                            @Override
                            public void onChanged(AllOrder allOrder) {
                                if (allOrder != null) {
                                    if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {

                                        ordersAdapter.setList(allOrder.getOrders());
                                        rv_orders.setAdapter(ordersAdapter);
                                    }
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
        orderIdPref.putInt("orderId", orderId).apply();
        fragmentStack = new FragmentStack(getActivity(), getActivity().getSupportFragmentManager(), R.id.main_container);
        fragmentStack.push(new OrderDetailsFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).bottomNavVisibility(true);

        String token = Paper.book().read("token", "none");

        if (!token.equals("none")) {
            homeViewModel.getNotifyCount("Bearer " + token);
            homeViewModel.getOrdersCount("Bearer " + token);

        }

        homeViewModel.notifyCount.observe(this, new Observer<NotifyCountModel>() {
            @Override
            public void onChanged(NotifyCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess()) {
                        if (notifyCountModel.getData().getUnread() > 0 && getActivity() != null)
                            ((MainActivity) getActivity()).navigationView.getOrCreateBadge(R.id.support).setNumber(Integer.parseInt(notifyCountModel.getData().getUnread().toString()));
                        else {
                            if (getActivity()!=null)
                            ((MainActivity) getActivity()).navigationView.removeBadge(R.id.support);
                        }
                    }
                }
            }
        });

        homeViewModel.ordersCount.observe(this, new Observer<OrdersCountModel>() {
            @Override
            public void onChanged(OrdersCountModel notifyCountModel) {
                if (notifyCountModel != null) {
                    if (notifyCountModel.getSuccess() && getActivity() != null) {
                        if (notifyCountModel.getData().getHasNewUpdates() && getActivity() != null)
                            ((MainActivity) getActivity()).navigationView.getOrCreateBadge(R.id.orders).setNumber(notifyCountModel.getData().getCount());
                        else {
                            if (getActivity() != null)
                                ((MainActivity) getActivity()).navigationView.removeBadge(R.id.orders);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).bottomNavVisibility(false);
    }
}