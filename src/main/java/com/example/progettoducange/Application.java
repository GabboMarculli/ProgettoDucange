package com.example.progettoducange;

import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// ###################################################################################################################
// ###################################################################################################################
// ###################################################################################################################
// ###################################################################################################################
// BISOGNA RENDERE MODULARE QUESTO FILE: CREA UNA CLASSE GRAPHIC BUILDER DOVE METTERE DENTRO LE FUNZIONI RELATIVE AI CAMBI SCENA ECC
// ###################################################################################################################
// ###################################################################################################################
// ###################################################################################################################
// ###################################################################################################################

public class Application extends javafx.application.Application {
    MongoDbDriver mongodb;
    private Stage primaryStage;
    private static Scene primaryScene;
    public static RegisteredUser authenticatedUser;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000 , 700);
        stage.setTitle("Login or Sign up");
        stage.setScene(scene);
        stage.show();

        setPrimaryStage(stage);
        setPrimaryScene(scene);

        mongodb = MongoDbDriver.getInstance();
        }

    public static void main(String[] args) {
        try {
            launch();
        }catch(Exception e){
            System.exit(1 );
        }
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