package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import com.example.imc.Helpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    Statement stmt;
    Helpers scene = new Helpers();
    @FXML
    TextField loginEmailController;
    @FXML
    PasswordField loginPasswordController;
    @FXML
    Text loginErrorController;

    @FXML
    public void initialize() throws SQLException {
        stmt = DatabaseHandler.getStatement();
        // Use stmt to execute SQL queries
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        if (loginEmailController.getText().equals("admin") && loginPasswordController.getText().equals("admin")) {
            loginEmailController.setText("");
            scene.changeScene(event, "views/menu-view.fxml");

        } else {
            loginErrorController.setText("Invalid credentials");
        }
    }

    @FXML
    void switchToSignupView(ActionEvent event) throws Exception {
        scene.changeScene(event, "views/signup-view.fxml");
    }
}
