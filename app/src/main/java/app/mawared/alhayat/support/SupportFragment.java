package app.mawared.alhayat.support;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.home.OrderViewModel;
import app.mawared.alhayat.orders.model.AllOrder;
import app.mawared.alhayat.support.chat.ChatViewModel;
import app.mawared.alhayat.support.chat.model.SendMsgModel;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class SupportFragment extends Fragment implements PickiTCallbacks {

    ArrayList<String> orders;
    View v;
    LinearLayout pick_btn;
    Spinner spinner;
    Button sendSupport;
    ImageView ic_BackBtn;
    EditText details_et;
    PickiT pickiT;
    ChatViewModel viewModel;
    OrderViewModel orderViewModel;
    String order_no = null;
    private static final int SELECT_IMAGE = 404;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_support, container, false);
        spinner = v.findViewById(R.id.order_spinner);
        ic_BackBtn = v.findViewById(R.id.ic_BackBtn);
        sendSupport = v.findViewById(R.id.sendSupport);
        pick_btn = v.findViewById(R.id.linearLayout);
        details_et = v.findViewById(R.id.editText);

        pickiT = new PickiT(getActivity(), this);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ChatViewModel.class);
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);

        orders = new ArrayList<>();
        orders.add("رقم الطلب");



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position==0){
                   order_no=null;
                }
                else if (orders.get(position).equals("اخرى")){
                    order_no=null;
                }else {
                    order_no=orders.get(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
               order_no = null;
                // your code here
            }

        });


        ((MainActivity)getActivity()).showDialog(true);

        orderViewModel.getAllOrders(1).observe(getActivity(), new Observer<AllOrder>() {
            @Override
            public void onChanged(AllOrder allOrder) {


                if (getActivity()!=null)
                ((MainActivity) getActivity()).showDialog(false);

                if (allOrder != null) {
                    if (allOrder.getOrders() != null && allOrder.getOrders().size() != 0) {

                        for (int i = 0; i < allOrder.getOrders().size(); i++) {
                            if (i < 10)
                                orders.add(allOrder.getOrders().get(i).getFormatedNumber() + "");
                        }

                    }
                }

                orders.add("اخرى");
                ArrayAdapter<String> aarrdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, orders);
                spinner.setAdapter(aarrdapter);
            }
        });

        sendSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details_et.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "ادخل التفاصيل", Toast.LENGTH_LONG).show();
                    return;
                }
                if (getActivity()!=null)
                ((MainActivity)getActivity()).showDialog(true);

                String title = "خدمة العملاء";
                if (order_no!=null)
                    title =  " خدمة العملاء لطلب "+order_no;
                viewModel.sendNsg(details_et.getText().toString(), null, order_no, title, "Bearer " + Paper.book().read("token", "none"));

            }
        });


        ic_BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null)
                ((MainActivity)getActivity()).onBackPressed();

            }
        });

        viewModel.sendMsgModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<SendMsgModel>() {
            @Override
            public void onChanged(SendMsgModel sendMsgModel) {
                ((MainActivity)getActivity()).showDialog(false);

                if (sendMsgModel != null) {
                    if (sendMsgModel.getSuccess()) {
                        Toast.makeText(getActivity(), "تم الارسال", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).fragmentStack.pop();
                    }
                }
            }
        });

        pick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        return v;
    }

    void checkPermission() {
        Dexter.withContext(getActivity())

                .withPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).onSameThread().check();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    // profile_iv.setImageBitmap(bitmap);

                    Uri uri = data.getData();

                    pickiT.getPath(uri, Build.VERSION.SDK_INT);


                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }


    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName().replaceAll("\\s+", ""), requestFile);

        // viewModel.uploadImage(PrefsUtils.getToken(MainActivity.this),body);

        Toast.makeText(getActivity(), "" + path, Toast.LENGTH_LONG).show();
    }

}