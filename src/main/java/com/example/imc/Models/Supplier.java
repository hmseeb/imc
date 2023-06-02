package com.example.imc.Models;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier {
    private final StringProperty supplierName;
    private final StringProperty productName;
    private final StringProperty category;
    private final StringProperty price;
    private final StringProperty contact;
    private final StringProperty doesReturn;

public Supplier(String supplierName, String productName, String category, String price, String contact, String doesReturn) {
        this.supplierName = new SimpleStringProperty(supplierName);
        this.productName = new SimpleStringProperty(productName);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleStringProperty(price);
        this.contact = new SimpleStringProperty(contact);
        this.doesReturn = new SimpleStringProperty(doesReturn);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public StringProperty supplierNameProperty() {
        return supplierName;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public String getDoesReturn() {
        return doesReturn.get();
    }

    public StringProperty doesReturnProperty() {
        return doesReturn;
    }
}
