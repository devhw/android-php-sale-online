package com.example.nguyen.project2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Nguyen on 22/04/2016.
 */
public class BMarketProvider extends ContentProvider {
    public static final int ITEM = 1;
    public static final int USER = 2;
    private BmarketHelper mHelper;
    public static final String AUTHORITY = "com.example.nguyen.project2.QUANG";
    public static final Uri ITEM_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri USER_URI = Uri.parse("content://" + AUTHORITY + "/user");
    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "item", ITEM);
        sUriMatcher.addURI(AUTHORITY, "user", USER);
    }

    @Override
    public boolean onCreate() {
        mHelper = new BmarketHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)){
            case ITEM:
                builder.setTables(ItemManager.TABLE_NAME);
                break;
            case USER:
                builder.setTables(UserManager.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case ITEM:
                return "item";
            case USER:
                return "user";
            default:
                return "Not found";
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (values.size() ==0){
            return null;
        }
        String tableName;
        switch (sUriMatcher.match(uri)){
            case ITEM:
                tableName = ItemManager.TABLE_NAME;
                break;
            case USER:
                tableName = UserManager.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long rowId;
        rowId = db.insert(tableName,"",values);
        if(rowId>0){
            if(tableName == ItemManager.TABLE_NAME){
                Uri _uri = ContentUris.withAppendedId(ITEM_URI, rowId);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;
            }else{
                Uri _uri = ContentUris.withAppendedId(USER_URI, rowId);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;
            }

        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case ITEM:
                count = db.delete(ItemManager.TABLE_NAME,selection,selectionArgs);
                break;
            case USER:
                count = db.delete(ItemManager.TABLE_NAME,selection,selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String table;
        switch (sUriMatcher.match(uri)){
            case ITEM:
                table = ItemManager.TABLE_NAME;
                count = db.update(table,values,selection,selectionArgs);
                break;
            case USER:
                table = UserManager.TABLE_NAME;
                count = db.update(table,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
