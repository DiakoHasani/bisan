package com.example.bisan.DataTypes;

public class OrderItem {
    private String order_id,name,address,price,date,status,recieveTime;

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPrice() {
        return price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRecieveTime() {
        return recieveTime;
    }


    public void setRecieveTime(String recieveTime) {
        this.recieveTime = recieveTime;
    }
}
