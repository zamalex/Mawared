
package app.mawared.alhayat.about.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("socials")
    private Socials mSocials;

    public Socials getSocials() {
        return mSocials;
    }

    public void setSocials(Socials socials) {
        mSocials = socials;
    }

}
