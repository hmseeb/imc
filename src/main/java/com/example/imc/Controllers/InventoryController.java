package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Models.Product;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class InventoryController {
    QueryHandler queryHandler = new QueryHandler();
    Statement stmt;
    @FXML
    Text onErrorText, categoriesText, totalProductsText, revenueText, costText, topSelling, outOfStockText, ordered, onErrorText1;
    @FXML
    TableView<Product> tableView;
    @FXML
    TableColumn<Product, String> c1, c2, c3, c4;
    @FXML
    TextField nameController, idController, quantityController, priceController, categoryController, supplierController, nameController1, categoryController1, supplierController1, priceController1, quantityController1, idController1;
    @FXML
    private Pane mainPane, popupPane, popupPane1;

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();

        QueryHandler.updateStats("SELECT COUNT(DISTINCT ProductCategory) FROM products;", categoriesText);
        QueryHandler.updateStats("SELECT COUNT(*) FROM products", totalProductsText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID;", revenueText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * ProductQuantity) AS Cost FROM Products;", costText);
        QueryHandler.updateStats("SELECT Products.ProductName, SUM(Orders.OrderQuantity) AS TotalQuantity FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY Products.ProductName ORDER BY TotalQuantity DESC LIMIT 10;", topSelling);
        QueryHandler.updateStats("SELECT COUNT(*) FROM products WHERE ProductQuantity = 0", outOfStockText);
        QueryHandler.updateStats("SELECT Products.ProductName, SUM(Orders.OrderQuantity) AS TotalOrderedQuantity FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY Products.ProductName;", ordered);

        // Set up the columns in the table
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
                    // Create a confirmation dialog
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation");
                    confirmationDialog.setHeaderText(null);
                    confirmationDialog.setContentText("Are you sure you want to delete this product from the database?");
                    confirmationDialog.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations

                    // Show the confirmation dialog and wait for user response
                    Optional<ButtonType> result = confirmationDialog.showAndWait();

                    // Check if the user confirmed the deletion
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean status = deleteFromDatabase(productID);
                        if (status) {
                            // Remove the product from the TableView
                            tableView.getItems().remove(selectedProduct);
                        } else {
                            String errorMessage = "An error occurred while deleting " + selectedProduct.getProductName() + " from the database";
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText(errorMessage);
                            alert.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations
                            alert.showAndWait();
                        }
                    }


                }
            } else if (event.getCode() == KeyCode.ENTER) {
                Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    String id = selectedProduct.getProductID();
                    idController1.setText(id);
                    nameController1.setText(selectedProduct.getProductName());
                    categoryController1.setText(selectedProduct.getProductCategory());
                    supplierController1.setText(selectedProduct.getSupplierID());
                    priceController1.setText(selectedProduct.getProductPrice());
                    quantityController1.setText(selectedProduct.getProductQuantity());
                    idController1.setEditable(false);
                    popupPane1.setVisible(true);
                    kEditProduct();
                }

            }
        });
    }

    @FXML
    void kAddProduct() {
        BoxBlur blur = new BoxBlur(5, 5, 3);
        mainPane.setEffect(blur);
        popupPane.setVisible(true);
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }

    private void kEditProduct() {
        BoxBlur blur = new BoxBlur(5, 5, 3);
        mainPane.setEffect(blur);
        popupPane1.setVisible(true);
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(300), popupPane1);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }

    @FXML
    private void onEditConfirmClicked() {
        Product product = new Product(
                idController1.getText(),
                nameController1.getText(),
                supplierController1.getText(),
                priceController1.getText(),
                quantityController1.getText(),
                categoryController1.getText()
        );
        boolean status = editProductDetails(product);
        if (status) {
            onErrorText1.setVisible(false);
            updateProduct(product);
        } else {
            Popup popup = new Popup();
            popup.show(mainPane.getScene().getWindow());
            onErrorText1.setVisible(true);
            return;
        }
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane1.setVisible(false));
        fadeOutTransition.play();
        mainPane.setEffect(null);
    }

    @FXML
    private void onConfirmClicked() {
        Product product = new Product(
                idController.getText(),
                nameController.getText(),
                supplierController1.getText(),
                priceController.getText(),
                quantityController.getText(),
                categoryController.getText()
        );

        boolean status = queryHandler.insertProduct(product.getProductID(), product.getProductName(), product.getProductCategory(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity());
        if (status) {
            onErrorText.setVisible(false);
            addProduct(product.getProductID(), product.getProductName(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity(), product.getProductCategory());
        } else {
            Popup popup = new Popup();
            popup.show(mainPane.getScene().getWindow());
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
        FadeTransition fadeOutTransition;

        if (popupPane.visibleProperty().getValue()) {
            onErrorText.setVisible(false);
            fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
            fadeOutTransition.setToValue(0);
            fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        } else {
            onErrorText1.setVisible(false);
            fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane1);
            fadeOutTransition.setToValue(0);
            fadeOutTransition.setOnFinished(event -> popupPane1.setVisible(false));
        }
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

    private boolean editProductDetails(Product product) {
        try {
            String updateQuery = "UPDATE products SET productName = '" + product.getProductName() + "', productCategory = '" + product.getProductCategory() + "', supplierID = '" + product.getSupplierID() + "', productPrice = " + product.getProductPrice() + ", productQuantity = " + product.getProductQuantity() + " WHERE productID = '" + product.getProductID() + "'";
            stmt.executeUpdate(updateQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            // Handle any exception that occurs during the database operation
        }
    }

    private void updateProduct(Product updatedProduct) {
        // Update the product in the TableView
        for (int i = 0; i < tableView.getItems().size(); i++) {
            Product product = tableView.getItems().get(i);
            if (product.getProductID().equals(updatedProduct.getProductID())) {
                tableView.getItems().set(i, updatedProduct);
                break;
            }
        }
    }

    private boolean deleteFromDatabase(String id) {
        try {
            String deleteQuery = "DELETE FROM products WHERE productID = '" + id + "'";
            stmt.executeUpdate(deleteQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            // Handle any exception that occurs during the database operation
        }
    }
}
