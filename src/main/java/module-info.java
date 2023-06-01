module com.example.imc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.imc to javafx.fxml;
    exports com.example.imc;
}