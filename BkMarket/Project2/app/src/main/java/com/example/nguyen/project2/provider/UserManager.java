package com.example.nguyen.project2.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.Infor.UserInfo;

/**
 * Created by Nguyen on 22/04/2016.
 */
public class UserManager {
    public static final String TABLE_NAME = "user";
    public static final String ID_COLUMN = "id";
    public static final String USER_NAME_COLUMN = "user_name";
    public static final String PASSWORD_COLUMN = "password";
    public static final String NAME_COLUMN = "name";
    public static final String PHONE_COLUMN = "phone";
    public static final String ADDRESS_COLUMN = "address";
    public static final String CREATED_COLUMN = "created";
    public static final String USER_ROLE_COLUMN = "user_role";
    private Context mContext;
    private static UserManager mUserManager;

    public UserManager(Context context) {
        mContext = context;
    }

    public static synchronized UserManager getInstance(Context context) {
        if (mUserManager == null || mUserManager.mContext == null) {
            mUserManager = new UserManager(context);
        }
        return mUserManager;
    }

    public boolean inSertUser(UserInfo info) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, info.getId());
        values.put(USER_NAME_COLUMN, info.getUser_name());
        values.put(NAME_COLUMN, info.getName());
        values.put(PHONE_COLUMN, info.getPhone());
        values.put(ADDRESS_COLUMN, info.getAddress());
        values.put(USER_ROLE_COLUMN, info.getUser_role());
        mContext.getContentResolver().insert(BMarketProvider.USER_URI, values);
        return true;
    }

    public UserInfo getUser(String where) {
        Cursor cursor;
        UserInfo userInfo = new UserInfo();
        cursor = mContext.getContentResolver().query(BMarketProvider.USER_URI, null, where, null, null);
        try {

            if (cursor.getCount() > 0 && cursor != null) {
                cursor.moveToFirst();
                userInfo.setId(cursor.getInt(cursor.getColumnIndex(ID_COLUMN)));
                userInfo.setUser_name(cursor.getString(cursor.getColumnIndex(USER_NAME_COLUMN)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                userInfo.setPhone(cursor.getString(cursor.getColumnIndex(PHONE_COLUMN)));
                userInfo.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN)));
                userInfo.setUser_role(cursor.getInt(cursor.getColumnIndex(USER_ROLE_COLUMN)));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userInfo;
    }

    public boolean isUserExist(String userName) {
        Cursor cursor;
        boolean check = false;
        cursor = mContext.getContentResolver().query(BMarketProvider.USER_URI, null, "user_name LIKE '" + userName + "'", null, null);
        try {

            if (cursor.getCount() > 0 && cursor != null) {
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
