package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.sql.Statement;

public class DashboardController {
    Statement stmt;
    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();
        // Use stmt to execute SQL queries
    }
}
