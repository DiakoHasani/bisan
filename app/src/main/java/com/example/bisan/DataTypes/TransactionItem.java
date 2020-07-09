package com.example.bisan.DataTypes;

public class TransactionItem {
    private String transaction_id,date,price;
    private int type;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setType(int type) {
        this.type = type;
    }
}
