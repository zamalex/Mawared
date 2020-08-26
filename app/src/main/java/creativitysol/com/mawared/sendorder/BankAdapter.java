package creativitysol.com.mawared.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.Holder> {


    ArrayList<Product> products = new ArrayList<>();




    @NonNull
    @Override
    public BankAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new BankAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BankAdapter.Holder holder, final int position) {


    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return 5;
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);

        }
    }


}