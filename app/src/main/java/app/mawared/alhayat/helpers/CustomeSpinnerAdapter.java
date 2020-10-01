package app.mawared.alhayat.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import app.mawared.alhayat.R;

public class CustomeSpinnerAdapter extends ArrayAdapter<String> {

    List<String> titles;
    List<String> citiesId;
    Context mContext;

    public CustomeSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CustomeSpinnerAdapter(Context mContext, List<String> titles, List<String> mcitiesId) {
        super(mContext,  R.layout.item_customadapter);
        this.titles = titles;
        this.citiesId = mcitiesId;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_customadapter, parent, false);

            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tv_CarMakers);
            mViewHolder.horizntalLine = convertView.findViewById(R.id.v_horizentalView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mName.setText(titles.get(position));
        mViewHolder.horizntalLine.setVisibility(View.INVISIBLE);

        return convertView;
    }
    @Override
    public View getDropDownView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    private static class ViewHolder {

        TextView mName;
        View horizntalLine;


    }
}
