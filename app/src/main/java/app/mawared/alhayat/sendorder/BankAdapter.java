package app.mawared.alhayat.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.mawared.alhayat.R;
import app.mawared.alhayat.sendorder.model.Bank;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.Holder> {


    ArrayList<Bank> banks = new ArrayList<>();


    BankInterface bankInterface;

    public BankAdapter(BankInterface bankInterface) {
        this.bankInterface = bankInterface;
    }

    @NonNull
    @Override
    public BankAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new BankAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BankAdapter.Holder holder, final int position) {
        Picasso.get().load(banks.get(position).getLogo()).fit().into(holder.img);

        if (banks.get(position).is==0){
            holder.img.setBackgroundResource(R.drawable.round_bank);
        }else {
            holder.img.setBackgroundResource(R.drawable.round_selected_bank);

        }


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

        ImageView img;
        public Holder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bankInterface.onBankSelected(banks.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }

    public interface BankInterface{
        void onBankSelected(Bank bank,int p);
    }

}