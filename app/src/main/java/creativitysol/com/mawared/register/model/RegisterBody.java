package creativitysol.com.mawared.register.model;

import com.google.gson.annotations.SerializedName;

public class RegisterBody {
    @SerializedName("mobile")
    String mobile;
    @SerializedName("code")
    String code;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("city_id")
    String city_id;


    public RegisterBody() {
    }

    public RegisterBody(String mobile, String code, String email, String password, String city_id) {
        this.mobile = mobile;
        this.code = code;
        this.email = email;
        this.password = password;
        this.city_id = city_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
