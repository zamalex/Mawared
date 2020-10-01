
package app.mawared.alhayat.sendorder.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Bank {

    public int is=0;
    @SerializedName("account_number")
    private String mAccountNumber;
    @SerializedName("hint")
    private String mHint;
    @SerializedName("iban")
    private String mIban;
    @SerializedName("id")
    private Long mId;
    @SerializedName("logo")
    private String mLogo;
    @SerializedName("name")
    private String mName;

    public String getAccountNumber() {
        return mAccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        mAccountNumber = accountNumber;
    }

    public String getHint() {
        return mHint;
    }

    public void setHint(String hint) {
        mHint = hint;
    }

    public String getIban() {
        return mIban;
    }

    public void setIban(String iban) {
        mIban = iban;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        mLogo = logo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
