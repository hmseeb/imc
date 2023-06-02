package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Models.Product;
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

public class InventoryController {
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
    private Pane mainPane;

    @FXML
    private Pane popupPane;
    @FXML
    private VBox productsContainer;

    // For the add product button in the inventory view
    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();
        String query = "CREATE TABLE if not exists products (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "productName VARCHAR(255) NOT NULL," +
                "productID INT NOT NULL," +
                "productCategory VARCHAR(255) NOT NULL," +
                "price INT NOT NULL," +
                "quantity INT NOT NULL," +
                "date DATE NOT NULL," +
                "unit INT NOT NULL," +
                "thresholdValue INT NOT NULL" +
                ")";

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM products");

        // Process the result set and add products to the productsContainer
        while (resultSet.next()) {
            String productName = resultSet.getString("productName");
            int price = resultSet.getInt("price");
            int quantity = resultSet.getInt("quantity");
            String date = resultSet.getString("date");
            boolean isAvailable = quantity > 0;

            Platform.runLater(() -> addProduct(productName, String.valueOf(price), String.valueOf(quantity), date, isAvailable));
        }
        stmt.executeUpdate(query);
        // Use stmt to execute SQL queries
    }

    @FXML
    void kAddProduct() {
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
    private void onConfirmClicked() throws SQLException{
        // TODO Add additional logic here
        Product product = new Product(
                nameController.getText(),
                Integer.parseInt(idController.getText()),
                categoryController.getText(),
                Integer.parseInt(valueController.getText()),
                Integer.parseInt(quantityController.getText()),
                dateController.getText(),
                Integer.parseInt(unitController.getText()),
                50
        );

        stmt.executeUpdate("INSERT INTO products (productName, productID, productCategory, price, quantity, date, unit, thresholdValue) VALUES ('" + product.getProductName() + "', '" + product.getProductID() + "', '" + product.getProductCategory() + "', '" + product.getPrice() + "', '" + product.getQuantity() + "', '" + product.getDate() + "', '" + product.getUnit() + "', '" + product.getThresholdValue() + "')");

        addProduct(
                product.getProductName(),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getQuantity()),
                product.getDate(),
                product.getQuantity() > 0);

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

    public void addProduct(String name, String price, String quantity, String date, boolean isAvailable) {
        HBox productPane = new HBox();
        productPane.getStyleClass().add("supplier-pane");
        productPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel(name);
        Label priceLabel = createLabel(price);
        Label quantityLabel = createLabel(quantity);
        Label dateLabel = createLabel(date);
        Label availablityLabel = createLabel((isAvailable ? "In stock" : "Out of stock"));
        productPane.getChildren().addAll(
                nameLabel,
                createSpacer(),
                priceLabel,
                createSpacer(),
                quantityLabel,
                createSpacer(),
                dateLabel,
                createSpacer(),
                availablityLabel
        );

        productsContainer.getChildren().addAll(
                productPane,
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
