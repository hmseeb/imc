package com.example.imc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController extends Helper {
    @FXML
    TextField signupNameController;
    @FXML
    TextField signupEmailController;
    @FXML
    PasswordField signupPasswordController;
    @FXML
    void signup(ActionEvent event) throws Exception{
        //TODO: Add validation
        switchToLoginView(event);

    }

    @FXML
    void switchToLoginView(ActionEvent event) throws Exception {
        changeScene(event, "views/login-view.fxml");
    }
}