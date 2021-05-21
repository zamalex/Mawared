package app.mawared.alhayat.mycart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.mawared.alhayat.SwipeHelper;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.home.model.addmodel.AddCardModel;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.mycart.model.CardModel;
import app.mawared.alhayat.mycart.model.Product;
import app.mawared.alhayat.sendorder.SendOrdersFragment;
import io.paperdb.Paper;
import okhttp3.ResponseBody;


public class MyCartFragment extends Fragment implements MyCartAdapter.sumListener, MyCartAdapter.UpdateListener {


    RecyclerView cartRv;
    View v;
    CartViewModel viewModel;
    TextView total_sum;
    MyCartAdapter adapter;
    CircularProgressButton next;
    ImageView back;
    String cid;
    Dialog dialog;

    EditText q_et;
    Button q_btn;

    Product p = null;

    public boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_cart, container, false);
        back = v.findViewById(R.id.imageView);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CartViewModel.class);

        adapter = new MyCartAdapter(this, this);
        dialog= new Dialog(getActivity());

        dialog.setContentView(R.layout.count_dialog);

        Window window1 = dialog.getWindow();
        window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        q_btn = dialog.findViewById(R.id.q_btn);
        q_et = dialog.findViewById(R.id.q_et);

        cartRv = v.findViewById(R.id.cart_rv);
        total_sum = v.findViewById(R.id.total_sum);
        next = v.findViewById(R.id.btn_next);
        next.setBackgroundResource(R.drawable.next_radius);




        cartRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartRv.setAdapter(adapter);


        cid = Paper.book().read("cid");
        // ((MainActivity)getActivity()).showDialog(true);
      //  next.startAnimation();
        viewModel.getCard(cid + "");


        viewModel.cardModelMutableLiveData.observe(getActivity(), new Observer<CardModel>() {
            @Override
            public void onChanged(CardModel cardModel) {
                //  ((MainActivity)getActivity()).showDialog(false);
                isLoading = false;

                next.revertAnimation();
                next.setBackgroundResource(R.drawable.next_radius);

                if (cardModel != null) {
                    if (cardModel.getStatus() == 200) {
                        adapter.setProducts((ArrayList<Product>) cardModel.getData().getProducts());
                        total_sum.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(cardModel.getData().getItemsSumFinalPrices()) + " ر.س");

                    }
                }


            }
        });

        SwipeHelper swipeHelper = new SwipeHelper(getActivity(), cartRv) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        getActivity(),
                        "إزالة",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                               // Toast.makeText(getActivity(), ""+pos, Toast.LENGTH_SHORT).show();


                                new AlertDialog.Builder(getActivity())
                                        .setTitle("حذف المنتج")
                                        .setMessage("هل انت متأكد من حذف المنتج؟")
                                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                viewModel.removeFromCard(cid + "", adapter.products.get(pos).getId().toString());
                                                next.startAnimation();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info  )
                                        .show();
                            }
                        }
                ));


            }
        };

        viewModel.addResponse.observe(getActivity(), new Observer<AddCardModel>() {
            @Override
            public void onChanged(AddCardModel responseBody) {
                viewModel.getCard(cid + "");

            }
        });

        viewModel.removeResponse.observe(getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {

                viewModel.getCard(cid + "");
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Paper.book().read("token", "none").equals("none")) {
                    if (viewModel.cardModelMutableLiveData.getValue()==null){
                            return;
                    }
                    if (viewModel.cardModelMutableLiveData.getValue()==null)
                        return;
                    if (viewModel.cardModelMutableLiveData.getValue().getData()==null)
                        return;
                    if (viewModel.cardModelMutableLiveData.getValue().getData().getProducts()==null)
                        return;

                    if (viewModel.cardModelMutableLiveData.getValue().getData().getProducts().size() == 0) {
                        Toast.makeText(getActivity(), "السلة فارغة", Toast.LENGTH_SHORT).show();
                        return;
                    }
                 /*   for (Product p : viewModel.cardModelMutableLiveData.getValue().getData().getProducts()){
                        if (p.getInCartQuantity()<10){
                            Toast.makeText(getActivity(), "اقل قيمة للطلب 10 قطع", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                   */
                    if (viewModel.cardModelMutableLiveData.getValue().getData().getItemsCount()<10)
                    {
                        Toast.makeText(getActivity(), "الحد الأدنى للطلب 10 منتجات او كراتين", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((MainActivity) getActivity()).showDialog(true);

                    SendOrdersFragment sendOrdersFragment = new SendOrdersFragment();

                    Bundle b = new Bundle();
                    b.putParcelableArrayList("clist", (ArrayList<? extends Parcelable>) viewModel.cardModelMutableLiveData.getValue().getData().getProducts());
                    b.putDouble("total", viewModel.cardModelMutableLiveData.getValue().getData().getItemsSumFinalPrices());
                    sendOrdersFragment.setArguments(b);

                    ((MainActivity) getActivity()).fragmentStack.push(sendOrdersFragment);
                }
                else {
                    Toast.makeText(getActivity(), "سجل الدخول اولا", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();

            }
        });


        q_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q_et.getText().toString().isEmpty())
                    return;
                if (p==null)//||Integer.parseInt(q_et.getText().toString())==0)
                    return;


                dialog.dismiss();

               // next.startAnimation();
                isLoading = true;
                viewModel.addToCard(p.getId() + "", q_et.getText().toString(), null, cid, "balance",p.getCity_id());
                q_et.setText("");


            }
        });


        return v;
    }

    @Override
    public void doSum(Double sum) {
        //total_sum.setText((Double) (Math.round(sum * 100) / 100.00) + " ر.س");

    }

    @Override
    public void increase(Product item, int qty) {
      //  next.startAnimation();
        isLoading = true;
        viewModel.addToCard(item.getId() + "", qty+"", null, cid, "balance",item.getCity_id());

    }

    @Override
    public void decrease(Product item, int qty) {
        isLoading = true;

      //  next.startAnimation();
        if (qty == 0)
            viewModel.removeFromCard(cid + "", item.getId().toString());
        else
            viewModel.addToCard(item.getId() + "", ""+qty, null, cid, "balance",item.getCity_id());

    }

    @Override
    public void setAmount(Product item) {
        p=item;

        dialog.show();
    }

}