package creativitysol.com.mawared.support.chatlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import creativitysol.com.mawared.R;
import creativitysol.com.mawared.support.chat.model.received.Message;
import creativitysol.com.mawared.support.chatlist.model.Chat;

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
            holder.order_no.setText("خدمات اخرى");

        else
        holder.order_no.setText(" رقم الطلب " + chats.get(position).getOrderId());

    }


    @Override
    public int getItemCount() {

        if (chats == null)
            return 0;
        return chats.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView order_no;
        ConstraintLayout item_cons;

        public Holder(@NonNull View itemView) {
            super(itemView);

            item_cons = itemView.findViewById(R.id.item_cons);
            order_no = itemView.findViewById(R.id.order_no);

            item_cons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatListener.onChatClick(chats.get(getAdapterPosition()));
                }
            });
        }
    }


    interface ChatListener{
        void onChatClick(Chat chat);
    }
}
