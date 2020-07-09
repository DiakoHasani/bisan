package com.example.bisan.DataTypes;

public class SliderItem {
    private String id,image,order,description;
    private boolean isMain;

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getOrder() {
        return order;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
