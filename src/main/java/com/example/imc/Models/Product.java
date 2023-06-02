package com.example.imc.Models;

public class Product {
    private final String productName;
    private final int productID;
    private final String productCategory;
    private final int price;
    private final int quantity;
    private final String date;
    private final int unit;
    private final int thresholdValue;


    public Product(String productName, int productID, String productCategory, int value, int quantity, String date, int unit, int thresholdValue) {
        this.productName = productName;
        this.productID = productID;
        this.productCategory = productCategory;
        this.price = value;
        this.quantity = quantity;
        this.date = date;
        this.unit = unit;
        this.thresholdValue = thresholdValue;

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

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public int getUnit() {
        return unit;
    }

    public int getThresholdValue() {
        return thresholdValue;
    }
}
