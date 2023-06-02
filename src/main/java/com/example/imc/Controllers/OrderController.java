package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Models.Order;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderController {
    Statement stmt;

    @FXML
    TextField nameController;
    @FXML
    TextField idController;
    @FXML
    TextField quantityController;
    @FXML
    TextField priceController;
    @FXML
    TextField categoryController;
    @FXML
    TextField dateController;
    @FXML
    TextField valueController;
    @FXML
    TextField unitController;
    @FXML
    TextField dodController;


    @FXML
    private Pane mainPane;

    @FXML
    private Pane popupPane;
    @FXML
    private VBox ordersContainer;

    // For the add product button in the inventory view

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();
        String query = "CREATE TABLE if not exists orders (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "productName VARCHAR(255) NOT NULL," +
                "productID INT NOT NULL," +
                "productCategory VARCHAR(255) NOT NULL," +
                "value INT NOT NULL," +
                "quantity INT NOT NULL," +
                "date DATE NOT NULL," +
                "price VARCHAR(255) NOT NULL," +
                "dod VARCHAR(255) NOT NULL," +
                "unit VARCHAR(255) NOT NULL" +
                ")";
        ResultSet rs = stmt.executeQuery("select * from orders");

        while (rs.next()) {
            String productName = rs.getString("productName");
            int productID = rs.getInt("productID");
            String productCategory = rs.getString("productCategory");
            int value = rs.getInt("value");
            int quantity = rs.getInt("quantity");
            String date = rs.getString("date");
            String price = rs.getString("price");
            String dod = rs.getString("dod");
            String unit = rs.getString("unit");

            Platform.runLater(() -> addOrder(productName, price, String.valueOf(quantity), String.valueOf(productID), dod, "Testing"));
        }
        stmt.executeUpdate(query);
    }



    @FXML
    void kAddOrder() {
        // Apply a BoxBlur effect to the mainPane to make it blur
        BoxBlur blur = new BoxBlur(5, 5, 3);
        mainPane.setEffect(blur);

        // Make the popup pane visible and animate its fade-in
        popupPane.setOpacity(0);
        popupPane.setVisible(true);
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }

@FXML
private void onConfirmClicked() throws SQLException {
    String name = nameController.getText();
    String id = idController.getText();
    String quantity = quantityController.getText();
    String price = priceController.getText();
    String category = categoryController.getText();
    String date = dateController.getText();
    String value = valueController.getText();
    String unit = unitController.getText();
    String dod = dodController.getText();

    // Create the Order object
    Order order = new Order(name, Integer.parseInt(id), category, Integer.parseInt(value), Integer.parseInt(quantity), date, price, dod, unit);

    // Insert the order data into the database
    String insertQuery = "INSERT INTO orders (productName, productID, productCategory, value, quantity, date, price, dod, unit) " +
            "VALUES ('" + order.getProductName() + "', '" + order.getProductID() + "', '" + order.getProductCategory() + "', '" +
            order.getValue() + "', '" + order.getQuantity() + "', '" + order.getDate() + "', '" + order.getPrice() + "', '" +
            order.getDod() + "', '" + order.getUnit() + "')";
    stmt.executeUpdate(insertQuery);

    // Add the order to the UI
    addOrder(order.getProductName(), order.getPrice(), String.valueOf(order.getQuantity()), String.valueOf(order.getProductID()),
            order.getDod(), "Testing");

    // Animate the popup pane's fade-out and then hide it
    FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
    fadeOutTransition.setToValue(0);
    fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
    fadeOutTransition.play();

    // Remove the BoxBlur effect from the mainPane
    mainPane.setEffect(null);
}


    @FXML
    private void onDiscardClicked() {
        // TODO Add additional logic here
        // Animate the popup pane's fade-out and then hide it
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        fadeOutTransition.play();

        // Remove the BoxBlur effect from the mainPane
        mainPane.setEffect(null);
    }

    public void addOrder(String name, String price, String quantity, String id, String dod, String status) {
        HBox supplierPane = new HBox();
        supplierPane.getStyleClass().add("supplier-pane");
        supplierPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel(name);
        Label priceLabel = createLabel(price);
        Label quantityLabel = createLabel(quantity);
        Label idLabel = createLabel(id);
        Label dodLabel = createLabel(dod);
        Label statusLabel = createLabel(status);

        supplierPane.getChildren().addAll(
                nameLabel,
                createSpacer(),
                priceLabel,
                createSpacer(),
                quantityLabel,
                createSpacer(),
                idLabel,
                createSpacer(),
                dodLabel,
                createSpacer(),
                statusLabel
        );
        ordersContainer.getChildren().addAll(
                supplierPane,
                createSeparator()
        );
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("child-item");
        label.setMinWidth(100); // Set a desired minimum width for the labels
        return label;
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        separator.setOpacity(0.5);
        separator.setMaxWidth(Double.MAX_VALUE);
        return separator;
    }
}
