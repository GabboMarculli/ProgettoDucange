package com.example.progettoducange;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    MongoDbDriver mongodb;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000 , 700);
        stage.setTitle("Login or Sign up");
        stage.setScene(scene);
        stage.show();

        mongodb = MongoDbDriver.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}