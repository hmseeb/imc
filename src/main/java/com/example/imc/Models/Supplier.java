package com.example.imc.Models;

public class Supplier {
    private String supplierName;
    private String productName;
    private String category;
    private String price;
    private int contact;
    private boolean doesReturn;

    public Supplier(String supplierName, String productName, String productCategory, String productPrice, int contactNumber, boolean doesReturn) {
        this.supplierName = supplierName;
        this.productName = productName;
        this.category = productCategory;
        this.price = productPrice;
        this.contact = contactNumber;
        this.doesReturn = doesReturn;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public int getContact() {
        return contact;
    }

    public boolean isDoesReturn() {
        return doesReturn;
    }
}
