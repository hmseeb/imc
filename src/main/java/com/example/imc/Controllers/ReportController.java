package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Models.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportController {
    Statement stmt;

    @FXML
    TableView<Product> tableView;
    @FXML
    TableColumn<Product, String> c1, c2, c3, c4;
    @FXML
    BarChart<String, Number> barChart2;
    @FXML
    Text revenue, sales, profit, netPurchases, netSales, moMProfit, yoYProfit, cat1, cat2, cat3, pro1, pro2, pro3;


    @FXML
    public void initialize() throws SQLException {

        initChart();

        stmt = DatabaseHandler.getStatement();

        ResultSet resultSet;
        QueryHandler.updateStats("SELECT SUM(OrderQuantity) FROM Orders;", sales);
        QueryHandler.updateStats("SELECT SUM(OrderQuantity * ProductPrice) FROM Orders o INNER JOIN Products p ON o.ProductID = p.ProductID;", revenue);
        QueryHandler.updateStats("""
                SELECT (SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID) - (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) AS Profit;

                """, profit);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * ProductQuantity) FROM Products;", netPurchases);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID;", netSales);
        QueryHandler.updateStats("SELECT (SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID) - (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) AS MoMProfit;", moMProfit);
        QueryHandler.updateStats("SELECT (SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID) - (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) AS YoYProfit;", yoYProfit);

        int index = 0;
        resultSet = stmt.executeQuery("SELECT ProductCategory, COUNT(*) AS CategoryCount FROM Products GROUP BY ProductCategory ORDER BY CategoryCount DESC LIMIT 3;");

        while (resultSet.next() && index < 3) {
            String category = resultSet.getString("ProductCategory");
            int count = resultSet.getInt("CategoryCount");

            switch (index) {
                case 0 -> cat1.setText(category + " (" + count + ")");
                case 1 -> cat2.setText(category + " (" + count + ")");
                case 2 -> cat3.setText(category + " (" + count + ")");
            }

            index++;
        }

        resultSet = stmt.executeQuery("SELECT ProductCategory, SUM(ProductQuantity) AS TotalQuantity FROM Products GROUP BY ProductCategory ORDER BY TotalQuantity DESC LIMIT 3;");
        index = 0;

        while (resultSet.next() && index < 3) {
            int count = resultSet.getInt("TotalQuantity");

            switch (index) {
                case 0 -> pro1.setText(String.valueOf(count));
                case 1 -> pro2.setText(String.valueOf(count));
                case 2 -> pro3.setText(String.valueOf(count));
            }

            index++;
        }

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
            // Use stmt to execute SQL queries
        }

    }

    public void addProduct(String productID, String productName, String supplierID, String productPrice, String productQuantity, String productCategory) {
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


    void initChart() {

        barChart2.getXAxis().setLabel("Category");
        barChart2.getYAxis().setLabel("Quantity");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try {
            stmt = DatabaseHandler.getStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT ProductCategory, SUM(OrderQuantity) AS SalesQuantity FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY ProductCategory");

            while (resultSet.next()) {
                String category = resultSet.getString("ProductCategory");
                int quantity = resultSet.getInt("SalesQuantity");
                series.getData().add(new XYChart.Data<>(category, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        barChart2.getData().add(series);
    }
}
