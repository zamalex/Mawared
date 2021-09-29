package app.mawared.alhayat.sendorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import app.mawared.alhayat.R;
import app.mawared.alhayat.sendorder.model.OrderShippingAddress;
import app.mawared.alhayat.sendorder.newaddress.DataItem;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Holder> {


    ArrayList<DataItem> addresses = new ArrayList<>();


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

    public void setAddresses(ArrayList<DataItem> addresses) {
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
        ImageView delete_address;
        public Holder(@NonNull View itemView) {
            super(itemView);
            addrss_tv = itemView.findViewById(R.id.addrss_tv);
            add_c = itemView.findViewById(R.id.add_c);
            delete_address = itemView.findViewById(R.id.delete_address);

            delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressInterface.onDelete(addresses.get(getAdapterPosition()));
                }
            });

            add_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressInterface.setAddress("شخصي",addresses.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface AddressInterface{
        void setAddress(String type,DataItem address);
        void onDelete(DataItem address);
    }

}