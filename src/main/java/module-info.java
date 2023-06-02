module com.example.imc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.imc to javafx.fxml;
    exports com.example.imc;
    exports com.example.imc.Controllers;
    opens com.example.imc.Controllers to javafx.fxml;
    exports com.example.imc.Handlers;
    opens com.example.imc.Handlers to javafx.fxml;
}