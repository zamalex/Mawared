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
import android.widget.TextView;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.about.aboutus.AboutUsModel;
import app.mawared.alhayat.about.model.SocialsModel;
import app.mawared.alhayat.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutMawaredFragment extends Fragment {


    View v;
    SocialViewModel viewModel;
    RecyclerView recyclerView;
    ImageView x_back;
    AboutAdapter adapter;
    TextView atitle,atext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v=inflater.inflate(R.layout.fragment_about_mawared, container, false);

       recyclerView = v.findViewById(R.id.rv_socials);
       atitle = v.findViewById(R.id.atitle);
        atext = v.findViewById(R.id.atext);
       recyclerView = v.findViewById(R.id.rv_socials);
       x_back = v.findViewById(R.id.imageView);
       viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SocialViewModel.class);

       adapter = new AboutAdapter(getActivity());

       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
       viewModel.getSocials();
       getAboutUs();

       viewModel.socialRes.observe(getActivity(), new Observer<SocialsModel>() {
           @Override
           public void onChanged(SocialsModel socialsModel) {
               if (isAdded()){
                   if (socialsModel.getSuccess()){
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

    void getAboutUs(){
        RetrofitClient.getApiInterface().getAbout().enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.isSuccessful()){
                    AboutUsModel aboutUsModel = response.body();
                    if (aboutUsModel!=null){
                        if (aboutUsModel.isSuccess()){
                            atitle.setText(aboutUsModel.getData().getTitle());
                            atext.setText(aboutUsModel.getData().getContent());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {

            }
        });
    }
}