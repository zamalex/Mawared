package app.mawared.alhayat.home;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.api.RetrofitClient;
import app.mawared.alhayat.home.model.NotifyAvailable;
import app.mawared.alhayat.home.model.addmodel.AddCardModel;
import app.mawared.alhayat.home.model.prodetails.Product;
import app.mawared.alhayat.home.model.prodetails.ProductDetails;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.mycart.CartViewModel;
import app.mawared.alhayat.mycart.MyCartFragment;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsFragment extends Fragment {
    TextView name, price, total_qty, offer_txt,product_offer_price;
    ImageView img, offer_img;
    ImageButton add, increase, decrease;
    LinearLayout quantityLayout;

    View itemView;
    String product_id = null;

    Button go_cart;
    Button send_not;

    Product product = null;
    CartViewModel cartViewModel;
    HomeViewModel homeViewModel;

    String card_id = null;
    String city_id = null;

    LatLng latLng;
    String lat="",lng="";
    VerifyLoginResponse verifyLoginResponse = Paper.book().read("login",null);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);

        latLng = Paper.book().read("latlng",null);
        lat="";
        lng="";

        if (latLng!=null){
            lat = latLng.latitude+"";
            lng = latLng.longitude+"";
        }
        if (verifyLoginResponse==null){
            send_not.setVisibility(View.GONE);
        }
        itemView = inflater.inflate(R.layout.fragment_product_details, container, false);
        initView();

        go_cart = itemView.findViewById(R.id.go_cart);
        send_not = itemView.findViewById(R.id.send_not);

        itemView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).onBackPressed();
            }
        });

        go_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).fragmentStack.push(new MyCartFragment());
            }
        });

        send_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_id!=null){
                    homeViewModel.notifyAvailable("Bearer "+verifyLoginResponse.getAccessToken(),product_id);
                }
            }
        });

        homeViewModel.notifyAvailable.observe(getViewLifecycleOwner(), new Observer<NotifyAvailable>() {
            @Override
            public void onChanged(NotifyAvailable notifyAvailable) {
                if (notifyAvailable!=null){
                    Toast.makeText(getActivity(), notifyAvailable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle bb = new Bundle();
        bb.putString("screen", "Product details screen Android");
        mFirebaseAnalytics.logEvent("user_location", bb);

        Map<String,Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.DESCRIPTION, "Product_details_screen_Android");

        AppsFlyerLib.getInstance().logEvent(getActivity(), "product_details",eventValues, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("flyer", "Event sent successfully");
            }
            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("flyer", "Event failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });


        assert getArguments() != null;
        product_id = getArguments().getString("product", null);
        assert getArguments() != null;
        city_id = getArguments().getString("city", null);

        card_id = Paper.book().read("cid", null);

        cartViewModel.addResponse.observe(

                getActivity(), new Observer<AddCardModel>() {
                    @Override
                    public void onChanged(AddCardModel addCardModel) {
                        if (isAdded()) {
                            if (addCardModel != null)
                                if (addCardModel.getSuccess())

                                    Paper.book().write("cid", addCardModel.getData().getCartId().toString());

                        }
                    }
                });

        RetrofitClient.getApiInterface().getProDetails(product_id, card_id, city_id).enqueue(new Callback<ProductDetails>() {
            @Override
            public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    product = response.body().getProduct();

                    if (product != null) {
                        if (!product.getAvailable()&&verifyLoginResponse!=null)
                            send_not.setVisibility(View.VISIBLE);
                        Double p = Double.parseDouble(product.getPrice().toString()) + (Double.parseDouble(product.getVat().toString()) / 100 * Double.parseDouble(product.getPrice().toString()));
                        Double pp = Double.parseDouble(product.getOld_price().toString());
/*
                        if (product.getInCartQuantity()!=null)
                            if (product.getInCartQuantity()==0)
                                product.qty=Integer.parseInt(product.getIncart().toString());
*/

                        price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(p) + " " + "ر.س");
                        product_offer_price.setPaintFlags(product_offer_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        if (product.getOld_price()!=0)
                            product_offer_price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(pp) + " " + "ر.س");
                        name.setText(product.getTitle());
                        total_qty.setText(product.getInCartQuantity() + "");



                        if (product.getInCartQuantity() == 0) {
                            add.setVisibility(View.VISIBLE);
                            quantityLayout.setVisibility(View.GONE);
                        } else {

                            add.setVisibility(View.GONE);
                            quantityLayout.setVisibility(View.VISIBLE);

                        }

                        offer_txt.setText("");
                        if (product.getHasOffer() || product.getOffer().isEmpty() || product.getOffer().replace(" ", "").equals("1+0")) {
                            offer_txt.setVisibility(View.GONE);
                            offer_img.setVisibility(View.GONE);

                        } else {
                            offer_txt.setVisibility(View.VISIBLE);
                            offer_img.setVisibility(View.VISIBLE);


                            String g = product.getOffer();

                            char[] c_arr = g.toCharArray();
                            for (int i = 0; i < c_arr.length; i++) {
                                if (c_arr[i] != ' ') {
                                    offer_txt.append(c_arr[i] + "");
                                    offer_txt.append("\n");
                                }

                            }

                        }


                        Picasso.get().load(product.getImg()).fit().into(img);
                        Log.d("imgg", product.getImg());


                        ////////////////////////////////////////
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!product.getAvailable()) {
                                    Toast.makeText(getActivity(), "المنتج غير متوفر حاليا", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                product.setInCartQuantity(product.getInCartQuantity() + 1);
                                total_qty.setText(product.getInCartQuantity() + "");
                                add.setVisibility(View.GONE);
                                quantityLayout.setVisibility(View.VISIBLE);
                                onAddClick(0, product);
                            }
                        });

                        increase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!product.getAvailable()) {
                                    Toast.makeText(getActivity(), "المنتج غير متوفر حاليا", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                product.setInCartQuantity(product.getInCartQuantity() + 1);
                                total_qty.setText(product.getInCartQuantity() + "");


                                onAddClick(0, product);

                            }
                        });


                        decrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                product.setInCartQuantity(product.getInCartQuantity() + -1);

                                if (product.getInCartQuantity() != 0) {

                                    total_qty.setText(product.getInCartQuantity() + "");

                                } else {
                                    product.setInCartQuantity(0l);
                                    add.setVisibility(View.VISIBLE);
                                    quantityLayout.setVisibility(View.GONE);
                                }

                                onDecreaseClick(0, product);

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDetails> call, Throwable t) {

            }
        });
        return itemView;
    }

    void initView() {
        name = itemView.findViewById(R.id.product_name);
        // name.setSelected(true);
        price = itemView.findViewById(R.id.product_price);
        product_offer_price = itemView.findViewById(R.id.product_offer_price);
        img = itemView.findViewById(R.id.product_img);
        add = itemView.findViewById(R.id.add);
        quantityLayout = itemView.findViewById(R.id.quantity_layout);
        total_qty = itemView.findViewById(R.id.total_qty);
        increase = itemView.findViewById(R.id.increase);
        decrease = itemView.findViewById(R.id.decrease);
        offer_img = itemView.findViewById(R.id.off_img);
        offer_txt = itemView.findViewById(R.id.txt_offer);
    }

    public void onAddClick(int pos, Product product) {
        // showCard(false);
        //Toast.makeText(getActivity(), "" + product.qty, Toast.LENGTH_SHORT).show();

        card_id = Paper.book().read("cid", null);

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "plus", product.getCityId(),lat,lng);


    }

    public void onDecreaseClick(int pos, Product product) {
        // Toast.makeText(getActivity(), "" + product.qty, Toast.LENGTH_SHORT).show();
        card_id = Paper.book().read("cid", null);

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "minus", product.getCityId(),lat,lng);


    }
}