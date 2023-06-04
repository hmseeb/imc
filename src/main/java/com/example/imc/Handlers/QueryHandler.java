package com.example.imc.Handlers;

import java.sql.*;

public class QueryHandler {
    Connection con;
    Statement stmt;
    String suppliersTableQuery = "CREATE TABLE Suppliers (" +
            "SupplierID INT PRIMARY KEY," +
            "SupplierName VARCHAR(255) NOT NULL," +
            "SupplierAddress VARCHAR(255)," +
            "SupplierPhone VARCHAR(20)" +
            ");";

    String productsTableQuery = "CREATE TABLE Products (" +
            "ProductID INT PRIMARY KEY," +
            "ProductName VARCHAR(255) NOT NULL," +
            "ProductCategory VARCHAR(255)," +
            "SupplierID INT," +
            "ProductPrice DECIMAL(10, 2)," +
            "ProductQuantity INT," +
            "CONSTRAINT fk_SupplierID FOREIGN KEY (SupplierID) REFERENCES Suppliers(SupplierID)" +
            ");";

    String ordersTableQuery = "CREATE TABLE Orders (" +
            "OrderID INT PRIMARY KEY," +
            "OrderDate DATE," +
            "ProductID INT," +
            "OrderQuantity INT," +
            "CONSTRAINT fk_ProductID FOREIGN KEY (ProductID) REFERENCES Products(ProductID)" +
            ");";


    public void createTables() {
        try {
            stmt = DatabaseHandler.getStatement();
//            stmt.executeUpdate(suppliersTableQuery);
//            stmt.executeUpdate(productsTableQuery);
            stmt.executeUpdate(ordersTableQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(String query) {
        try {
            stmt = DatabaseHandler.getStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropTables() {
        try {
            stmt = DatabaseHandler.getStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS Orders");
//            stmt.executeUpdate("DROP TABLE IF EXISTS Products");
//            stmt.executeUpdate("DROP TABLE IF EXISTS Suppliers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertSupplier(String supplierID, String supplierName, String supplierAddress, String supplierPhone) {
        String insertSupplierQuery = "INSERT INTO Suppliers (SupplierID, SupplierName, SupplierAddress, SupplierPhone) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(insertSupplierQuery)) {
            statement.setInt(1, Integer.parseInt(supplierID));
            statement.setString(2, supplierName);
            statement.setString(3, supplierAddress);
            statement.setString(4, supplierPhone);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertProduct(String productID, String productName, String productCategory, String supplierID, String productPrice, String productQuantity) {
        String insertProductQuery = "INSERT INTO Products (ProductID, ProductName, ProductCategory, SupplierID, ProductPrice, ProductQuantity) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(insertProductQuery)) {
            statement.setInt(1, Integer.parseInt(productID));
            statement.setString(2, productName);
            statement.setString(3, productCategory);
            statement.setInt(4, Integer.parseInt(supplierID));
            statement.setInt(5, Integer.parseInt(productPrice));
            statement.setInt(6, Integer.parseInt(productQuantity));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean insertOrder(String orderID, String orderDate, String productID, String orderQuantity) {
        String insertOrderQuery = "INSERT INTO Orders (OrderID, OrderDate, ProductID, OrderQuantity) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(insertOrderQuery)) {
            statement.setInt(1, Integer.parseInt(orderID));
            statement.setTimestamp(2, Timestamp.valueOf(orderDate));
            statement.setInt(3, Integer.parseInt(productID));
            statement.setInt(4, Integer.parseInt(orderQuantity));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
