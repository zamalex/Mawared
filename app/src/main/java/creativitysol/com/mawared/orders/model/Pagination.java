
package creativitysol.com.mawared.orders.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination implements Parcelable
{

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("perPage")
    @Expose
    private int perPage;
    @SerializedName("currentPage")
    @Expose
    private int currentPage;
    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    public final static Creator<Pagination> CREATOR = new Creator<Pagination>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        public Pagination[] newArray(int size) {
            return (new Pagination[size]);
        }

    }
    ;

    protected Pagination(Parcel in) {
        this.count = ((int) in.readValue((int.class.getClassLoader())));
        this.total = ((int) in.readValue((int.class.getClassLoader())));
        this.perPage = ((int) in.readValue((int.class.getClassLoader())));
        this.currentPage = ((int) in.readValue((int.class.getClassLoader())));
        this.totalPages = ((int) in.readValue((int.class.getClassLoader())));
    }

    public Pagination() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(count);
        dest.writeValue(total);
        dest.writeValue(perPage);
        dest.writeValue(currentPage);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return  0;
    }

}
