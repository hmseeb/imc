package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Models.Supplier;
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

public class SupplierController {
    Statement stmt;

    @FXML
    TextField nameController;
    @FXML
    TextField productController;
    @FXML
    TextField contactController;
    @FXML
    TextField priceController;
    @FXML
    TextField typeController;
    @FXML
    TextField categoryController;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane popupPane;
    @FXML
    private VBox suppliersContainer;

    // For the add product button in the inventory view
    @FXML
    public void initialize() throws SQLException {
        String anotherQuery = "ALTER TABLE suppliers MODIFY COLUMN doesReturn VARCHAR(255);\n";
        stmt = DatabaseHandler.getStatement();
        String query = "CREATE TABLE if not exists suppliers (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "supplierName VARCHAR(255) NOT NULL," +
                "productName VARCHAR(255) NOT NULL," +
                "category VARCHAR(255) NOT NULL," +
                "price VARCHAR(255) NOT NULL," +
                "contact INT NOT NULL," +
                "doesReturn VARCHAR(255) NOT NULL" +
                ")";

        stmt.executeUpdate(anotherQuery);
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM suppliers");

        while (resultSet.next()) {
            String name = resultSet.getString("supplierName");
            String product = resultSet.getString("productName");
            String category = resultSet.getString("category");
            String price = resultSet.getString("price");
            String contact = resultSet.getString("contact");
            String doesReturn = resultSet.getString("doesReturn");
            Platform.runLater(() -> addSupplier(name, product, contact, price, String.valueOf(doesReturn)));
        }
        stmt.executeUpdate(query);
    }

    @FXML
    void kAddSupplier() {
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
        Supplier supplier = new Supplier(
                nameController.getText(),
                productController.getText(),
                categoryController.getText(),
                priceController.getText(),
               contactController.getText(),
                typeController.getText()
        );
        String query = "INSERT INTO suppliers (supplierName, productName, category, price, contact, doesReturn) VALUES (" +
                "'" + supplier.getSupplierName()+ "'," +
                "'" + supplier.getProductName() + "'," +
                "'" + supplier.getCategory() + "'," +
                "'" + supplier.getPrice() + "'," +
                "'" + supplier.getContact() + "'," +
                "'" + supplier.getDoesReturn() + "'" +
                ")";
        stmt.executeUpdate(query);
        addSupplier(supplier.getSupplierName(), supplier.getProductName(),String.valueOf(supplier.getContact()), supplier.getPrice(), String.valueOf(supplier.getDoesReturn()));
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

    public void addSupplier(String name, String product, String contact, String price, String type) {
        HBox supplierPane = new HBox();
        supplierPane.getStyleClass().add("supplier-pane");
        supplierPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel(name);
        Label productLabel = createLabel(product);
        Label contactNumberLabel = createLabel(contact);
        Label priceLabel = createLabel(price);
        Label typeLabel = createLabel(type);

        supplierPane.getChildren().addAll(
                nameLabel,
                createSpacer(),
                productLabel,
                createSpacer(),
                contactNumberLabel,
                createSpacer(),
                priceLabel,
                createSpacer(),
                typeLabel
        );

        suppliersContainer.getChildren().addAll(
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