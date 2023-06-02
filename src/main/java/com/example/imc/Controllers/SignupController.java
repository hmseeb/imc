package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.sql.Statement;

public class SignupController {
    Helpers scene = new Helpers();
    @FXML
    TextField signupNameController;
    @FXML
    TextField signupEmailController;
    @FXML
    PasswordField signupPasswordController;

    @FXML
    public void initialize() throws SQLException {
        Statement stmt = DatabaseHandler.getStatement();
        // Use stmt to execute SQL queries
    }

    @FXML
    void signup(ActionEvent event) throws Exception {
        //TODO: Add validation
        switchToLoginView(event);

    }

    @FXML
    void switchToLoginView(ActionEvent event) throws Exception {
        scene.changeScene(event, "views/login-view.fxml");
    }
}