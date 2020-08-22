package creativitysol.com.mawared.support.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import creativitysol.com.mawared.R;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {



    @NonNull
    @Override
    public ChatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.Holder holder, final int position) {
        if (position % 2 == 0) {
            holder.me.setVisibility(View.VISIBLE);
            holder.you.setVisibility(View.GONE);
            holder.met.setVisibility(View.VISIBLE);
            holder.yout.setVisibility( View.GONE);



        } else {
            holder.me.setVisibility(View.GONE);
            holder.you.setVisibility(View.VISIBLE);
            holder.met.setVisibility(View.GONE);
            holder.yout.setVisibility(View.VISIBLE);

        }

    }



    @Override
    public int getItemCount() {

        return 5;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView me,you,met,yout;

        public Holder(@NonNull View itemView) {
            super(itemView);

            me = itemView.findViewById(R.id.me);
            you = itemView.findViewById(R.id.you);
            met = itemView.findViewById(R.id.met);
            yout = itemView.findViewById(R.id.yout);

        }
    }


}
