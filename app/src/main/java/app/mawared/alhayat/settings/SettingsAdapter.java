package app.mawared.alhayat.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.mawared.alhayat.MainActivity;
import app.mawared.alhayat.R;
import app.mawared.alhayat.contactus.ContactUsFragment;
import app.mawared.alhayat.helpers.FragmentStack;
import app.mawared.alhayat.login.LoginActivity;
import app.mawared.alhayat.notification.NotificationFragments;
import io.paperdb.Paper;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.settingsHolder> {

    List<Settings> settingsList;
    FragmentStack fragmentStack;
    FragmentActivity fragmentActivity;
    SeetingsListener listener;
    Context context;
    String token = Paper.book().read("token", "none");

    public SettingsAdapter(List<Settings> settingsList, FragmentActivity fragmentActivity,SeetingsListener listener,Context context) {
        this.settingsList = settingsList;
        this.listener = listener;
        this.fragmentActivity = fragmentActivity;
        this.context = context;
    }

    @NonNull
    @Override
    public settingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_items, parent, false);
        return new settingsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull settingsHolder holder, int position) {
        Settings settingsModel = settingsList.get(position);
        if(settingsModel.getItemId() == 1){
            holder.cl_notificationItems.setVisibility(View.VISIBLE);
            holder.tv_notificationCount.setText(settingsModel.getNotificationCount());
        }
        holder.tv_settingsText.setText(settingsModel.getItemText());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.iv_settingsImage.setImageDrawable(holder.itemView.getResources().getDrawable(settingsModel.getItemImageId(), holder.itemView.getContext().getTheme()));
        } else {
            holder.iv_settingsImage.setImageDrawable(holder.itemView.getResources().getDrawable(settingsModel.getItemImageId()));
        }
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public class settingsHolder extends RecyclerView.ViewHolder {
        TextView tv_notificationCount,tv_settingsText;
        ImageView iv_settingsImage;
        ConstraintLayout cl_notificationItems;
        public settingsHolder(@NonNull final View itemView) {
            super(itemView);
            tv_notificationCount = itemView.findViewById(R.id.tv_notificationCount);
            tv_settingsText = itemView.findViewById(R.id.tv_settingsText);
            iv_settingsImage = itemView.findViewById(R.id.iv_settingsImage);
            cl_notificationItems = itemView.findViewById(R.id.cl_notificationItems);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (settingsList.get(getAdapterPosition()).getItemId() == 1&&token.equals("none")){
                        Toast.makeText(context, "يجب عليك تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        return;
                    }
                    if (settingsList.get(getAdapterPosition()).getItemId() == 1&&!token.equals("none")){
                        fragmentStack = new FragmentStack(fragmentActivity,fragmentActivity.getSupportFragmentManager(),R.id.main_container);
                        fragmentStack.push(new NotificationFragments());
                    }else if(settingsList.get(getAdapterPosition()).getItemId() == 2){
                        fragmentStack = new FragmentStack(fragmentActivity,fragmentActivity.getSupportFragmentManager(),R.id.main_container);
                        fragmentStack.push(new ContactUsFragment());
                    }
                    else {
                        listener.onSettingsClick(settingsList.get(getAdapterPosition()));
                    }
                }
            });


        }
    }

    public interface SeetingsListener{
        void onSettingsClick(Settings settings);
    }
}
