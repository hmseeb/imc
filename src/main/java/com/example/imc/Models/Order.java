package com.example.imc.Models;

public class Order {
    private String productName;
    private int productID;
    private String productCategory;
    private int value;
    private int quantity;
    private String date;

    private String price;
    private String dod;
    private String unit;

    public Order(String productName, int productID, String productCategory, int value, int quantity, String date, String price, String dod, String unit) {
        this.productName = productName;
        this.productID = productID;
        this.productCategory = productCategory;
        this.value = value;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
        this.dod = dod;
        this.unit = unit;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public int getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getDod() {
        return dod;
    }

    public String getUnit() {
        return unit;
    }
}
