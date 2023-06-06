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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class SupplierController {
    QueryHandler queryHandler = new QueryHandler();
    Statement stmt;

    @FXML
    Text onErrorText;
    @FXML
    TableView<Supplier> tableView;
    @FXML
    TableColumn<Supplier, String> c1, c2, c3, c4;
    @FXML
    TextField nameController, idController, contactController, addressController;
    @FXML
    private Pane mainPane, popupPane;


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

            Supplier supplier = new Supplier(supplierID, supplierName, supplierPhone, supplierAddress);
            Platform.runLater(() -> addSupplier(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getSupplierPhone(), supplier.getSupplierAddress()));
        }
        // Add event listener for delete key press
        tableView.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                        // Get the selected product
                        Supplier selectedOrder = tableView.getSelectionModel().getSelectedItem();
                        if (selectedOrder != null) {
                            // Call a method to delete the row from the database
                            String supplierID = selectedOrder.getSupplierID();

                            // Create a confirmation dialog
                            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationDialog.setTitle("Confirmation");
                            confirmationDialog.setHeaderText(null);
                            confirmationDialog.setContentText("Are you sure you want to delete this supplier from the database?");
                            confirmationDialog.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations

                            // Show the confirmation dialog and wait for user response
                            Optional<ButtonType> result = confirmationDialog.showAndWait();

                            // Check if the user confirmed the deletion
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                boolean status = deleteFromDatabase(supplierID);
                                if (status) {
                                    tableView.getItems().remove(selectedOrder);
                                } else {
                                    String errorMessage = "An error occurred while deleting " + selectedOrder.getSupplierName() + " from the database";
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText(null);
                                    alert.setContentText(errorMessage);
                                    alert.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations
                                    alert.showAndWait();
                                }
                            }
                        }
                        // Remove the product from the TableView
                    }
                }
        );
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
        if (supplierExists(supplier.getSupplierID())) {
            boolean status = editSupplierDetails(supplier);
            if (status) {
                onErrorText.setVisible(false);
                updateSupplier(supplier);
            } else {
                Popup popup = new Popup();
                popup.show(mainPane.getScene().getWindow());
                onErrorText.setVisible(true);
                return;
            }
        } else {
            boolean status = queryHandler.insertSupplier(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getSupplierPhone(), supplier.getSupplierAddress());
            if (status) {
                onErrorText.setVisible(false);
                addSupplier(supplier.getSupplierID(), supplier.getSupplierName(), supplier.getSupplierPhone(), supplier.getSupplierAddress());
            } else {
                onErrorText.setVisible(true);
                return;
            }
        }
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
        onErrorText.setVisible(false);
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

    private boolean supplierExists(String supplierID) {
        // Check if the product already exists in the TableView
        for (Supplier supplier : tableView.getItems()) {
            if (supplier.getSupplierID().equals(supplierID)) {
                return true;
            }
        }
        return false;
    }

    private boolean editSupplierDetails(Supplier supplier) {
        try {
            String updateQuery = "UPDATE Suppliers SET SupplierName = ?, SupplierAddress = ?, SupplierPhone = ? WHERE SupplierID = ?;";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(updateQuery);
            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getSupplierAddress());
            statement.setString(3, supplier.getSupplierPhone());
            statement.setInt(4, Integer.parseInt(supplier.getSupplierID()));
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            // Handle any exception that occurs during the database operation
        }
    }

    private void updateSupplier(Supplier updatedSupplier) {
        // Update the product in the TableView
        for (int i = 0; i < tableView.getItems().size(); i++) {
            Supplier supplier = tableView.getItems().get(i);
            if (supplier.getSupplierID().equals(updatedSupplier.getSupplierID())) {
                tableView.getItems().set(i, updatedSupplier);
                break;
            }
        }
    }

    private boolean deleteFromDatabase(String id) {
        try {
            String deleteQuery = "DELETE FROM suppliers WHERE supplierID = '" + id + "'";
            stmt.executeUpdate(deleteQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            // Handle any exception that occurs during the database operation
        }
    }
}