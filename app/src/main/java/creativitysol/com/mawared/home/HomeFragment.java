package creativitysol.com.mawared.home;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiselrahman.hintspinner.HintSpinner;
import com.jaiselrahman.hintspinner.HintSpinnerAdapter;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.CitiesModel;
import creativitysol.com.mawared.home.model.Datum;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.MiniModel;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.home.model.addmodel.AddCardModel;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.login.model.checkmodel.CheckCardModel;
import creativitysol.com.mawared.mycart.CartViewModel;
import creativitysol.com.mawared.mycart.MyCartFragment;
import creativitysol.com.mawared.mycart.model.CardModel;
import io.paperdb.Paper;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements HomeAdapter.addListener {

    View v;
    RecyclerView recyclerView;
    HomeViewModel viewModel;
    CartViewModel cartViewModel;
    HomeAdapter adapter;
    Spinner spinner;
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
    EditText q_et;

    SharedPreferences pref;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new HomeAdapter(getActivity(), this);
        dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.count_dialog);

        Window window1 = dialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        q_btn = dialog.findViewById(R.id.q_btn);
        q_et = dialog.findViewById(R.id.q_et);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);
        cartViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);

        LoginResponse loginResponse = Paper.book().read("login", null);
        card_id = Paper.book().read("cid", null);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

   listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals("ccc"))
                    viewModel.getHomeProducts();
                Toast.makeText(getActivity(), "again", Toast.LENGTH_SHORT).show();

            }
        };




        if (card_id != null && loginResponse != null)
            viewModel.bindUserCard(card_id, loginResponse.getUser().

                    getId().

                    toString());

        else if (loginResponse != null)
            viewModel.checkUserCart(loginResponse.getUser().

                    getId().

                    toString());


        if (card_id != null)
            cartViewModel.getCard(card_id);
        viewModel.getMin();
        viewModel.getCities();
        viewModel.getHomeProducts();

        card_size.setValue(0);

        viewModel.getHomeSlider();
        recyclerView = v.findViewById(R.id.home_p_rv);
        spinner = v.findViewById(R.id.city_spinner);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                ((MainActivity) getActivity()).showDialog(true);

                if (position == 0)
                    viewModel.getHomeProducts();
                else
                    viewModel.filterByCity(cityIds.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        recyclerView.setLayoutManager(new

                GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        viewModel.result.observe(

                getActivity(), new Observer<HomeProductModel>() {
                    @Override
                    public void onChanged(HomeProductModel homeProductModel) {
                        if (isAdded()) {
                            ((MainActivity) getActivity()).showDialog(false);

                            if (homeProductModel != null)
                                adapter.setProducts((ArrayList<Product>) homeProductModel.getProducts());

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
                            viewModel.checkUserCart(loginResponse.getUser().getId().toString());
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
                                if (checkCardModel.getData().getCartId() != null){
                                    Paper.book().write("cid", checkCardModel.getData().getCartId().toString());
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("ccc",checkCardModel.getData().getCartId().toString());

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
                    ((MainActivity) getActivity()).fragmentStack.push(new MyCartFragment());
            }
        });


        viewModel.cities.observe(

                getActivity(), new Observer<CitiesModel>() {
                    @Override
                    public void onChanged(CitiesModel citiesModel) {
                        if (isAdded()) {
                            if (citiesModel != null) {
                                if (citiesModel.getStatusCode() == 200) {
                                    for (Datum d : citiesModel.getData()) {
                                        cities.add(d.getName());
                                        cityIds.add(d.getId() + "");
                                    }

                                }

                            }
                            ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                            spinner.setAdapter(aarrdapter);

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

                                        linear_txt.setText((Double) (Math.round((cardModel.getData().getItemsSumFinalPrices()) * 100) / 100.00) + " ر.س ");
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
                                linear_txt.setText((Double) (Math.round((addCardModel.getData().getItemsSumFinalPrices()) * 100) / 100.00) + " ر.س ");
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
                if (p == null || Integer.parseInt(q_et.getText().toString()) == 0)
                    return;


                dialog.dismiss();

                card_id = Paper.book().read("cid", null);
                cartViewModel.addToCard(p.getId() + "", q_et.getText().toString(), null, card_id, "plus");
                adapter.products.get(pos).qty = Integer.parseInt(q_et.getText().toString());
                adapter.notifyDataSetChanged();
                q_et.setText("");


            }
        });

        return v;
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

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "plus");




    }

    @Override
    public void onDecreaseClick(int pos, Product product) {
        // Toast.makeText(getActivity(), "" + product.qty, Toast.LENGTH_SHORT).show();
        card_id = Paper.book().read("cid", null);

        cartViewModel.addToCard(product.getId() + "", "1", null, card_id, "minus");



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
}