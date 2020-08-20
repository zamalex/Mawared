package creativitysol.com.mawared.home;

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
import android.widget.Toast;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.HomeProductModel;
import creativitysol.com.mawared.home.model.Product;


public class HomeFragment extends Fragment {

    View v;
    RecyclerView recyclerView;
    HomeViewModel viewModel;
    HomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new HomeAdapter();

        viewModel =ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);


        viewModel.getHomeProducts();

        recyclerView = v.findViewById(R.id.home_p_rv);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);

        viewModel.result.observe(getActivity(), new Observer<HomeProductModel>() {
            @Override
            public void onChanged(HomeProductModel homeProductModel) {
                adapter.setProducts((ArrayList<Product>) homeProductModel.getProducts());
                Toast.makeText(getActivity(), ""+homeProductModel.getProducts().size(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}