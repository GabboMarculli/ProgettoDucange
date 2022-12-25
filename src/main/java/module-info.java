module com.example.progettoducange {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.neo4j.driver;
    requires org.slf4j;
    requires java.logging;
    requires json;
    requires json.simple;
    requires com.google.gson;



    opens com.example.progettoducange.model to javafx.base;
    exports com.example.progettoducange;
    exports com.example.progettoducange.Controller;
    opens com.example.progettoducange.Controller to javafx.fxml;
    opens com.example.progettoducange.DTO to javafx.base;

}