package creativitysol.com.mawared.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.about.model.SocialsModel;
import creativitysol.com.mawared.sendorder.model.Bank;



public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.Holder> {


    SocialsModel model =null;

    String url="";

    Context context;

    public AboutAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AboutAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_social, parent, false);
        return new AboutAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AboutAdapter.Holder holder, final int position) {
       if (position==0)
           holder.img.setImageResource(R.drawable.twitter);
        else if (position==1)
            holder.img.setImageResource(R.drawable.insta);
        else if (position==2)
            holder.img.setImageResource(R.drawable.rnap);
        else if (position==3)
            holder.img.setImageResource(R.drawable.snap);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0)
                 url = model.getData().getSocials().getTwitter();
                else if (position==1)
                    url = model.getData().getSocials().getInstagram();
                else if (position==2)
                    url = model.getData().getSocials().getWhatsapp();
                else if (position==3)
                    url = model.getData().getSocials().getSnapchatGhost();


                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }

        });

    }

    public void setBanks(SocialsModel model) {
        this.model = model;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


        return 4;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);

            img= itemView.findViewById(R.id.social_img);
        }
    }


}