package com.example.nguyen.project2.Util;

import com.example.nguyen.project2.Config.Config;
import com.example.nguyen.project2.Infor.ItemsInfo;
import com.example.nguyen.project2.Infor.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Nguyen on 21/03/2016.
 */
public class LoadJSON {
    public static final String LINK = Config.ROOT_URL + "/bookbk.php";

    public void sendDataToServer(int method, HashMap<String, Object> map) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //send data to server
        params.put(Config.KEY_METHOD, method);
//        if (map != null) {
//            for (String key :
//                    map.keySet()) {
//                params.put(key, map.get(key));
//            }
//        }
        if (map != null) {
            for (String key : map.keySet()) {
                params.put(key, map.get(key));
            }
        }
        client.post(LINK, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                System.out.println("onSuccess:" + json);
                onFinishLoadJSonListener.finishLoadJSon(null, json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure:" + statusCode);

                String e;

                if (statusCode == 404) {
                    e = "Requested resource not found";
                } else if (statusCode == 500) {
                    e = "Something went wrong at server end";
                } else {
                    e = "Device might not be connected to Internet";
                }
                onFinishLoadJSonListener.finishLoadJSon(e, null);
            }
        });
    }

    public static UserInfo jsonToUser(JSONObject object) {
        try {
            int id = object.getInt(Config.KEY_ID);
            String userName = object.getString(Config.KEY_USER_NAME);
            String password = object.getString(Config.KEY_PASS);
            String name = object.getString(Config.KEY_NAME);
            String phone = object.getString(Config.KEY_PHONE);
            String address = object.getString(Config.KEY_ADDRESS);
            String created = object.getString(Config.KEY_CREATED);
            int userRole = object.getInt(Config.KEY_USER_ROLE);
            return new UserInfo(id, userRole, userName, password, phone, name, address, created);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemsInfo jsonToItems(JSONObject object) {
        try {
            int id = object.getInt(Config.KEY_ID);
            String title = object.getString(Config.KEY_TITLE);
            String content = object.getString(Config.KEY_CONTENT);
            String cate_name = object.getString(Config.KEY_CATE_NAME);
            String user_name = object.getString(Config.KEY_USER_NAME);
            String price = object.getString(Config.KEY_PRICE);
            String image_link = object.getString(Config.KEY_IMAGE_LINK);
            String image_list = object.getString(Config.KEY_IMAGE_LIST);
            String created = object.getString(Config.KEY_CREATED);
            int status = object.getInt(Config.KEY_STATUS);
            int qty = object.getInt(Config.KEY_QTY);
            return new ItemsInfo(id, user_name, cate_name, price, status, image_link, image_list, title, content, created, qty);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ItemsInfo> jsonToListItems(String json) {
        ArrayList<ItemsInfo> list = new ArrayList<ItemsInfo>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ItemsInfo item = jsonToItems(jsonObject);
                list.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public interface OnFinishLoadJSonListener {
        void finishLoadJSon(String error, String json);
    }

    public OnFinishLoadJSonListener onFinishLoadJSonListener;

    public void setOnFinishLoadJSonListener(OnFinishLoadJSonListener onFinishLoadJSonListener) {
        this.onFinishLoadJSonListener = onFinishLoadJSonListener;
    }
}
