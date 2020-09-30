package creativitysol.com.mawared.support.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.login.model.LoginResponse;
import creativitysol.com.mawared.support.chat.model.received.Message;
import io.paperdb.Paper;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    String user = ((LoginResponse) Paper.book().read("login")).getUser().getId().toString();

    ArrayList<Message> messages = new ArrayList<>();

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.Holder holder, final int position) {

        Message msg = messages.get(position);
        String newString="00:00 AM";
        String originalString = msg.getCreatedAt();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
             newString = new SimpleDateFormat("H:mm a").format(date);





        } catch (ParseException e) {
            e.printStackTrace();
            newString="00:00 AM";
        }




        if (msg.getUserId().equals(user)) {
            holder.me.setVisibility(View.VISIBLE);
            holder.you.setVisibility(View.GONE);
            holder.met.setVisibility(View.VISIBLE);
            holder.yout.setVisibility( View.GONE);

            holder.me.setText(msg.getMessage());

            holder.met.setText(newString);


        } else {
            holder.me.setVisibility(View.GONE);
            holder.you.setVisibility(View.VISIBLE);
            holder.met.setVisibility(View.GONE);
            holder.yout.setVisibility(View.VISIBLE);

            holder.you.setText(msg.getMessage());
            holder.yout.setText(newString);


        }

    }



    @Override
    public int getItemCount() {

        if (messages==null)
            return 0;
        return messages.size();
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
