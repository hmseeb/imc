package com.example.imc;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class Helpers {

    public void changeScene(ActionEvent event, String fxml) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        // Create a new scene with the root node
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/stylesheet.css")).toExternalForm());
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), scene.getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        // Set the new scene with a smooth transition
        stage.setScene(scene);
        stage.show();
        }

        // Apply a fade transition to the scene

    public void changeView(Pane viewPane, String fxmlFileName) throws Exception {
        // Load the new view
        Pane newView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
        // Clear the current view
        viewPane.getChildren().clear();

        // Add the new view with a fade animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), newView);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setOnFinished(event -> {
            // Animation finished, add the new view to the view pane
            viewPane.getChildren().add(newView);
        });
        fadeTransition.play();
    }
}
