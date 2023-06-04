package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Models.Product;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InventoryController {
    QueryHandler queryHandler = new QueryHandler();
    Statement stmt;
    @FXML
    Text onErrorText;
    @FXML
    TableView<Product> tableView;
    @FXML
    TableColumn<Product, String> c1;
    @FXML
    TableColumn<Product, String> c2;
    @FXML
    TableColumn<Product, String> c3;
    @FXML
    TableColumn<Product, String> c4;
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
    TextField supplierController;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane popupPane;

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM products");
        // Process the result set and add products to the productsContainer
        while (resultSet.next()) {
            String productID = resultSet.getString("productID");
            String productName = resultSet.getString("productName");
            String productCategory = resultSet.getString("productCategory");
            String productPrice = resultSet.getString("productPrice");
            String supplierID = resultSet.getString("supplierID");
            String productQuantity = resultSet.getString("productQuantity");

            Product product = new Product(productID, productName, supplierID, productPrice, productQuantity, productCategory);
            Platform.runLater(() -> addProduct(product.getProductID(), product.getProductName(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity(), product.getProductCategory()));

        }
        // Add event listener for delete key press
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                // Get the selected product
                Product selectedProduct = tableView.getSelectionModel().getSelectedItem();

                if (selectedProduct != null) {
                    // Call a method to delete the row from the database
                    String productID = selectedProduct.getProductID();

                    deleteFromDatabase(productID);

                    // Remove the product from the TableView
                    tableView.getItems().remove(selectedProduct);
                }
            }
        });
    }

    @FXML
    void kAddProduct() {
        BoxBlur blur = new BoxBlur(5, 5, 3);
        mainPane.setEffect(blur);

        popupPane.setOpacity(0);
        popupPane.setVisible(true);
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }

    @FXML
    private void onConfirmClicked() {
        Product product = new Product(
                idController.getText(),
                nameController.getText(),
                idController.getText(),
                priceController.getText(),
                quantityController.getText(),
                categoryController.getText()
        );
        boolean status = queryHandler.insertProduct(product.getProductID(), product.getProductName(), product.getProductCategory(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity());
        if (status) {
            onErrorText.setVisible(false);
            addProduct(product.getProductID(), product.getProductName(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity(), product.getProductCategory());
        } else {
            onErrorText.setVisible(true);
            return;
        }
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        fadeOutTransition.play();

        mainPane.setEffect(null);
    }

    @FXML
    private void onDiscardClicked() {
        onErrorText.setVisible(false);
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        fadeOutTransition.play();

        mainPane.setEffect(null);
    }

    public void addProduct(String productID, String productName, String supplierID, String productPrice, String productQuantity, String productCategory) {
        Product product = new Product(productID, productName, supplierID, productPrice, productQuantity, productCategory);
        c1.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().productQuantityProperty());
        c4.setCellValueFactory(cellData -> {
            String stockStatus = Double.parseDouble(cellData.getValue().getProductQuantity()) > 0 ? "In Stock" : "Out of Stock";
            return new SimpleStringProperty(stockStatus);
        });
        tableView.getItems().add(product);
    }


    private void deleteFromDatabase(String id) {
        try {
            String deleteQuery = "DELETE FROM products WHERE productID = '" + id + "'";

            stmt.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exception that occurs during the database operation
        }
    }
}
