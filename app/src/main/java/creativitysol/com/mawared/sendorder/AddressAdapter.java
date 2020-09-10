package creativitysol.com.mawared.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.sendorder.model.CustomerShippingAddress;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Holder> {


    ArrayList<CustomerShippingAddress> addresses = new ArrayList<>();


    AddressInterface addressInterface;

    public AddressAdapter(AddressInterface addressInterface) {
        this.addressInterface = addressInterface;
    }

    @NonNull
    @Override
    public AddressAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new AddressAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressAdapter.Holder holder, final int position) {

        holder.addrss_tv.setText(addresses.get(position).getAddress());



    }

    public void setAddresses(ArrayList<CustomerShippingAddress> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        if (addresses==null)
            return 0;
        return addresses.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView addrss_tv;
        ConstraintLayout add_c;
        public Holder(@NonNull View itemView) {
            super(itemView);
            addrss_tv = itemView.findViewById(R.id.addrss_tv);
            add_c = itemView.findViewById(R.id.add_c);

            add_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressInterface.setAddress("شخصي",addresses.get(getAdapterPosition()).getAddress());
                }
            });

        }
    }

    public interface AddressInterface{
        void setAddress(String type,String address);
    }

}