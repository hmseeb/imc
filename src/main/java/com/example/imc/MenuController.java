package com.example.imc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuController extends Helper {
    @FXML
    void switchToDashboard() throws Exception {
        changeView( "views/dashboard-view.fxml");
    }
    @FXML
    void switchToInventory() throws Exception{
        changeView("views/inventory-view.fxml");
    }
    @FXML
    void switchToReports() throws Exception{
        changeView("views/reports-view.fxml");
    }

    @FXML
    void switchToSuppliers() throws Exception{
        changeView("views/suppliers-view.fxml");
    }

    @FXML
    void switchToOrders() throws Exception{
        changeView("views/orders-view.fxml");
    }

    @FXML
    void switchToManageStores() throws Exception{
        changeView("views/manage-stores-view.fxml");
    }

    @FXML
    void logout (ActionEvent event) throws Exception{
        changeScene(event, "views/login-view.fxml");
    }


}
