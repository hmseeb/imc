package com.example.imc.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
    private final StringProperty productName;
    private final StringProperty productID;
    private final StringProperty productCategory;
    private final StringProperty value;
    private final StringProperty quantity;
    private final StringProperty date;

    private final StringProperty price;
    private final StringProperty dod;
    private final StringProperty unit;

    public Order(String productName, String productID, String productCategory,  String value, String quantity, String date, String price, String dod, String unit) {
        this.productName = new SimpleStringProperty(productName);
        this.productID = new SimpleStringProperty (productID);
        this.productCategory = new SimpleStringProperty (productCategory);
        this.value = new SimpleStringProperty (value);
        this.quantity = new SimpleStringProperty (quantity);
        this.date = new SimpleStringProperty (date);
        this.price = new SimpleStringProperty (price);
        this.dod = new SimpleStringProperty (dod);
        this.unit = new SimpleStringProperty (unit);
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

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
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

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getDod() {
        return dod.get();
    }

    public StringProperty dodProperty() {
        return dod;
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }
}
