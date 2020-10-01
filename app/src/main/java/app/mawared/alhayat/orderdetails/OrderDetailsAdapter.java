package app.mawared.alhayat.orderdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.mawared.alhayat.R;
import app.mawared.alhayat.orderdetails.model.Product;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.productHolder> {

    List<Product> productList;

    public OrderDetailsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_order_items, parent, false);
        return new productHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull productHolder holder, int position) {
        Product productModel = productList.get(position);
        holder.tv_productCountDetails.setText("x"+productModel.getQuantity());
        holder.tv_productDetailsName.setText(productModel.getTitle());
        holder.tv_productDetailsPrice.setText(productModel.getTotal()+" ر.س");


        String c = "\\u002B";
        if (productModel.isHasOffer() == 1) {
            if (productModel.getOffer() != null)
                if (!productModel.getOffer().isEmpty()) {
                    String o = productModel.getOffer().replace(" ", "");
                    String[] parts = o.split(c);
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);

                    if (productModel.getQuantity() >= part1 && part2 > 0) {
                        holder.offer_c.setVisibility(View.VISIBLE);
                        holder.tv_productDetailsName2.setText(productModel.getTitle());

                        int q = Integer.parseInt(productModel.getQuantity() + "") / part1 * part2;

                        holder.tv_productCountDetails2.setText("x"+q);

                    } else
                        holder.offer_c.setVisibility(View.GONE);

                }
        } else {
            holder.offer_c.setVisibility(View.GONE);
        }

        /*String c = "\\u002B";

            if (productModel.getOffer() != null)
                if (!productModel.getOffer().isEmpty()) {
                    String o = productModel.getOffer().replace(" ", "");
                    String[] parts = o.split(c);
                    int part1 = Integer.parseInt(parts[0]);
                    int part2 = Integer.parseInt(parts[1]);

                    if (productModel.getQuantity() >= part1 && part2 > 0) {
                        holder.offer_c.setVisibility(View.VISIBLE);
                        holder.tv_productDetailsName2.setText(productModel.getTitle());
                        int q = Integer.parseInt(productModel.getQuantity() + "") / part1 * part2;

                        holder.tv_productCountDetails2.setText("x"+q);
                    } else
                        holder.offer_c.setVisibility(View.GONE);

                }*/

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class productHolder extends RecyclerView.ViewHolder {
        TextView tv_productCountDetails, tv_productDetailsName, tv_productDetailsPrice;
        TextView tv_productCountDetails2, tv_productDetailsName2;
        ConstraintLayout offer_c;

        public productHolder(@NonNull View itemView) {
            super(itemView);
            tv_productCountDetails = itemView.findViewById(R.id.tv_productCountDetails);
            tv_productDetailsName = itemView.findViewById(R.id.tv_productDetailsName);
            tv_productCountDetails2 = itemView.findViewById(R.id.tv_productCountDetails2);
            tv_productDetailsName2 = itemView.findViewById(R.id.tv_productDetailsName2);
            tv_productDetailsPrice = itemView.findViewById(R.id.tv_productDetailsPrice);
            offer_c = itemView.findViewById(R.id.offer_c);
        }
    }
}
