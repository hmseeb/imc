package com.example.imc.Models;

public class Order {
    private final String productName;
    private final int productID;
    private final String productCategory;
    private final int value;
    private final int quantity;
    private final String date;

    private final String price;
    private final String dod;
    private final String unit;

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
