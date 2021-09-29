package app.mawared.alhayat.orders.newmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("first_page_url")
	private String firstPageUrl;

	@SerializedName("path")
	private String path;

	@SerializedName("per_page")
	private int perPage;

	@SerializedName("total")
	private int total;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("last_page")
	private int lastPage;

	@SerializedName("last_page_url")
	private String lastPageUrl;

	@SerializedName("next_page_url")
	private String nextPageUrl;

	@SerializedName("from")
	private int from;

	@SerializedName("to")
	private int to;

	@SerializedName("prev_page_url")
	private Object prevPageUrl;

	@SerializedName("current_page")
	private int currentPage;

	public String getFirstPageUrl(){
		return firstPageUrl;
	}

	public String getPath(){
		return path;
	}

	public int getPerPage(){
		return perPage;
	}

	public int getTotal(){
		return total;
	}

	public List<DataItem> getData(){
		return data;
	}

	public int getLastPage(){
		return lastPage;
	}

	public String getLastPageUrl(){
		return lastPageUrl;
	}

	public String getNextPageUrl(){
		return nextPageUrl;
	}

	public int getFrom(){
		return from;
	}

	public int getTo(){
		return to;
	}

	public Object getPrevPageUrl(){
		return prevPageUrl;
	}

	public int getCurrentPage(){
		return currentPage;
	}
}