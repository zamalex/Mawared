package app.mawared.alhayat.home;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.mawared.alhayat.AddressModel;
import app.mawared.alhayat.AddressViewModel;
import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.MapsActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.helpers.EndlessRecyclerViewScrollListener;
import app.mawared.alhayat.home.model.CitiesModel;
import app.mawared.alhayat.home.model.Datum;
import app.mawared.alhayat.home.model.HomeProductModel;
import app.mawared.alhayat.home.model.HomeSliderModel;
import app.mawared.alhayat.home.model.MiniModel;
import app.mawared.alhayat.home.model.Product;
import app.mawared.alhayat.home.model.addmodel.AddCardModel;
import app.mawared.alhayat.home.model.checkrate.CheckRate;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.login.model.LoginResponse;
import app.mawared.alhayat.login.model.checkmodel.CheckCardModel;
import app.mawared.alhayat.login.model.newlogin.VerifyLoginResponse;
import app.mawared.alhayat.mycart.CartViewModel;
import app.mawared.alhayat.mycart.MyCartFragment;
import app.mawared.alhayat.mycart.model.CardModel;
import app.mawared.alhayat.orders.OrderFragment;
import app.mawared.alhayat.orders.model.AllOrder;
import app.mawared.alhayat.sendorder.AddressAdapter;
import app.mawared.alhayat.sendorder.SendOrderViewModel;
import app.mawared.alhayat.sendorder.newaddress.AddressNewResponse;
import app.mawared.alhayat.sendorder.newaddress.DataItem;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.paperdb.Paper;
import okhttp3.ResponseBody;


public class HomeFragment extends Fragment implements HomeAdapter.addListener, CitiesAdapter.onCity, AddressAdapter.AddressInterface {

    View v;
    RecyclerView recyclerView;
    HomeViewModel viewModel;
    CartViewModel cartViewModel;
    AddressViewModel addressViewModel;
    HomeAdapter adapter;
    EditText spinner;
    ImageView go_cart;
    SliderView flipper_layout;
    ArrayList<String> cities;
    ArrayList<String> cityIds;
    MutableLiveData<Integer> card_size = new MutableLiveData<>();
    TextView card_txt, linear_txt;
    ProgressBar progressBar_cyclic;
    LinearLayout card_linear;
    String card_id;

    Dialog dialog;
    Button q_btn;
    EditText q_et, et_city;

    ImageView show_map;

    SharedPreferences pref;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    TextView all;
    TextView selected_address_name;
    RecyclerView cities_rv;
    CitiesAdapter citiesAdapter;
    ReviewManager manager;
    ReviewInfo reviewInfo = null;
    String appPackageName = "app.mawared.alhayat"; // getPackageName() from Context or Activity object
    Long city_id = 0l;
    LatLng latLng;
    String lat="",lng="";
    BottomSheetDialog citiesDialog;
    GridLayoutManager gridLayoutManager;

    BottomSheetDialog address_dialog;
    AddressAdapter addressAdapter;

    RecyclerView address_rv;
    SendOrderViewModel sendOrderViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        city_id = 0l;
        v = inflater.inflate(R.layout.fragment_home, container, false);

        latLng = Paper.book().read("latlng",null);
        lat="";
        lng="";

        if (latLng!=null){
            lat = latLng.latitude+"";
            lng = latLng.longitude+"";
        }

        checkLocPermission();
        manager = ReviewManagerFactory.create(getActivity());
        manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    if (((MainActivity) getActivity()) != null) {
                        if (!((MainActivity) getActivity()).didShow) {

                            if (reviewInfo != null) {
                                manager.launchReviewFlow(getActivity(), reviewInfo).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void result) {
                                        if (getActivity()!=null)
                                            if (((MainActivity) getActivity()).didShow!=null)
                                        ((MainActivity) getActivity()).didShow = true;

                                    }
                                });
                            }
                        }
                    }

                }
            }
        });


        adapter = new HomeAdapter(getActivity(), this);


        citiesAdapter = new CitiesAdapter(this);

        dialog = new Dialog(getActivity());

        citiesDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        citiesDialog.setContentView(R.layout.cities_dialog);
        Window window3 = citiesDialog.getWindow();
        window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        citiesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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


        selected_address_name =v.findViewById(R.id.selected_address_name);


        cities_rv = citiesDialog.findViewById(R.id.rv_cities);
        et_city = citiesDialog.findViewById(R.id.et_city);
        all = citiesDialog.findViewById(R.id.all);

        cities_rv.setAdapter(citiesAdapter);

        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                citiesAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setText("الكل");
                city_id = 0l;
                citiesDialog.dismiss();
                ((MainActivity) getActivity()).showDialog(true);
                viewModel.getHomeProducts(card_id,lat,lng,1);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL);
        cities_rv.addItemDecoration(dividerItemDecoration);


        dialog.setContentView(R.layout.count_dialog);

        Window window1 = dialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        q_btn = dialog.findViewById(R.id.q_btn);
        q_et = dialog.findViewById(R.id.q_et);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);
        cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);
        addressViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AddressViewModel.class);
        sendOrderViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SendOrderViewModel.class);
        VerifyLoginResponse loginResponse = Paper.book().read("login", null);



        initAddresses();

        if (loginResponse!=null)
        sendOrderViewModel.getAddresses("Bearer "+loginResponse.getAccessToken());
        selected_address_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_dialog.show();
            }
        });

        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MapsActivity.class));
            }
        });


        AddressModel addressModel = Paper.book().read("address", null);
        card_id = Paper.book().read("cid", null);

        if (addressModel!=null){
            selected_address_name.setText(addressModel.getAddress());
            if (loginResponse!=null&&!addressModel.isAdded()){
                //add address
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("lat",addressModel.getLat());
                jsonObject.addProperty("lng",addressModel.getLng());
                if (addressModel.getMobile().isEmpty())
                    addressModel.setMobile(loginResponse.getUser().getPhone());

                jsonObject.addProperty("mobile",addressModel.getMobile());
                jsonObject.addProperty("address",addressModel.getAddress());
                if (addressModel.getUsername().isEmpty())
                    addressModel.setUsername(loginResponse.getUser().getName());
                jsonObject.addProperty("username",addressModel.getUsername());
                jsonObject.addProperty("set_default","1");
                jsonObject.addProperty("type",addressModel.getType());
                addressViewModel.addAddress(jsonObject);
            }
        }
        addressViewModel.addressResponse.observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if (responseBody!=null&&addressModel!=null){
                    addressModel.setAdded(true);
                    Paper.book().write("address",addressModel);
                    if (loginResponse!=null)
                    sendOrderViewModel.getAddresses("Bearer "+loginResponse.getAccessToken());
                }
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("ccc")) {
                    String ccc = Paper.book().read("cid", null);

                    viewModel.getHomeProducts(ccc,lat,lng,1);
                    //   Toast.makeText(getActivity(), "again", Toast.LENGTH_SHORT).show();

                }


            }
        };


        if (card_id != null && loginResponse != null)
            viewModel.bindUserCard(card_id, loginResponse.getUser().getId()+"");

        else if (loginResponse != null)
            viewModel.checkUserCart(loginResponse.getUser().

                    getId()+"");


        if (card_id != null)
            cartViewModel.getCard(card_id);
        viewModel.getMin();
        viewModel.getCities();
        viewModel.getHomeProducts(card_id,lat,lng,1);


        VerifyLoginResponse aloginResponse = Paper.book().read("login", null);
       if (aloginResponse!=null){
        if (aloginResponse.isSuccess()){
            ((MainActivity) getActivity()).showDialog(true);

            viewModel.checkRate(aloginResponse.getAccessToken()).observe(getViewLifecycleOwner(), new Observer<CheckRate>() {
                @Override
                public void onChanged(CheckRate checkRate) {
                    if (isAdded()){
                        if (getActivity()!=null)
                        ((MainActivity) getActivity()).showDialog(false);

                        if (checkRate!=null){
                        if (checkRate.getSuccess()){
                            if (checkRate.getData().getHasNewUpdates()){
                                OrderFragment orderFragment = new OrderFragment();
                                Bundle b = new Bundle();
                                b.putString("has","has");
                                orderFragment.setArguments(b);
                                ((MainActivity)getActivity()).fragmentStack.push(orderFragment);
                            }
                        }
                    }}
                }
            });}}

        card_size.setValue(0);

        viewModel.getHomeSlider();
        recyclerView = v.findViewById(R.id.home_p_rv);
        spinner = v.findViewById(R.id.city_spinner);
        spinner.setText("الكل");
        card_linear = v.findViewById(R.id.card_linear);
        linear_txt = v.findViewById(R.id.linear_txt);
        progressBar_cyclic = v.findViewById(R.id.progressBar_cyclic);
        card_txt = v.findViewById(R.id.card_txt);
        go_cart = v.findViewById(R.id.go_cart);
        flipper_layout = v.findViewById(R.id.flipper_layout);
        cities = new ArrayList<>();
        cityIds = new ArrayList<>();

        cities.add("الكل");
        cityIds.add("");

        gridLayoutManager = new

                GridLayoutManager(getActivity(), 2);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
            @Override
            public RecyclerView.LayoutManager getLayoutManager() {
                return gridLayoutManager;
            }

            @Override
            public void onLoadMore() {
              /*  if (pageNum >= 1) {
                    pageNum++;
                    viewModel.getHomeProducts(card_id,lat,lng,pageNum);

                }*/
            }
        });

        card_size.observe(

                getActivity(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        card_txt.setText(integer + "");

                        if (integer > 0)
                            card_txt.setVisibility(View.VISIBLE);
                        else
                            card_txt.setVisibility(View.GONE);


                    }
                });

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citiesDialog.show();
            }
        });

      /*  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                ((MainActivity) getActivity()).showDialog(true);

                if (position == 0)
                    viewModel.getHomeProducts(card_id);
                else
                    viewModel.filterByCity(cityIds.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
*/


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        viewModel.result.observe(

                getActivity(), new Observer<HomeProductModel>() {
                    @Override
                    public void onChanged(HomeProductModel homeProductModel) {
                        if (isAdded()) {
                            ((MainActivity) getActivity()).showDialog(false);

                            if (homeProductModel != null) {
                                adapter.setProducts((ArrayList<Product>) homeProductModel.getProducts());
                            }

                        }
                    }
                });
        viewModel.minimum.observe(

                getActivity(), new Observer<MiniModel>() {
                    @Override
                    public void onChanged(MiniModel miniModel) {


                        if (miniModel != null) {
                            if (miniModel.getStatus() == 200) {

                                Paper.book().write("min", miniModel.getData().getAmount());
                            }
                        }

                    }
                });

        viewModel.bindResponse.observe(

                getActivity(), new Observer<ResponseBody>() {
                    @Override
                    public void onChanged(ResponseBody responseBody) {
                        if (loginResponse != null)
                            viewModel.checkUserCart(loginResponse.getUser().getId()+"");
                    }
                });

        viewModel.filteredProducts.observe(

                getActivity(), new Observer<HomeProductModel>() {
                    @Override
                    public void onChanged(HomeProductModel homeProductModel) {
                        if (isAdded()) {

                            ((MainActivity) getActivity()).showDialog(false);
                            if (homeProductModel != null) {
                                if (homeProductModel.getStatus() != null) {
                                    if (homeProductModel.getStatus() == 200)
                                        adapter.setProducts((ArrayList<Product>) homeProductModel.getProducts());
                                }
                            } else
                                adapter.setProducts(new ArrayList<Product>());

                        }
                    }
                });
        viewModel.slider.observe(

                getActivity(), new Observer<HomeSliderModel>() {
                    @Override
                    public void onChanged(HomeSliderModel homeSliderModel) {
                        if (isAdded()) {
                            if (homeSliderModel != null) {
                                SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity());
                                flipper_layout.setSliderAdapter(sliderAdapterExample);
                                sliderAdapterExample.renewItems(homeSliderModel.getData());

                                flipper_layout.startAutoCycle();
                                flipper_layout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                flipper_layout.setIndicatorSelectedColor(Color.WHITE);
                                flipper_layout.setIndicatorUnselectedColor(Color.GRAY);
                                flipper_layout.setIndicatorVisibility(true);
                            }
                        }


                    }
                });


        viewModel.checkCardModelMutableLiveData.observe(

                getActivity(), new Observer<CheckCardModel>() {
                    @Override
                    public void onChanged(CheckCardModel checkCardModel) {


                        if (checkCardModel != null) {
                            if (checkCardModel.getStatus() == 200) {
                                if (checkCardModel.getData().getCartId() != null) {
                                    Paper.book().write("cid", checkCardModel.getData().getCartId().toString());
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("ccc", checkCardModel.getData().getCartId().toString());

                                    editor.apply();
                                }
                            }
                        }

                    }
                });


        go_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cid = Paper.book().read("cid", null);
                if (cid == null) {
                    Toast.makeText(getActivity(), "السلة فارغة", Toast.LENGTH_SHORT).show();
                } else
                    if (getActivity()!=null)
                    ((MainActivity) getActivity()).fragmentStack.push(new MyCartFragment());
            }
        });


        card_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cid = Paper.book().read("cid", null);
                if (cid == null) {
                    Toast.makeText(getActivity(), "السلة فارغة", Toast.LENGTH_SHORT).show();
                } else
                    if (getActivity()!=null)
                    ((MainActivity) getActivity()).fragmentStack.push(new MyCartFragment());
            }
        });


        addressViewModel.deleteAddressResponse.observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if (getActivity()!=null)
                ((MainActivity) getActivity()).showDialog(false);

                if (responseBody!=null){
                    if (loginResponse!=null)
                    sendOrderViewModel.getAddresses("Bearer "+loginResponse.getAccessToken());
                }

            }
        });


        viewModel.cities.observe(

                getActivity(), new Observer<CitiesModel>() {
                    @Override
                    public void onChanged(CitiesModel citiesModel) {
                        if (isAdded()) {
                            if (citiesModel != null) {
                               if (citiesModel.getStatusCode()!=null){
                                   if (citiesModel.getStatusCode() == 200) {
                                    /*
                                    for (Datum d : citiesModel.getData()) {
                                        cities.add(d.getName());
                                        cityIds.add(d.getId() + "");
                                    }*/
                                       citiesAdapter.setList((ArrayList<Datum>) citiesModel.getData());

                                   }
                               }

                            }
                            // ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                            // spinner.setAdapter(aarrdapter);

                        }
                    }
                });

        cartViewModel.cardModelMutableLiveData.observe(

                getViewLifecycleOwner(), new Observer<CardModel>() {
                    @Override
                    public void onChanged(CardModel cardModel) {
                        if (isAdded()) {
                            if (cardModel != null) {
                                if (cardModel.getStatus() == 200) {
                                    card_size.setValue(cardModel.getData().getItemsCount().intValue());
                                    if (cardModel.getData().getItemsCount() > 0) {
                                        card_linear.setVisibility(View.VISIBLE);

                                        linear_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format((cardModel.getData().getItemsSumFinalPrices())) + " ر.س ");



                                    } else
                                        card_linear.setVisibility(View.GONE);

                                }
                            }
                        }
                    }
                });


        cartViewModel.addResponse.observe(

                getActivity(), new Observer<AddCardModel>() {
                    @Override
                    public void onChanged(AddCardModel addCardModel) {
                        if (isAdded()) {
                            showCard(true);
                            card_size.setValue(addCardModel.getData().getItemsCount().intValue());
                            if (addCardModel.getData().getItemsCount() > 0) {
                                card_linear.setVisibility(View.VISIBLE);

                                linear_txt.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format((addCardModel.getData().getItemsSumFinalPrices())) + " ر.س ");

                            } else
                                card_linear.setVisibility(View.GONE);

                            Paper.book().write("cid", addCardModel.getData().getCartId().toString());

                        }
                    }
                });


        q_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q_et.getText().toString().isEmpty())
                    return;
                if (p == null )//|| Integer.parseInt(q_et.getText().toString()) == 0)
                    return;


                dialog.dismiss();

                card_id = Paper.book().read("cid", null);
                cartViewModel.addToCard(p.getId() + "", q_et.getText().toString(), null, card_id, "balance", p.getCity_id(),lat,lng);
                adapter.products.get(pos).qty = Integer.parseInt(q_et.getText().toString());
                adapter.products.get(pos).setIncart(Long.parseLong(q_et.getText().toString()));
                adapter.notifyDataSetChanged();
                q_et.setText("");


            }
        });

        return v;
    }

    private void initAddresses() {
        addressAdapter = new AddressAdapter(HomeFragment.this);

        address_dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        address_dialog.setContentView(R.layout.address_dialog);

        address_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                BottomSheetDialog dialogc = (BottomSheetDialog) dialog;

                dialogc.setCancelable(false);
                // When using AndroidX the resource can be found at com.google.android.material.R.id.design_bottom_sheet
                FrameLayout bottomSheet = dialogc.findViewById(R.id.design_bottom_sheet);

                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                setupFullHeight(bottomSheet);
                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        address_dialog.findViewById(R.id.xaddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_dialog.dismiss();
            }
        });

        address_rv = address_dialog.findViewById(R.id.address_rv);
        show_map = address_dialog.findViewById(R.id.show_map);
        address_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        address_rv.setAdapter(addressAdapter);

        sendOrderViewModel.addresses.observe(getActivity(), new Observer<AddressNewResponse>() {
            @Override
            public void onChanged(AddressNewResponse addressModel) {

                if (addressModel.getStatus() == 200) {
                    addressAdapter.setAddresses((ArrayList<DataItem>) addressModel.getData().getData());
                } else if (addressModel.getStatus() == 401) {
                    Toast.makeText(getActivity(), "session expired login again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
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
                            SmartLocation.with(getActivity()).location()
                                    //.oneFix()
                                    .start(new OnLocationUpdatedListener() {
                                        @Override
                                        public void onLocationUpdated(Location location) {

                                            try {
                                                System.out.println(getAddress(location));
                                            } catch (NullPointerException | IOException e){
                                                e.printStackTrace();

                                            }

                                        }
                                    });
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

        addresses = geocoder.getFromLocation(24.669820, 46.677190, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        System.out.println("city is "+city);
        String state = addresses.get(0).getAdminArea();
        System.out.println("state is "+state);

        String country = addresses.get(0).getCountryName();
        System.out.println("country is "+country);

        String postalCode = addresses.get(0).getPostalCode();
        System.out.println("postalCode is "+postalCode);

        String knownName = addresses.get(0).getFeatureName();
        System.out.println("knownName is "+knownName);



        return address;
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

    @Override
    public void onAddClick(int pos, Product product) {
        showCard(false);
        //Toast.makeText(getActivity(), "" + product.qty, Toast.LENGTH_SHORT).show();

        card_id = Paper.book().read("cid", null);

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "plus", product.getCity_id(),lat,lng);


    }

    @Override
    public void onDecreaseClick(int pos, Product product) {
        // Toast.makeText(getActivity(), "" + product.qty, Toast.LENGTH_SHORT).show();
        card_id = Paper.book().read("cid", null);

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "minus", product.getCity_id(),lat,lng);


    }

    Product p = null;
    int pos = 0;

    @Override
    public void setAmount(int position, Product product) {
        p = product;
        pos = position;
        dialog.show();

    }

    void showCard(boolean b) {
        if (!b) {
            go_cart.setVisibility(View.INVISIBLE);
            progressBar_cyclic.setVisibility(View.VISIBLE);
        } else {
            progressBar_cyclic.setVisibility(View.INVISIBLE);
            go_cart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        pref.registerOnSharedPreferenceChangeListener(listener);

        super.onResume();
    }

    @Override
    public void onPause() {
        pref.unregisterOnSharedPreferenceChangeListener(listener);

        super.onPause();
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }

    @Override
    public void onSelect(Datum city) {
        ((MainActivity) getActivity()).showDialog(true);

        city_id = city.getId();
        spinner.setText(city.getName());

        viewModel.filterByCity(city.getId().toString());

        citiesDialog.dismiss();

    }

    @Override
    public void setAddress(String type, DataItem address) {
        address_dialog.dismiss();
        VerifyLoginResponse loginResponse = Paper.book().read("login",null);
        AddressModel oldAddress = Paper.book().read("address",null);


        if (oldAddress!=null){
            if (oldAddress.city_id!=0){
                if (oldAddress.city_id!=address.getCity_id()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// Add the buttons
                    builder.setMessage("سيتم تغيير العنوان وحذف السلة الخاصة بك");
                    builder.setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            JsonObject body = new JsonObject();
                            body.addProperty("id",address.getId());
                            if (loginResponse!=null)
                                addressViewModel.setDefaultAddress(body, loginResponse.getAccessToken());

                            Paper.book().write("latlng",new LatLng(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng())));
                            Paper.book().write("address",new AddressModel(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng()),address.getMobile(),address.getAddress(),address.getUsername(),"personal",true,address.getCity_id()));

                            //  double lat, double lng, String mobile, String address, String username,String type, boolean isAdded)

                            if (getActivity()!=null){
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }
                    });
                    builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else {
                    JsonObject body = new JsonObject();
                    body.addProperty("id",address.getId());
                    if (loginResponse!=null)
                        addressViewModel.setDefaultAddress(body, loginResponse.getAccessToken());

                    Paper.book().write("latlng",new LatLng(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng())));
                    Paper.book().write("address",new AddressModel(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng()),address.getMobile(),address.getAddress(),address.getUsername(),"personal",true,address.getCity_id()));

                    //  double lat, double lng, String mobile, String address, String username,String type, boolean isAdded)

                    if (getActivity()!=null){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
            }else {
                JsonObject body = new JsonObject();
                body.addProperty("id",address.getId());
                if (loginResponse!=null)
                    addressViewModel.setDefaultAddress(body, loginResponse.getAccessToken());

                Paper.book().write("latlng",new LatLng(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng())));
                Paper.book().write("address",new AddressModel(Double.parseDouble(address.getLat()),Double.parseDouble(address.getLng()),address.getMobile(),address.getAddress(),address.getUsername(),"personal",true,address.getCity_id()));

                //  double lat, double lng, String mobile, String address, String username,String type, boolean isAdded)

                if (getActivity()!=null){
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
            }
        }



    }

    @Override
    public void onDelete(DataItem address) {
        if (getActivity()!=null)
        ((MainActivity) getActivity()).showDialog(true);

        address_dialog.dismiss();
        addressViewModel.delateAddress(address.getId());
    }
}