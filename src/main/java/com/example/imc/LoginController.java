package com.example.imc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController extends Helper {
    @FXML
    TextField loginEmailController;
    @FXML
    PasswordField loginPasswordController;
    @FXML
    Text loginErrorController;
    @FXML
    void login (ActionEvent event) throws Exception {
        if (loginEmailController.getText().equals("admin") && loginPasswordController.getText().equals("admin"))
        { loginEmailController.setText("");
            changeScene(event,"views/menu-view.fxml");
        } else {
            loginErrorController.setText("Invalid credentials");
        }
    }
    @FXML
   void switchToSignupView(ActionEvent event) throws Exception {
        changeScene(event, "views/signup-view.fxml");
}
}
