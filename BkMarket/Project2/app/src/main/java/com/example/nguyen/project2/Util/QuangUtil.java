package com.example.nguyen.project2.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nguyen on 15/03/2016.
 */
public class QuangUtil {
    /**
     * GET currentTime
     * @return
     */
    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        Date date =new Date();
        String result = simpleDateFormat.format(date);
        return result;
    }

    /**
     * hide keyboard
     * @param activity
     * @param view
     */
    public static void hideKeyboard(Activity activity,View view){
        final InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    /**
     * save user and pass into pref
     * @param context
     * @param key
     * @param value
     */
    public static void savePreference(Context context, String key, String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key,value);
        editor.apply();
    }

    /**
     * get data from pref
     * @param context
     * @param key
     * @return
     */
    public static String getFromPref(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(key,null);
    }
    public static void showToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }
}
