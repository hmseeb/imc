package com.example.imc.Models;

public class Supplier {
    private String supplierName;
    private String productName;
    private String productCategory;
    private String productPrice;
    private int contactNumber;
    private boolean doesReturn;

    public Supplier(String supplierName, String productName, String productCategory, String productPrice, int contactNumber, boolean doesReturn) {
        this.supplierName = supplierName;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.contactNumber = contactNumber;
        this.doesReturn = doesReturn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public boolean isDoesReturn() {
        return doesReturn;
    }
}
