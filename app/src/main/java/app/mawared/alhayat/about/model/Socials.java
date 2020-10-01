
package app.mawared.alhayat.about.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Socials {

    @SerializedName("facebook")
    private Object mFacebook;
    @SerializedName("google-plus")
    private Object mGooglePlus;
    @SerializedName("instagram")
    private String mInstagram;
    @SerializedName("snapchat-ghost")
    private String mSnapchatGhost;
    @SerializedName("twitter")
    private String mTwitter;
    @SerializedName("whatsapp")
    private String mWhatsapp;

    public Object getFacebook() {
        return mFacebook;
    }

    public void setFacebook(Object facebook) {
        mFacebook = facebook;
    }

    public Object getGooglePlus() {
        return mGooglePlus;
    }

    public void setGooglePlus(Object googlePlus) {
        mGooglePlus = googlePlus;
    }

    public String getInstagram() {
        return mInstagram;
    }

    public void setInstagram(String instagram) {
        mInstagram = instagram;
    }

    public String getSnapchatGhost() {
        return mSnapchatGhost;
    }

    public void setSnapchatGhost(String snapchatGhost) {
        mSnapchatGhost = snapchatGhost;
    }

    public String getTwitter() {
        return mTwitter;
    }

    public void setTwitter(String twitter) {
        mTwitter = twitter;
    }

    public String getWhatsapp() {
        return mWhatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        mWhatsapp = whatsapp;
    }

}
