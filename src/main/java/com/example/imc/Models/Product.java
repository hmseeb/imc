package com.example.imc.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final StringProperty productID;
    private final StringProperty productName;
    private final StringProperty productCategory;
    private final StringProperty supplierID;
    private final StringProperty productPrice;
    private final StringProperty productQuantity;

    public Product(String productID, String productName, String supplierID, String productPrice, String productQuantity, String productCategory) {
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.supplierID = new SimpleStringProperty(supplierID);
        this.productPrice = new SimpleStringProperty(productPrice);
        this.productQuantity = new SimpleStringProperty(productQuantity);
        this.productCategory = new SimpleStringProperty(productCategory);
    }

    public String getProductID() {
        return productID.get();
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getSupplierID() {
        return supplierID.get();
    }

    public StringProperty supplierIDProperty() {
        return supplierID;
    }

    public String getProductPrice() {
        return productPrice.get();
    }

    public StringProperty productPriceProperty() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity.get();
    }

    public StringProperty productQuantityProperty() {
        return productQuantity;
    }

    public String getProductCategory() {
        return productCategory.get();
    }

    public StringProperty productCategoryProperty() {
        return productCategory;
    }
}
