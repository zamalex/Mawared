package app.mawared.alhayat.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mawared.alhayat.R;
import app.mawared.alhayat.home.model.Datum;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesHolder> {

    onCity listener;

    public CitiesAdapter(onCity listener) {
        this.listener = listener;
    }

    ArrayList<Datum> cities = new ArrayList<>();
    ArrayList<Datum> citiesCopy = new ArrayList<>();

    void setList(ArrayList<Datum> cities) {
        this.cities = cities;
        citiesCopy.addAll(cities);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        cities.clear();
        if(text.isEmpty()){
            cities.addAll(citiesCopy);
        } else{
            text = text.toLowerCase();
            for(Datum item: citiesCopy){
                if(item.getName().toLowerCase().contains(text)){
                    cities.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new CitiesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesHolder holder, int position) {
        Datum city = cities.get(position);

        holder.city.setText(city.getName());

        holder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelect(city);
            }
        });

    }


    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class CitiesHolder extends RecyclerView.ViewHolder {
        TextView city;

        public CitiesHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.city);
        }
    }

    public interface onCity{
        void onSelect(Datum city);
    }

}
