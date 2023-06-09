package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Helpers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuController {

    Helpers view = new Helpers();
    Statement stmt;

    @FXML
    Text quantityText, outOfStockText, suppliersText, categoriesText,
            salesSalesText, salesProfitText, salesRevenueText, salesCostText,
            purchaseSalesText, purchaseProfitText, purchaseRevenueText, purchaseCostText;

    @FXML
    BarChart<String, Number> barChart;
    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    private Pane viewPane;

    private String currentView = "dashboard-view.fxml";

    @FXML
    void switchToDashboard() throws Exception {
        if (!currentView.equals("dashboard-view.fxml")) {
            view.changeView(viewPane, "views/dashboard-view.fxml");
            currentView = "dashboard-view.fxml";
        }
    }

    @FXML
    void switchToInventory() throws Exception {
        if (!currentView.equals("inventory-view.fxml")) {
            view.changeView(viewPane, "views/inventory-view.fxml");
            currentView = "inventory-view.fxml";
        }
    }

    @FXML
    void switchToReports() throws Exception {
        if (!currentView.equals("report-view.fxml")) {
            view.changeView(viewPane, "views/report-view.fxml");
            currentView = "report-view.fxml";
        }
    }

    @FXML
    void switchToSuppliers() throws Exception {
        if (!currentView.equals("supplier-view.fxml")) {
            view.changeView(viewPane, "views/supplier-view.fxml");
            currentView = "supplier-view.fxml";
        }
    }

    @FXML
    void switchToOrders() throws Exception {
        if (!currentView.equals("order-view.fxml")) {
            view.changeView(viewPane, "views/order-view.fxml");
            currentView = "order-view.fxml";
        }
    }

    @FXML
    void logout(ActionEvent event) throws Exception {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to logout?");
        confirmationDialog.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations

        // Show the confirmation dialog and wait for user response
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        // Check if the user confirmed the deletion
        if (result.isPresent() && result.get() == ButtonType.OK)
            view.changeScene(event, "views/login-view.fxml");
    }

    @FXML

    public void initialize() throws SQLException {

        initLineChart();
        initBarChart();

        stmt = DatabaseHandler.getStatement();

        QueryHandler.updateStats("SELECT COUNT(*) FROM products", quantityText);
        QueryHandler.updateStats("SELECT COUNT(*) FROM products WHERE ProductQuantity = 0", outOfStockText);
        QueryHandler.updateStats("SELECT COUNT(*) FROM suppliers", suppliersText);
        QueryHandler.updateStats("SELECT COUNT(DISTINCT ProductCategory) FROM products", categoriesText);
        QueryHandler.updateStats("SELECT SUM(OrderQuantity) FROM Orders;", salesSalesText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID;", salesRevenueText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * ProductQuantity) FROM Products;", salesCostText);
        QueryHandler.updateStats("""
                SELECT (SELECT SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID) - (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) AS Profit;

                """, salesProfitText);
        QueryHandler.updateStats("SELECT SUM(ProductQuantity) AS Purchases FROM Products;", purchaseSalesText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * ProductQuantity) FROM Products;", purchaseRevenueText);
        QueryHandler.updateStats("SELECT SUM(ProductPrice * ProductQuantity) FROM Products;", purchaseCostText);
        QueryHandler.updateStats("""
                SELECT (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) - (SELECT SUM(ProductPrice * ProductQuantity) FROM Products) AS Profit;

                """, purchaseProfitText);
    }

    void initLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        lineChart.getXAxis().setLabel("Month");
        lineChart.getYAxis().setLabel("Revenue");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try {
            stmt = DatabaseHandler.getStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT SUBSTRING(OrderDate, 1, 7), SUM(ProductPrice * OrderQuantity) FROM Products JOIN Orders ON Products.ProductID = Orders.ProductID GROUP BY SUBSTRING(OrderDate, 1, 7)");

            while (resultSet.next()) {
                String month = resultSet.getString(1);
                double revenue = resultSet.getDouble(2);
                series.getData().add(new XYChart.Data<>(month, revenue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set explicit categories for the x-axis
        xAxis.setCategories(FXCollections.observableArrayList(series.getData().stream().map(XYChart.Data::getXValue).collect(Collectors.toList())));

        lineChart.getData().add(series);
    }


    void initBarChart() {
        barChart.setCategoryGap(10);
        barChart.setBarGap(5);
        barChart.getXAxis().setLabel("Category");
        barChart.getYAxis().setLabel("Quantity");
        barChart.getXAxis().setTickLabelsVisible(true);
        barChart.getYAxis().setTickLabelsVisible(true);

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();

        try {
            stmt = DatabaseHandler.getStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT ProductCategory, COUNT(*) FROM products GROUP BY ProductCategory");

            while (resultSet.next()) {
                String category = resultSet.getString(1);
                int count = resultSet.getInt(2);
                barSeries.getData().add(new XYChart.Data<>(category, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        barChart.getData().add(barSeries);
    }

}
