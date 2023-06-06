package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Handlers.QueryHandler;
import com.example.imc.Models.Order;
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

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class OrderController {
    QueryHandler queryHandler = new QueryHandler();
    Statement stmt;

    @FXML
    Text onErrorText, totalOrders, totalSold, revenue, productName, totalQuantity, lastOrderedDate, lastOrderedProduct;
    @FXML
    TableView<Order> tableView;
    @FXML
    TableColumn<Order, String> c1, c2, c3, c4;
    @FXML
    TextField orderIDController, productIDController, quantityController;
    @FXML
    private Pane mainPane, popupPane;


    // For the add product button in the inventory view

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();

        QueryHandler.updateStats("SELECT COUNT(*) FROM Orders;", totalOrders);
        QueryHandler.updateStats("SELECT SUM(OrderQuantity) FROM Orders;", totalSold);
        QueryHandler.updateStats("SELECT SUM(p.ProductPrice * o.OrderQuantity) " +
                "FROM Orders o INNER JOIN Products p ON o.ProductID = p.ProductID;", revenue);

        ResultSet resultSet = stmt.executeQuery("SELECT p.ProductName, o.OrderDate " +
                "FROM Orders o JOIN Products p ON o.ProductID = p.ProductID " +
                "WHERE o.OrderDate = (SELECT MAX(OrderDate) FROM Orders);");

        if (resultSet.next()) {
            String lastOrderedProduct = resultSet.getString("ProductName");
            lastOrderedProduct = lastOrderedProduct.length() > 6 ? lastOrderedProduct.substring(0, 6) + "..." : lastOrderedProduct;

            Date lastOrderDate = resultSet.getDate("OrderDate");
            this.lastOrderedProduct.setText(lastOrderedProduct);
            this.lastOrderedDate.setText(lastOrderDate.toString());
        }

        resultSet = stmt.executeQuery("SELECT p.ProductName, SUM(o.OrderQuantity) AS TotalQuantity " +
                "FROM Orders o JOIN Products p ON o.ProductID = p.ProductID " +
                "GROUP BY p.ProductName " +
                "ORDER BY TotalQuantity DESC " +
                "LIMIT 1;");

        if (resultSet.next()) {
            String productName = resultSet.getString("ProductName");
            productName = productName.length() > 6 ? productName.substring(0, 6) + "..." : productName;
            int totalQuantity = resultSet.getInt("TotalQuantity");
            this.productName.setText(productName);
            this.totalQuantity.setText(String.valueOf(totalQuantity));
        }

        resultSet = stmt.executeQuery("select * from orders");

        while (resultSet.next()) {
            String orderID = resultSet.getString("OrderID");
            String productID = resultSet.getString("ProductID");
            String quantity = resultSet.getString("OrderQuantity");
            String orderDate = resultSet.getString("OrderDate");
            Platform.runLater(() -> addOrder(orderID, productID, quantity, orderDate));
        }
        // Add event listener for delete key press
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                // Get the selected product
                Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    // Call a method to delete the row from the database
                    String orderID = selectedOrder.getOrderID();

                    // Create a confirmation dialog
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation");
                    confirmationDialog.setHeaderText(null);
                    confirmationDialog.setContentText("Are you sure you want to delete this order from the database?");
                    confirmationDialog.initStyle(StageStyle.UNDECORATED); // Optional: Removes the default window decorations

                    // Show the confirmation dialog and wait for user response
                    Optional<ButtonType> result = confirmationDialog.showAndWait();

                    // Check if the user confirmed the deletion
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean status = deleteFromDatabase(orderID);
                        if (status) {
                            tableView.getItems().remove(selectedOrder);
                        } else {
                            String errorMessage = "Error deleting the order from the database";
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText(errorMessage);
                            alert.showAndWait();
                        }
                    }
                    // Remove the product from the TableView
                }
            }
        });
    }

    @FXML
    private void kAddOrder() {
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
        String orderID = orderIDController.getText();
        String productID = productIDController.getText();
        String quantity = quantityController.getText();
        LocalDateTime dateTime = LocalDateTime.now();

        // Create the Order object
        Order order = new Order(orderID, dateTime.toString(), productID, quantity);
        if (orderExists(order.getOrderID())) {
            boolean status = editOrderDetails(order);
            if (status) {
                onErrorText.setVisible(false);
                updateOrder(order);
            } else {
                Popup popup = new Popup();
                popup.show(mainPane.getScene().getWindow());
                onErrorText.setVisible(true);
                return;
            }
        } else {
            Date currentDate = Calendar.getInstance().getTime();
            boolean status = queryHandler.insertOrder(order.getOrderID(), order.getOrderDate(currentDate), order.getProductID(), order.getOrderQuantity());
            // Add the order to the UI
            if (status) {
                onErrorText.setVisible(false);
                addOrder(order.getOrderID(), order.getProductID(), order.getOrderQuantity(), order.getOrderDate(currentDate));
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

    public void addOrder(String orderID, String productID, String quantity, String dateTime) {
        // Create the custom row
        Order order = new Order(orderID, dateTime, productID, quantity);
        c1.setCellValueFactory(cellData -> cellData.getValue().orderIDProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().orderQuantityProperty());
        c4.setCellValueFactory(cellData -> new SimpleStringProperty(dateTime.split(" ")[0]));
        // Add the custom row to the table
        tableView.getItems().add(order);

    }

    private boolean orderExists(String orderID) {
        // Check if the product already exists in the TableView
        for (Order order : tableView.getItems()) {
            if (order.getOrderID().equals(orderID)) {
                return true;
            }
        }
        return false;
    }

    private boolean editOrderDetails(Order order) {
        try {
            Date currentDate = Calendar.getInstance().getTime();
            Timestamp timestamp = new Timestamp(currentDate.getTime());
            String updateQuery = "UPDATE Orders SET OrderDate = ?, ProductID = ?, OrderQuantity = ? WHERE OrderID = ?;";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(updateQuery);
            statement.setTimestamp(1, timestamp);
            statement.setInt(2, Integer.parseInt(order.getProductID()));
            statement.setInt(3, Integer.parseInt(order.getOrderQuantity()));
            statement.setInt(4, Integer.parseInt(order.getOrderID()));
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            // Handle any exception that occurs during the database operation
        }
    }

    private void updateOrder(Order updatedOrder) {
        // Update the product in the TableView
        for (int i = 0; i < tableView.getItems().size(); i++) {
            Order order = tableView.getItems().get(i);
            if (order.getOrderID().equals(updatedOrder.getOrderID())) {
                tableView.getItems().set(i, updatedOrder);
                break;
            }
        }
    }

    private boolean deleteFromDatabase(String id) {
        try {
            String deleteQuery = "DELETE FROM orders WHERE orderID = '" + id + "'";
            stmt.executeUpdate(deleteQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
