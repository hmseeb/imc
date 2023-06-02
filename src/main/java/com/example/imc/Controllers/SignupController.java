package com.example.imc.Controllers;

import com.example.imc.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
    Helper scene = new Helper();
    @FXML
    TextField signupNameController;
    @FXML
    TextField signupEmailController;
    @FXML
    PasswordField signupPasswordController;

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