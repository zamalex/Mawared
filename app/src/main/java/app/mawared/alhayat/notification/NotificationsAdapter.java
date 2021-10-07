package app.mawared.alhayat.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.mawared.alhayat.R;
import app.mawared.alhayat.notification.model.Notifications_messages;
import app.mawared.alhayat.notification.newmodel.ListItem;
import app.mawared.alhayat.notification.newmodel.NewNotifications;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolders> {

    List<ListItem> notifications_messagesList;
    NotificationFragments notificationFragments;

    public NotificationsAdapter(List<ListItem> notifications_messagesList,NotificationFragments notificationFragments) {
        this.notifications_messagesList = notifications_messagesList;
        this.notificationFragments = notificationFragments;
    }

    @NonNull
    @Override
    public NotificationHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_item, parent, false);
        return new NotificationHolders(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolders holder, int position) {
        ListItem notifications_messagesModel = notifications_messagesList.get(position);
            String imageUrl = notifications_messagesModel.getPhoto();
            holder.tv_notificationTitle.setText(notifications_messagesModel.getTitle());
            holder.tv_notificationMessages.setText(notifications_messagesModel.getMessage());


            if (imageUrl!=null)
        Glide.with(holder.itemView.getContext()).load(imageUrl).apply(new RequestOptions()
                .placeholder(R.drawable.inv)
                .error(R.drawable.inv)
                .centerCrop()
                .fitCenter()).into(holder.iv_notificationImages);
    }

    @Override
    public int getItemCount() {
        if (notifications_messagesList==null)
            return 0;
        return notifications_messagesList.size();
    }

    public class NotificationHolders extends RecyclerView.ViewHolder {
        TextView tv_notificationTitle,tv_notificationMessages;
        ImageView iv_notificationImages;
        public NotificationHolders(@NonNull View itemView) {
            super(itemView);
            tv_notificationTitle = itemView.findViewById(R.id.tv_notificationTitle);
            tv_notificationMessages = itemView.findViewById(R.id.tv_notificationMessages);
            iv_notificationImages = itemView.findViewById(R.id.iv_notificationImages);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationFragments.goToProduct(notifications_messagesList.get(getAdapterPosition()));
                }
            });
        }
    }
}
