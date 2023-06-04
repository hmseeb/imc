package com.example.imc.Controllers;

import com.example.imc.Handlers.QueryHandler;
import javafx.fxml.FXML;

public class DashboardController {
    QueryHandler queryHandler = new QueryHandler();
    @FXML
    public void initialize() {
        queryHandler.executeQuery("SET FOREIGN_KEY_CHECKS=0;");
        // Use stmt to execute SQL queries
    }
}
