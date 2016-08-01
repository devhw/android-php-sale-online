package com.example.nguyen.project2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nguyen.project2.Activity.HideItemActivity;
import com.example.nguyen.project2.Activity.ItemDetailsActivity;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.R;

import java.util.List;

/**
 * Created by Nguyen on 14/04/2016.
 */
public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.RecycleHoder>{
    private List<ItemsInfo> mListItem;
    private List<ItemsInfo> mStringFilter;
    private Context mContext;
    public ItemRecycleAdapter(List<ItemsInfo> mListItem, Context mContext) {
        this.mListItem = mListItem;
        this.mContext = mContext;
    }
    public void updateList(List<ItemsInfo> data){
        mListItem = data;
        notifyDataSetChanged();
    }

    @Override
    public RecycleHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View intView = inflater.inflate(R.layout.item,parent,false);
        return new RecycleHoder(intView);
    }

    @Override
    public void onBindViewHolder(RecycleHoder holder, int position) {
        ItemsInfo item = mListItem.get(position);
        int mColorhide = 0;
        int mColorwait = 0;
        if (item.getStatus() == 1){
            holder.title.setText(item.getTitle());
            holder.price.setText(item.getPrice()+" đ");
            holder.time.setText(item.getCreated());
        }else if(item.getStatus() == -1){
            mColorhide = mContext.getResources().getColor(R.color.color_hide_item);
            holder.title.setText(item.getTitle());
            holder.title.setTextColor(mColorhide);

            holder.price.setText(item.getPrice()+ "đ");
            holder.price.setTextColor(mColorhide);

            holder.time.setText(item.getCreated());
            holder.time.setTextColor(mColorhide);

            holder.hide.setVisibility(View.VISIBLE);

            holder.img.setImageResource(R.drawable.upload_photo_hide);
        }else{
            mColorhide = mContext.getResources().getColor(R.color.color_hide_wait);
            mColorwait = mContext.getResources().getColor(android.R.color.holo_green_light);
            holder.title.setText(item.getTitle());
            holder.title.setTextColor(mColorhide);

            holder.price.setText(item.getPrice()+ "đ");
            holder.price.setTextColor(mColorhide);

            holder.time.setText(item.getCreated());
            holder.time.setTextColor(mColorhide);

            holder.hide.setVisibility(View.VISIBLE);
            holder.hide.setText("Chờ duyệt");
            holder.hide.setTextColor(mColorwait);
            holder.img.setImageResource(R.drawable.upload_photo_hide);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public ItemsInfo getmItem(int position) {
        return mListItem.get(position);
    }

    public void detailItem(int position){
        if (getmItem(position).getStatus() == 1) {
            Intent intent = new Intent(mContext, ItemDetailsActivity.class);
            intent.putExtra("info",getmItem(position));
            mContext.startActivity(intent);
        }
    }
    public void hideItem(int position){
        if (getmItem(position).getStatus() == 1) {
            Intent intent = new Intent(mContext, HideItemActivity.class);
            intent.putExtra("id", getmItem(position).getId());
            mContext.startActivity(intent);
        }
    }
    public class RecycleHoder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{
        private TextView title, price, time, hide;
        private ImageView img;
        private RelativeLayout layout;
        public RecycleHoder(View itemView) {
            super(itemView);
            title =(TextView) itemView.findViewById(R.id.lbl_title);
            price = (TextView) itemView.findViewById(R.id.lbl_price);
            time = (TextView) itemView.findViewById(R.id.lbl_time);
            img = (ImageView) itemView.findViewById(R.id.img_title);
            hide = (TextView) itemView.findViewById(R.id.lbl_hide);
            layout = (RelativeLayout)itemView.findViewById(R.id.rl_layout_item);
            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            detailItem(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            hideItem(getAdapterPosition());
            return true;
        }
    }
}
