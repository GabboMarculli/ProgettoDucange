package com.example.progettoducange;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    MongoDbDriver mongodb;
    private Stage primaryStage;
    private static Scene primaryScene;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ProfilePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000 , 700);
        stage.setTitle("Login or Sign up");
        stage.setScene(scene);
        stage.show();
        setPrimaryStage(stage);
        setPrimaryScene(scene);

        mongodb = MongoDbDriver.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxml + ".fxml"));
        primaryScene.setRoot(fxmlLoader.load());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setPrimaryStage(Stage stage)
    {
        this.primaryStage = stage;
    }

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void setPrimaryScene(Scene primaryScene) {
        Application.primaryScene = primaryScene;
    }
}