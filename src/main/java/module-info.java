module com.example.eksamenchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.eksamenchat to javafx.fxml;
    exports com.example.eksamenchat;
}