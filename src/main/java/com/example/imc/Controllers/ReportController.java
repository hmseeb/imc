package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Models.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportController {
    Statement stmt;

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
            System.out.println(product.getSupplierID());
            Platform.runLater(() -> addProduct(product.getProductID(), product.getProductName(), product.getSupplierID(), product.getProductPrice(), product.getProductQuantity(), product.getProductCategory()));
            // Use stmt to execute SQL queries
        }

    }
        public void addProduct (String productID, String productName, String supplierID, String productPrice, String productQuantity, String productCategory){
            Product product = new Product(productID, productName, supplierID, productPrice, productQuantity, productCategory);
            c1.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
            c2.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
            c3.setCellValueFactory(cellData -> cellData.getValue().productCategoryProperty());
            c4.setCellValueFactory(cellData -> {
                String stockStatus = Double.parseDouble(cellData.getValue().getProductQuantity()) > 0 ? "In Stock" : "Out of Stock";
                return new SimpleStringProperty(stockStatus);
            });
            tableView.getItems().add(product);
        }
}
