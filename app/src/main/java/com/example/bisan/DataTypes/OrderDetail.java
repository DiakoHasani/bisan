package com.example.bisan.DataTypes;

public class OrderDetail {

    private String Product_name,count,price;

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public String getCount() {
        return count;
    }

}
