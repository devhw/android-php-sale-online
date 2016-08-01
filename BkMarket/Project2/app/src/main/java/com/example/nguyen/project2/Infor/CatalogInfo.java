package com.example.nguyen.project2.Infor;

/**
 * Created by Nguyen on 21/03/2016.
 */
public class CatalogInfo {
    private int id;
    private int parent_id;
    private String name;

    public CatalogInfo(int parent_id, int id, String name) {
        this.parent_id = parent_id;
        this.id = id;
        this.name = name;
    }

    public CatalogInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
