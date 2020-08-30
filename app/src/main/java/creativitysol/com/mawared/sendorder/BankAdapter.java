package creativitysol.com.mawared.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.home.model.Product;
import creativitysol.com.mawared.sendorder.model.Bank;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.Holder> {


    ArrayList<Bank> banks = new ArrayList<>();




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

    public void setBanks(ArrayList<Bank> banks) {
        this.banks = banks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return banks.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);

        }
    }


}