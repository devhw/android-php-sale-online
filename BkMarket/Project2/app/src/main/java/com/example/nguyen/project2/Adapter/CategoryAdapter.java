package com.example.nguyen.project2.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyen.project2.Infor.CategoryInfo;
import com.example.nguyen.project2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 28/02/2016.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryInfo> {
    List<CategoryInfo> items = new ArrayList<CategoryInfo>();
    Activity mContext;

    public CategoryAdapter(Activity context, int resource, List<CategoryInfo> items) {
        super(context, resource, items);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.catagory_item, null);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.img_category);
        TextView lable = (TextView) convertView.findViewById(R.id.lbl_category);

        CategoryInfo item = items.get(position);
        img.setImageResource(item.getImage());
        lable.setText(item.getLabel());
        return convertView;
    }
}
