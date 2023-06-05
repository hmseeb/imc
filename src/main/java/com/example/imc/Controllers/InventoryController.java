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
    Text categoriesText;
    @FXML
    Text totalProductsText;
    @FXML
    Text revenueText;
    @FXML
    Text costText;
    @FXML
    Text topSelling;
    @FXML
    Text outOfStockText;
    @FXML
    Text ordered;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane popupPane;

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT COUNT(DISTINCT ProductCategory) FROM products;");
        resultSet.next();
        categoriesText.setText(resultSet.getString(1));

        resultSet = stmt.executeQuery("SELECT COUNT(*) FROM products");
        resultSet.next();
        totalProductsText.setText(resultSet.getString(1));

        resultSet = stmt.executeQuery("SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID;");
        resultSet.next();
        revenueText.setText(resultSet.getString(1));

        resultSet = stmt.executeQuery("SELECT SUM(ProductPrice * ProductQuantity) AS Cost FROM Products;");
        resultSet.next();
        costText.setText(resultSet.getString("Cost"));

        resultSet = stmt.executeQuery("SELECT Products.ProductName, SUM(Orders.OrderQuantity) AS TotalQuantity FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY Products.ProductName ORDER BY TotalQuantity DESC LIMIT 10;");
        resultSet.next();
        topSelling.setText(resultSet.getString("ProductName"));

        resultSet = stmt.executeQuery("SELECT COUNT(*) FROM products WHERE ProductQuantity = 0");
        resultSet.next();
        outOfStockText.setText(resultSet.getString(1));

        resultSet = stmt.executeQuery("SELECT Products.ProductName, SUM(Orders.OrderQuantity) AS TotalOrderedQuantity FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY Products.ProductName;");
        resultSet.next();
        ordered.setText(resultSet.getString("ProductName"));

        // Set up the columns in the table
        resultSet = stmt.executeQuery("SELECT * FROM products");
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


