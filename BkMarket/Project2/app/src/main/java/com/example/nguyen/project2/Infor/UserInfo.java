package com.example.nguyen.project2.Infor;

import com.example.nguyen.project2.Config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Nguyen on 01/03/2016.
 */
public class UserInfo implements Serializable{
    private int id;
    private int user_role;
    private String user_name;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String created;

    public UserInfo() {
    }

    public UserInfo(int id, int user_role, String user_name, String password, String phone, String name, String address, String created) {
        this.id = id;
        this.user_role = user_role;
        this.user_name = user_name;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_ID,getId());
            jsonObject.put(Config.KEY_USER_NAME,getUser_name());
            jsonObject.put(Config.KEY_PASS,getPassword());
            jsonObject.put(Config.KEY_NAME,getName());
            jsonObject.put(Config.KEY_PHONE,getPhone());
            jsonObject.put(Config.KEY_ADDRESS,getAddress());
            jsonObject.put(Config.KEY_CREATED,getCreated());
            jsonObject.put(Config.KEY_USER_ROLE,getUser_role());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
