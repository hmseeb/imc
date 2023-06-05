package com.example.imc.Controllers;

import com.example.imc.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MenuController {

    Helpers view = new Helpers();
    @FXML
    private Pane viewPane;

    private String currentView = "";

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
        view.changeScene(event, "views/login-view.fxml");
    }

}
