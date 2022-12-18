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

    opens com.example.progettoducange.model to javafx.base;
    exports com.example.progettoducange;
    exports com.example.progettoducange.Controller;
    opens com.example.progettoducange.Controller to javafx.fxml;
}