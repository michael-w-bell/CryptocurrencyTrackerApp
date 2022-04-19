module com.michaelbell.cryptocurrencytrackerapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires json.simple;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.sql;
    requires mysql.connector.java;

    opens com.michaelbell.cryptocurrencytrackerapp to javafx.fxml;
    exports com.michaelbell.cryptocurrencytrackerapp;
    exports com.michaelbell.cryptocurrencytrackerapp.controllers;
    exports com.michaelbell.cryptocurrencytrackerapp.models;
    opens com.michaelbell.cryptocurrencytrackerapp.controllers to javafx.fxml;
}