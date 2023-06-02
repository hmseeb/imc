package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.sql.SQLException;
import java.sql.Statement;

public class SupplierController {
    @FXML
    TextField nameController;
    @FXML
    TextField productController;
    @FXML
    TextField contactController;
    @FXML
    TextField priceController;
    @FXML
    TextField typeController;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane popupPane;
    @FXML
    private VBox suppliersContainer;

    // For the add product button in the inventory view
    @FXML
    public void initialize() throws SQLException {
        Statement stmt = DatabaseHandler.getStatement();
        // Use stmt to execute SQL queries
    }

    @FXML
    void kAddSupplier() {
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
        String name = nameController.getText(), product = productController.getText(), contact = contactController.getText(), price = priceController.getText(), type = typeController.getText();
        addSupplier(name, product, contact, price, type);
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

    public void addSupplier(String name, String product, String contact, String price, String type) {
        HBox supplierPane = new HBox();
        supplierPane.getStyleClass().add("supplier-pane");
        supplierPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel(name);
        Label productLabel = createLabel(product);
        Label contactNumberLabel = createLabel(contact);
        Label priceLabel = createLabel(price);
        Label typeLabel = createLabel(type);

        supplierPane.getChildren().addAll(
                nameLabel,
                createSpacer(),
                productLabel,
                createSpacer(),
                contactNumberLabel,
                createSpacer(),
                priceLabel,
                createSpacer(),
                typeLabel
        );

        suppliersContainer.getChildren().addAll(
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