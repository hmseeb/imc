package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Models.Supplier;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierController {
    QueryHandler queryHandler = new QueryHandler();
    Statement stmt;
    @FXML
    TableView<Supplier> tableView;
    @FXML
    TableColumn<Supplier, String> c1;
    @FXML
    TableColumn<Supplier, String> c2;
    @FXML
    TableColumn<Supplier, String> c3;
    @FXML
    TableColumn<Supplier, String> c4;

    @FXML
    TextField nameController;
    @FXML
    TextField idController;
    @FXML
    TextField contactController;
    @FXML
    TextField addressController;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane popupPane;
    // For the add product button in the inventory view
    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM suppliers");

        while (resultSet.next()) {
            String supplierID = resultSet.getString("supplierID");
            String supplierName = resultSet.getString("supplierName");
            String supplierPhone = resultSet.getString("supplierPhone");
            String supplierAddress = resultSet.getString("supplierAddress");

            Platform.runLater(() -> addSupplier(supplierID, supplierName, supplierPhone, supplierAddress));
        }
                // Add event listener for delete key press
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                // Get the selected product
                Supplier selectedProduct = tableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    // Call a method to delete the row from the database
                    String supplierID = selectedProduct.getSupplierID();
                    deleteFromDatabase(supplierID);
                    // Remove the product from the TableView
                    tableView.getItems().remove(selectedProduct);
                }
            }
        });
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
    private void onConfirmClicked() {
        Supplier supplier = new Supplier(
                idController.getText(),
                nameController.getText(),
                contactController.getText(),
                addressController.getText()
        );

        queryHandler.insertSupplier(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getSupplierPhone(), supplier.getSupplierAddress());

        // Animate the popup pane's fade-out and then hide it
        addSupplier(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getSupplierPhone(), supplier.getSupplierAddress());
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

    public void addSupplier(String id, String name, String contact, String address) {
        Supplier supplier = new Supplier(id, name, contact, address);
        c1.setCellValueFactory(cellData -> cellData.getValue().supplierIDProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().supplierPhoneProperty());
        c4.setCellValueFactory(cellData -> cellData.getValue().supplierAddressProperty());

        tableView.getItems().add(supplier);
    }
    private void deleteFromDatabase(String id) {
    try {
        String deleteQuery = String.format("DELETE FROM suppliers WHERE supplierID = %s", id);
        stmt.executeUpdate(deleteQuery);
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle any exception that occurs during the database operation
    }
    }


}