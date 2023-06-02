package com.example.imc.Models;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final StringProperty productName;
    private final StringProperty productID;
    private final StringProperty productCategory;
    private final StringProperty price;
    private final StringProperty quantity;
    private final StringProperty date;
    private final StringProperty unit;
    private final StringProperty thresholdValue;

    public Product(String productName, String productID, String productCategory, String price, String quantity, String date, String unit, String thresholdValue) {
        this.productName = new SimpleStringProperty(productName);
        this.productID = new SimpleStringProperty(productID);
        this.productCategory = new SimpleStringProperty( productCategory);
        this.price = new SimpleStringProperty( price);
        this.quantity = new SimpleStringProperty( quantity);
        this.date = new SimpleStringProperty( date);
        this.unit = new SimpleStringProperty( unit);
        this.thresholdValue = new SimpleStringProperty( thresholdValue);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getProductID() {
        return productID.get();
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public String getProductCategory() {
        return productCategory.get();
    }

    public StringProperty productCategoryProperty() {
        return productCategory;
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getQuantity() {
        return quantity.get();
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public String getThresholdValue() {
        return thresholdValue.get();
    }

    public StringProperty thresholdValueProperty() {
        return thresholdValue;
    }
}
