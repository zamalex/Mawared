package app.mawared.alhayat;

public class AddressModel {
    double lat,lng;
    String mobile;
    String address;
    String username;
    String type;
    String title;
    boolean isAdded = false;
   public int city_id=0;

    @Override
    public String toString() {
        return "AddressModel{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", isAdded=" + isAdded +
                ", city_id=" + city_id +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public String getTitle() {
        return title;
    }

    public AddressModel(double lat, double lng, String mobile, String address, String username, String type, boolean isAdded) {
        this.lat = lat;
        this.lng = lng;
        this.mobile = mobile;
        this.address = address;
        this.username = username;
        this.isAdded = isAdded;
        this.type = type;
    }

    public AddressModel(double lat, double lng, String mobile, String address, String username, String type, boolean isAdded,int city,String title) {
        this.lat = lat;
        this.lng = lng;
        this.mobile = mobile;
        this.address = address;
        this.username = username;
        this.isAdded = isAdded;
        this.type = type;
        this.city_id=city;
        this.title=title;

    }



}
