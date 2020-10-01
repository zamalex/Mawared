package app.mawared.alhayat.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.about.model.SocialsModel;


public class AboutMawaredFragment extends Fragment {


    View v;
    SocialViewModel viewModel;
    RecyclerView recyclerView;
    ImageView x_back;
    AboutAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v=inflater.inflate(R.layout.fragment_about_mawared, container, false);

       recyclerView = v.findViewById(R.id.rv_socials);
       x_back = v.findViewById(R.id.imageView);
       viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SocialViewModel.class);

       adapter = new AboutAdapter(getActivity());

       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
       viewModel.getSocials();


       viewModel.socialRes.observe(getActivity(), new Observer<SocialsModel>() {
           @Override
           public void onChanged(SocialsModel socialsModel) {
               if (isAdded()){
                   if (socialsModel.getStatus()==200){
                       recyclerView.setAdapter(adapter);
                       adapter.setBanks(socialsModel);
                   }
               }
           }
       });

       x_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((MainActivity)getActivity()).onBackPressed();
           }
       });

        return v;
    }
}