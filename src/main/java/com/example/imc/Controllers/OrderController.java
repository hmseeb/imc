package com.example.imc.Controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class OrderController {

    @FXML
    TextField nameController;
    @FXML
    TextField idController;
    @FXML
    TextField quantityController;
    @FXML
    TextField priceController;
    @FXML
    TextField categoryController;
    @FXML
    TextField dateController;
    @FXML
    TextField valueController;
    @FXML
    TextField unitController;
    @FXML
    TextField dodController;


    @FXML
    private Pane mainPane;

    @FXML
    private Pane popupPane;
    @FXML
    private VBox ordersContainer;

    // For the add product button in the inventory view
    @FXML
    void kAddOrder() {
        // Apply a BoxBlur effect to the mainPane to make it blur
        BoxBlur blur = new BoxBlur(5, 5, 3);
        mainPane.setEffect(blur);

        // Make the popup pane visible and animate its fade-in
        popupPane.setOpacity(0);
        popupPane.setVisible(true);
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }

    @FXML
    private void onConfirmClicked() {
        // TODO Add additional logic here
        String name = nameController.getText(), id = idController.getText(), quantity = quantityController.getText(), price = priceController.getText(), category = categoryController.getText(), date = dateController.getText(), value = valueController.getText(), unit = unitController.getText(), dod = dodController.getText();
        addOrder(name, price, quantity, id, dod, "Testing");
        // Animate the popup pane's fade-out and then hide it
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        fadeOutTransition.play();

        // Remove the BoxBlur effect from the mainPane
        mainPane.setEffect(null);
    }

    @FXML
    private void onDiscardClicked() {
        // TODO Add additional logic here
        // Animate the popup pane's fade-out and then hide it
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(300), popupPane);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> popupPane.setVisible(false));
        fadeOutTransition.play();

        // Remove the BoxBlur effect from the mainPane
        mainPane.setEffect(null);
    }

    public void addOrder(String name, String price, String quantity, String id, String dod, String status) {
        HBox supplierPane = new HBox();
        supplierPane.getStyleClass().add("supplier-pane");
        supplierPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel(name);
        Label priceLabel = createLabel(price);
        Label quantityLabel = createLabel(quantity);
        Label idLabel = createLabel(id);
        Label dodLabel = createLabel(dod);
        Label statusLabel = createLabel(status);

        supplierPane.getChildren().addAll(
                nameLabel,
                createSpacer(),
                priceLabel,
                createSpacer(),
                quantityLabel,
                createSpacer(),
                idLabel,
                createSpacer(),
                dodLabel,
                createSpacer(),
                statusLabel
        );
        ordersContainer.getChildren().addAll(
                supplierPane,
                createSeparator()
        );
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("child-item");
        label.setMinWidth(100); // Set a desired minimum width for the labels
        return label;
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        separator.setOpacity(0.5);
        separator.setMaxWidth(Double.MAX_VALUE);
        return separator;
    }
}
