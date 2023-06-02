package com.example.imc.Controllers;

import com.example.imc.Handlers.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportController {
    @FXML
    VBox productsContainer;

    @FXML
    public void initialize() throws SQLException {
        Statement stmt = DatabaseHandler.getStatement();
        // Use stmt to execute SQL queries
        ResultSet rs = stmt.executeQuery("show databases;");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        // Process the result set
        viewAll();

    }

    @FXML
    void viewAll() {
        addSupplier();
    }

    public void addSupplier() {
        HBox supplierPane = new HBox();
        supplierPane.getStyleClass().add("supplier-pane");
        supplierPane.setAlignment(Pos.BASELINE_LEFT);
        Label nameLabel = createLabel("name");
        Label productLabel = createLabel("product");
        Label contactNumberLabel = createLabel("contact");
        Label priceLabel = createLabel("price");
        Label typeLabel = createLabel("type");

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

        productsContainer.getChildren().addAll(
                supplierPane,
                createSeparator()
        );
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("child-item");
        label.setMinWidth(120); // Set a desired minimum width for the labels
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
