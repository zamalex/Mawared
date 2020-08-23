package creativitysol.com.mawared.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaiselrahman.hintspinner.HintSpinner;
import com.jaiselrahman.hintspinner.HintSpinnerAdapter;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import creativitysol.com.mawared.MainActivity;
import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.HomeSliderModel;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.mycart.MyCartFragment;


public class HomeFragment extends Fragment {

    View v;
    RecyclerView recyclerView;
    HomeViewModel viewModel;
    HomeAdapter adapter;
    Spinner spinner;
    ImageView go_cart;
    SliderView flipper_layout;
   ArrayList<String> cities;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new HomeAdapter();

        viewModel =ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);



        viewModel.getHomeProducts();

        viewModel.getHomeSlider();
        recyclerView = v.findViewById(R.id.home_p_rv);
        spinner = v.findViewById(R.id.city_spinner);
        go_cart = v.findViewById(R.id.go_cart);
        flipper_layout = v.findViewById(R.id.flipper_layout);
        cities = new ArrayList<>();
        cities.add("الرياض");
        cities.add("تبوك");
        cities.add("جدة");
        ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);
        spinner.setAdapter(aarrdapter);
        viewModel.result.observe(getActivity(), new Observer<HomeProductModel>() {
            @Override
            public void onChanged(HomeProductModel homeProductModel) {
                if (isAdded()){
                    adapter.setProducts((ArrayList<Product>) homeProductModel.getProducts());

                }
            }
        });


        viewModel.slider.observe(getActivity(), new Observer<HomeSliderModel>() {
            @Override
            public void onChanged(HomeSliderModel homeSliderModel) {

                SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity());
                flipper_layout.setSliderAdapter(sliderAdapterExample);
                sliderAdapterExample.renewItems(homeSliderModel.getData());

                flipper_layout.startAutoCycle();
                flipper_layout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                flipper_layout.setIndicatorSelectedColor(Color.WHITE);
                flipper_layout.setIndicatorUnselectedColor(Color.GRAY);
            }
        });

        go_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentStack.push(new MyCartFragment());
            }
        });



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).bottomNavVisibility(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).bottomNavVisibility(false);
    }
}