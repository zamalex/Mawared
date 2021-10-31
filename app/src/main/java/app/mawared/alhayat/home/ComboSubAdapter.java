package app.mawared.alhayat.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import app.mawared.alhayat.R;
import app.mawared.alhayat.home.model.subproducts.SubProductsItem;


public class ComboSubAdapter extends RecyclerView.Adapter<ComboSubAdapter.ComboSubViewHolder>{

    ArrayList<SubProductsItem> products = new ArrayList<>();
    public void setProducts(ArrayList<SubProductsItem> products) {
        this.products=products;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ComboSubViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_combo, parent, false);
        return new ComboSubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ComboSubViewHolder holder, int position) {
        SubProductsItem item = products.get(position);

        holder.i_name.setText(item.getTitle());
        holder.i_price.setText(new DecimalFormat("0.00",new DecimalFormatSymbols(Locale.US)).format(item.getPrice()) + " " + "ر.س");
        holder.i_count.setText("x"+item.getQuantity());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ComboSubViewHolder extends RecyclerView.ViewHolder {
        TextView i_count,i_price,i_name;
        public ComboSubViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            i_name = itemView.findViewById(R.id.i_name);
            i_price = itemView.findViewById(R.id.i_price);
            i_count = itemView.findViewById(R.id.i_count);
        }
    }
}
