package com.example.imc.Controllers;
import com.example.imc.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MenuController {
    @FXML
    private Pane viewPane;
    Helper view = new Helper();
    @FXML
    void switchToDashboard() throws Exception {
        view.changeView(viewPane,"views/dashboard-view.fxml");
    }

    @FXML
    void switchToInventory() throws Exception {
        view.changeView(viewPane,"views/inventory-view.fxml");
    }

    @FXML
    void switchToReports() throws Exception {
        view.changeView(viewPane,"views/report-view.fxml");
    }

    @FXML
    void switchToSuppliers() throws Exception {
        view.changeView(viewPane,"views/supplier-view.fxml");
    }

    @FXML
    void switchToOrders() throws Exception {
        view.changeView(viewPane,"views/order-view.fxml");
    }

    @FXML
    void switchToManageStores() throws Exception {
        view.changeView(viewPane,"views/manage-stores-view.fxml");
    }

    @FXML
    void logout(ActionEvent event) throws Exception {
        view.changeScene(event, "views/login-view.fxml");
    }


}
