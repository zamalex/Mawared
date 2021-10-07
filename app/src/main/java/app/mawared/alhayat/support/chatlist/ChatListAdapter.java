package app.mawared.alhayat.support.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mawared.alhayat.R;
import app.mawared.alhayat.support.chatlist.model.Chat;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {


    ChatListener chatListener;
    ArrayList<Chat> chats = new ArrayList<>();

    public ChatListAdapter(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public void setMessages(ArrayList<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item, parent, false);
        return new ChatListAdapter.Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.Holder holder, final int position) {

        if (chats.get(position).getOrderId() == null)
            holder.daam.setText("خدمة العملاء");

        else
            holder.daam.setText(" خدمة العملاء للطلب " + chats.get(position).getOrderId());

        holder.order_no.setText(chats.get(position).getCreatedAt());

        if (chats.get(position).lastMessage!=null)
        holder.last_msg.setText(chats.get(position).lastMessage.message);

      //  holder.unread_count.setText(chats.get(position).unread_count.toString());
        if (chats.get(position).getStatus().equals("0"))
        {
            holder.unread_count.setVisibility(View.VISIBLE);

            holder.read_count.setVisibility(View.INVISIBLE);
        }
        else {
            if (chats.get(position).unread_count > 0) {
                holder.read_count.setVisibility(View.VISIBLE);
                holder.read_count.setText(chats.get(position).unread_count+"");

            } else {
                holder.read_count.setVisibility(View.INVISIBLE);

            }
        }



    }


    @Override
    public int getItemCount() {

        if (chats == null)
            return 0;
        return chats.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView order_no, daam, last_msg, unread_count,read_count;
        ConstraintLayout item_cons;

        public Holder(@NonNull View itemView) {
            super(itemView);

            item_cons = itemView.findViewById(R.id.item_cons);
            order_no = itemView.findViewById(R.id.order_no);
            daam = itemView.findViewById(R.id.textView11);
            last_msg = itemView.findViewById(R.id.last_msg);
            unread_count = itemView.findViewById(R.id.unread_count);
            read_count = itemView.findViewById(R.id.read_count);

            item_cons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatListener.onChatClick(chats.get(getAdapterPosition()));
                }
            });
        }
    }


    interface ChatListener {
        void onChatClick(Chat chat);
    }
}
