package app.mawared.alhayat.contactus;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.contactus.model.ContactUsResponse;
import app.mawared.alhayat.helpers.FragmentStack;


public class ContactUsFragment extends Fragment {


    View view;
    ContactUsViewModel contactUsViewModel;
    EditText et_MessageTitle,et_MessageContent;
    ConstraintLayout btn_sendMsg;
    ImageView iv_backCBtnFromContactUs;
    FragmentStack fragmentStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        et_MessageTitle = view.findViewById(R.id.et_MessageTitle);
        et_MessageContent = view.findViewById(R.id.et_MessageContent);
        btn_sendMsg = view.findViewById(R.id.btn_sendMsg);
        iv_backCBtnFromContactUs = view.findViewById(R.id.iv_backCBtnFromContactUs);

        contactUsViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);

        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_MessageTitle.getText().toString().isEmpty() && !et_MessageContent.getText().toString().isEmpty()){
                    ((MainActivity)getActivity()).showDialog(true);
                    contactUsViewModel.contactUs(et_MessageTitle.getText().toString(),et_MessageContent.getText().toString())
                            .observe(getActivity(), new Observer<ContactUsResponse>() {
                                @Override
                                public void onChanged(ContactUsResponse contactUsResponse) {
                                    ((MainActivity)getActivity()).showDialog(false);

                                    if (contactUsResponse!=null){
                                        if (contactUsResponse.getStatus() == 200){
                                            Toast.makeText(getActivity(),contactUsResponse.getMessage().getDescription(),Toast.LENGTH_LONG).show();
                                        }
                                    }else
                                        Toast.makeText(getActivity(),"حدث خطأ",Toast.LENGTH_LONG).show();


                                }
                            });
                }else {
                    //Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();

                }
            }
        });

        iv_backCBtnFromContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentStack = new FragmentStack(getActivity(),getActivity().getSupportFragmentManager(),R.id.main_container);
                fragmentStack.back();
            }
        });
        return view;
    }
}