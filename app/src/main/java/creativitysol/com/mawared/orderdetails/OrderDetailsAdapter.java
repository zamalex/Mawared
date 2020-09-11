package creativitysol.com.mawared.orderdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.orderdetails.model.Product;


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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class productHolder extends RecyclerView.ViewHolder {
        TextView tv_productCountDetails, tv_productDetailsName, tv_productDetailsPrice;

        public productHolder(@NonNull View itemView) {
            super(itemView);
            tv_productCountDetails = itemView.findViewById(R.id.tv_productCountDetails);
            tv_productDetailsName = itemView.findViewById(R.id.tv_productDetailsName);
            tv_productDetailsPrice = itemView.findViewById(R.id.tv_productDetailsPrice);
        }
    }
}
