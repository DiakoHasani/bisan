package com.example.bisan.DataTypes;

public class CategoryItem {

    private String category_id,category_name,category_img;
    private boolean Selected;

    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_img() {
        return category_img;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
