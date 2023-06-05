package com.example.imc.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Order {
    private final StringProperty orderID;
    private final StringProperty orderDate;

    private final StringProperty productID;

    private final StringProperty orderQuantity;

    public Order(String orderID, String orderDate, String productID, String orderQuantity) {
        this.orderID = new SimpleStringProperty(orderID);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.productID = new SimpleStringProperty(productID);
        this.orderQuantity = new SimpleStringProperty(orderQuantity);
    }

    public String getOrderID() {
        return orderID.get();
    }

    public StringProperty orderIDProperty() {
        return orderID;
    }


    public String getOrderDate(Date currentDate) {
        // Get the current date and time
        // Convert to a Timestamp object
        Timestamp timestamp = new Timestamp(currentDate.getTime());
        // Format the timestamp in SQL format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Return the timestamp in SQL format
        return dateFormat.format(timestamp);
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    public String getProductID() {
        return productID.get();
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public String getOrderQuantity() {
        return orderQuantity.get();
    }

    public StringProperty orderQuantityProperty() {
        return orderQuantity;
    }
}
