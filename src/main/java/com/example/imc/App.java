package com.example.imc;

import com.example.imc.Handlers.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    Stage primaryStage;

    @FXML
    public static void main(String[] args) {
        DatabaseHandler.initialize();
        //sql
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/stylesheet.css")).toExternalForm());
        stage.setTitle("IMC");
        stage.setScene(scene);
        stage.show();
    }
}