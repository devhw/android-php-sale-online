package com.example.nguyen.project2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 01/03/2016.
 */
public class ItemsAdapter extends ArrayAdapter<ItemsInfo> implements Filterable {
    private List<ItemsInfo> mListItem;
    private List<ItemsInfo> mStringFilter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private TextView title, price, time, hide;
    private ImageView img;
    private DisplayImageOptions option;
    private String link = "";

    public ItemsAdapter(Context context, int resource, List<ItemsInfo> mListItem) {
        super(context, resource, mListItem);
        this.mListItem = mListItem;
        this.mContext = context;
        mStringFilter = mListItem;
        mLayoutInflater = LayoutInflater.from(mContext);
        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemsInfo item = getItem(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item, null);
        }
        title = (TextView) convertView.findViewById(R.id.lbl_title);
        price = (TextView) convertView.findViewById(R.id.lbl_price);
        time = (TextView) convertView.findViewById(R.id.lbl_time);
        img = (ImageView) convertView.findViewById(R.id.img_title);
        hide = (TextView) convertView.findViewById(R.id.lbl_hide);
        int colorHide = 0;
        int colorHide1 = 0;
        if (item.getStatus() == 1) {
            link = item.getImage_link();
            System.out.println("QuangNDB:" + link);
            option = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.upload_photo)
                    .showImageOnFail(R.drawable.upload_photo)
                    .showImageOnLoading(R.drawable.upload_photo)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory(true).cacheOnDisk(true).build();
            title.setText(getItem(position).getTitle());
            price.setText(getItem(position).getPrice() + " đ");
            time.setText(getItem(position).getCreated());
            hide.setVisibility(View.GONE);
            price.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            title.setTextColor(mContext.getResources().getColor(android.R.color.black));
            time.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
            ImageLoader.getInstance().displayImage(link, img,
                    option);
        } else if (item.getStatus() == -1) {
            link = item.getImage_link();
            System.out.println("QuangNDB:" + link);
            option = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.upload_photo_hide)
                    .showImageOnFail(R.drawable.upload_photo_hide)
                    .showImageOnLoading(R.drawable.upload_photo_hide)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory(true).cacheOnDisk(true).build();
            colorHide = mContext.getResources().getColor(R.color.color_hide_item);
            title.setText(getItem(position).getTitle());
            title.setTextColor(colorHide);

            price.setText(getItem(position).getPrice() + " đ");
            price.setTextColor(colorHide);

            time.setText(getItem(position).getCreated());
            time.setTextColor(colorHide);

            hide.setVisibility(View.VISIBLE);
            hide.setText("Đã ẩn");
            hide.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));

            ImageLoader.getInstance().displayImage(link, img,
                    option);
        } else {
            link = item.getImage_link();
            System.out.println("QuangNDB:" + link);
            option = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.upload_photo_hide)
                    .showImageOnFail(R.drawable.upload_photo_hide)
                    .showImageOnLoading(R.drawable.upload_photo_hide)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory(true).cacheOnDisk(true).build();
            colorHide = mContext.getResources().getColor(R.color.color_hide_wait);
            colorHide1 = mContext.getResources().getColor(android.R.color.holo_green_light);
            title.setText(getItem(position).getTitle());
            title.setTextColor(colorHide);

            price.setText(getItem(position).getPrice() + " đ");
            price.setTextColor(colorHide);

            time.setText(getItem(position).getCreated());
            time.setTextColor(colorHide);


            hide.setVisibility(View.VISIBLE);
            hide.setText("Chờ duyệt");
            hide.setTextColor(colorHide1);

            ImageLoader.getInstance().displayImage(link, img,
                    option);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter result = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults mFilterResults = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<ItemsInfo> mFilterList = new ArrayList<ItemsInfo>();
                    for (int i = 0; i < mStringFilter.size(); i++) {
                        if (mStringFilter.get(i).getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                            mFilterList.add(mStringFilter.get(i));
                        }
                    }
                    mFilterResults.count = mFilterList.size();
                    mFilterResults.values = mFilterList;
                } else {
                    mFilterResults.count = mStringFilter.size();
                    mFilterResults.values = mStringFilter;
                }
                return mFilterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mListItem = (ArrayList<ItemsInfo>) results.values;
                    notifyDataSetChanged();
                }

            }
        };
        return result;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public ItemsInfo getItem(int position) {
        return mListItem.get(position);
    }
}
