package app.mawared.alhayat.settings;

public class Settings {
    int itemId;
    int itemImageId;
    String itemText;
    String notificationCount;



    public Settings(int itemId, int itemImageId, String itemText, String notificationCount) {
        this.itemId = itemId;
        this.itemImageId = itemImageId;
        this.itemText = itemText;
        this.notificationCount = notificationCount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemImageId() {
        return itemImageId;
    }

    public void setItemImageId(int itemImageId) {
        this.itemImageId = itemImageId;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }
}
