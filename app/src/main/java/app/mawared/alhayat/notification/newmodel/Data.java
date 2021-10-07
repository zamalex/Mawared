package app.mawared.alhayat.notification.newmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("unread")
	private int unread;

	@SerializedName("last_notifications_read_at")
	private String lastNotificationsReadAt;

	@SerializedName("list")
	private List<ListItem> list;

	public int getUnread(){
		return unread;
	}

	public String getLastNotificationsReadAt(){
		return lastNotificationsReadAt;
	}

	public List<ListItem> getList(){
		return list;
	}
}