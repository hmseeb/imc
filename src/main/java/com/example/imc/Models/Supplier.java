package com.example.imc.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier {
    private final StringProperty supplierID;
    private final StringProperty supplierName;

    private final StringProperty supplierAddress;
    private final StringProperty supplierPhone;

    public Supplier(String supplierID, String supplierName, String supplierAddress, String supplierPhone) {
        this.supplierID = new SimpleStringProperty(supplierID);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.supplierPhone = new SimpleStringProperty(supplierPhone);
        this.supplierAddress = new SimpleStringProperty(supplierAddress);
    }

    public String getSupplierID() {
        return supplierID.get();
    }

    public StringProperty supplierIDProperty() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public StringProperty supplierNameProperty() {
        return supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress.get();
    }

    public StringProperty supplierAddressProperty() {
        return supplierAddress;
    }

    public String getSupplierPhone() {
        return supplierPhone.get();
    }

    public StringProperty supplierPhoneProperty() {
        return supplierPhone;
    }
}
