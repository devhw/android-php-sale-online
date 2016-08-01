package com.example.nguyen.project2.Config;

/**
 * Created by Nguyen on 17/03/2016.
 */
public class Config {
    public static final String ROOT_URL = "http://quangpg95-001-site1.1tempurl.com/";
    public static final String KEY_METHOD = "method";
    /*KEY of user table*/
    public static final String KEY_ID = "id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_PASS = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CREATED = "created";
    public static final String KEY_USER_ROLE = "user_role";
    /*KEY of items table*/
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CATE_NAME = "cate_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE_LINK = "image_link";
    public static final String KEY_IMAGE_LIST = "image_list";
    public static final String KEY_STATUS = "status";
    public static final String KEY_QTY = "qty";
    /*LOGIN*/
    public static final String KEY_LOGIN = "login";
    public static final String KEY_REGISTER = "register";
    public static final String KEY_ADD = "add";
    public static final String KEY_HIDE_ITEM = "update";
    /*METHOD*/
    public static final int METHOD_LOGIN = 1;
    public static final int METHOD_REGISTER = 2;
    public static final int METHOD_GET_ITEMS = 3;
    public static final int METHOD_ADD_ITEM = 4;
    public static final int METHOD_GET_ALL_ISTEMS = 5;
    public static final int METHOD_GET_BOOK_ISTEMS = 6;
    public static final int METHOD_GET_TEST_ISTEMS = 7;
    public static final int METHOD_GET_OTHER_ISTEMS = 8;
    public static final int METHOD_GET_USER_INFO = 9;
    public static final int METHOD_HIDE_ITEM = 10;
    public static final int METHOD_MANAGE_ITEMS = 11;
    public static final int METHOD_ADD_ITEM_IMG = 12;

}
