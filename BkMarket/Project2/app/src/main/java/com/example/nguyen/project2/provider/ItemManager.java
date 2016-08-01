package com.example.nguyen.project2.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.nguyen.project2.Infor.ItemsInfo;

import java.util.ArrayList;

/**
 * Created by Nguyen on 22/04/2016.
 */
public class ItemManager {
    public static final String TABLE_NAME = "items";
    public static final String ID_COLUMN = "id";
    public static final String USER_NAME_COLUMN = "user_name";
    public static final String CATE_NAME_COLUMN = "cate_name";
    public static final String PRICE_COLUMN = "price";
    public static final String STATUS_COLUMN = "status";
    public static final String IMAGE_LINK_COLUMN = "image_link";
    public static final String IMAGE_LIST_COLUMN = "image_list";
    public static final String TITLE_COLUMN = "title";
    public static final String CONTENT_COLUMN = "content";
    public static final String CREATED_COLUMN = "created";
    public static final String QTY_COLUMN = "qty";
    private Context mContext;
    private static ItemManager mItemManager;

    public ItemManager(Context context) {
        mContext = context;
    }

    public static synchronized ItemManager getInstance(Context context) {
        if (mItemManager == null || mItemManager.mContext == null) {
            mItemManager = new ItemManager(context);
        }
        return mItemManager;
    }

    public boolean inSertItem(ItemsInfo info) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, info.getId());
        values.put(USER_NAME_COLUMN, info.getUser_name());
        values.put(CATE_NAME_COLUMN, info.getCate_name());
        values.put(PRICE_COLUMN, info.getPrice());
        values.put(STATUS_COLUMN, info.getStatus());
        values.put(IMAGE_LINK_COLUMN, info.getImage_link());
        values.put(IMAGE_LIST_COLUMN, info.getImage_list());
        values.put(TITLE_COLUMN, info.getTitle());
        values.put(CONTENT_COLUMN, info.getContent());
        values.put(CREATED_COLUMN, info.getCreated());
        values.put(QTY_COLUMN, info.getQty());
        Uri uri = mContext.getContentResolver().insert(BMarketProvider.ITEM_URI, values);
        return true;
    }

    public boolean deleteAll(String where) {
        mContext.getContentResolver().delete(BMarketProvider.ITEM_URI, where, null);
        return true;
    }

    public ArrayList<ItemsInfo> getListItem(String where) {
        ArrayList<ItemsInfo> listItem = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver()
                .query(BMarketProvider.ITEM_URI, null, where, null, ID_COLUMN + " DESC", null);
        try {
            if (cursor.getCount() > 0 && cursor != null) {
                cursor.moveToFirst();
                do {
                    listItem.add(getItemFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listItem;
    }

    public ItemsInfo getItemFromCursor(Cursor cursor) {
        ItemsInfo info = new ItemsInfo();
        info.setId(cursor.getInt(cursor.getColumnIndex(ID_COLUMN)));
        info.setUser_name(cursor.getString(cursor.getColumnIndex(USER_NAME_COLUMN)));
        info.setCate_name(cursor.getString(cursor.getColumnIndex(CATE_NAME_COLUMN)));
        info.setTitle(cursor.getString(cursor.getColumnIndex(TITLE_COLUMN)));
        info.setContent(cursor.getString(cursor.getColumnIndex(CONTENT_COLUMN)));
        info.setImage_link(cursor.getString(cursor.getColumnIndex(IMAGE_LINK_COLUMN)));
        info.setImage_list(cursor.getString(cursor.getColumnIndex(IMAGE_LIST_COLUMN)));
        info.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS_COLUMN)));
        info.setQty(cursor.getInt(cursor.getColumnIndex(QTY_COLUMN)));
        info.setCreated(cursor.getString(cursor.getColumnIndex(CREATED_COLUMN)));
        info.setPrice(cursor.getString(cursor.getColumnIndex(PRICE_COLUMN)));
        return info;
    }

    public boolean isItemExist(int id) {
        Cursor cursor;
        boolean check = false;
        cursor = mContext.getContentResolver().query(BMarketProvider.ITEM_URI, null, "id = " + id, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                check = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return check;
    }

    public boolean isHasData(String where) {
        Cursor cursor;
        boolean check = false;
        cursor = mContext.getContentResolver().query(BMarketProvider.ITEM_URI, null, where, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                check = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return check;
    }
}
