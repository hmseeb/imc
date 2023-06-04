package com.example.imc.Controllers;

import com.example.imc.Handlers.QueryHandler;
import javafx.fxml.FXML;

public class DashboardController {
    QueryHandler queryHandler = new QueryHandler();
    @FXML
    public void initialize() {
        queryHandler.dropTables();
        queryHandler.createTables();
        // Use stmt to execute SQL queries
    }
}
