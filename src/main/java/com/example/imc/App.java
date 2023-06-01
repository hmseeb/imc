package com.example.imc;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;


public class App extends Application {
    Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        //TODO: Add a login view
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/styles.css")).toExternalForm());
        stage.setTitle("IMC");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void main(String[] args) {
               try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con= DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/imc","root","2028YLV2");
        Statement stmt=con.createStatement();
        stmt.executeUpdate("create database if not exists imc;");
        ResultSet rs=stmt.executeQuery("show databases;");
        while(rs.next()) {
        System.out.println(rs.getString(1));
        }
        con.close();
    }

    catch(Exception ignored)
    {

    }
        launch();
    }
}