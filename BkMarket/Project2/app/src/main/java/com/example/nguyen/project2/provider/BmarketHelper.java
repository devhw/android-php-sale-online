package com.example.nguyen.project2.provider;

import android.content.ClipData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nguyen on 22/04/2016.
 */
public class BmarketHelper extends SQLiteOpenHelper {
    public static final String DATABESE_NAME = "bmarket.db";
    public static final int DATABASE_VERSION = 3;

    public BmarketHelper(Context context) {
        super(context, DATABESE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createItem(db);
        createUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ItemManager.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+UserManager.TABLE_NAME);
        onCreate(db);
    }

    private void createItem(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ItemManager.TABLE_NAME + " ("
                + ItemManager.ID_COLUMN + " INTEGER PRIMARY KEY , "
                + ItemManager.USER_NAME_COLUMN + " TEXT, "
                + ItemManager.CATE_NAME_COLUMN + " TEXT, "
                + ItemManager.PRICE_COLUMN + " TEXT, "
                + ItemManager.TITLE_COLUMN + " TEXT, "
                + ItemManager.CONTENT_COLUMN + " TEXT, "
                + ItemManager.IMAGE_LINK_COLUMN + " TEXT, "
                + ItemManager.IMAGE_LIST_COLUMN + " TEXT, "
                + ItemManager.CREATED_COLUMN + " TEXT, "
                + ItemManager.STATUS_COLUMN + " INTEGER, "
                + ItemManager.QTY_COLUMN + " INTEGER " + ");");
    }

    private void createUser(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UserManager.TABLE_NAME + " ("
                + UserManager.ID_COLUMN + " INTEGER PRIMARY KEY , "
                + UserManager.USER_NAME_COLUMN + " TEXT, "
                + UserManager.PHONE_COLUMN + " TEXT, "
                + UserManager.ADDRESS_COLUMN + " TEXT, "
                + UserManager.NAME_COLUMN + " TEXT, "
                + UserManager.USER_ROLE_COLUMN + " INTEGER " + ");");
    }
}
