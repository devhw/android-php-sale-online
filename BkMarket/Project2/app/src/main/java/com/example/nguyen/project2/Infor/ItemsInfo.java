package com.example.nguyen.project2.Infor;

import java.io.Serializable;

/**
 * Created by Nguyen on 01/03/2016.
 */
public class ItemsInfo implements Serializable{
    private int id;
    private String user_name;
    private String cate_name;
    private String price;
    private int status;
    private String image_link;
    private String image_list;
    private String title;
    private String content;
    private String created;
    private int qty;

    public ItemsInfo() {
    }

    public ItemsInfo(int id, String user_name, String cate_name, String price, int status, String image_link, String image_list, String title, String content, String created, int qty) {
        this.id = id;
        this.user_name = user_name;
        this.cate_name = cate_name;
        this.price = price;
        this.status = status;
        this.image_link = image_link;
        this.image_list = image_list;
        this.title = title;
        this.content = content;
        this.created = created;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_list() {
        return image_list;
    }

    public void setImage_list(String image_list) {
        this.image_list = image_list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
