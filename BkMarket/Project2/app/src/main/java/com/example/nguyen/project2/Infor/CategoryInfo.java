package com.example.nguyen.project2.Infor;

/**
 * Created by Nguyen on 28/02/2016.
 */
public class CategoryInfo {
    private String label;
    private int image;
    private int id;

    public CategoryInfo() {
    }

    public CategoryInfo(String label, int image, int id) {
        this.label = label;
        this.image = image;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
