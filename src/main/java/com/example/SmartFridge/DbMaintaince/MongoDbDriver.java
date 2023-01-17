package com.example.SmartFridge.DbMaintaince;


import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import org.bson.conversions.Bson;

public class MongoDbDriver {



    private static MongoDbDriver driver = null;
    private static MongoClient mongoclient;
    private static MongoDatabase database;

    private MongoDbDriver()
    {


        //---Connect to the MongoDB---
        ConnectionString uri = new ConnectionString("mongodb://localhost:27017/?serverSelectionTimeoutMS=1000&connectTimeoutMS=1000");
        /*

        mongoclient = MongoClients.create(uri);
        try {

            database = mongoclient.getDatabase("Progetto");
            System.out.println("Database prelevato");

         */

        try{
             mongoclient = MongoClients.create(uri);
            database = mongoclient.getDatabase("Progetto");
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Connected successfully to server.");
        } catch (Exception me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            Error panic = new Error(me);
            /*
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Button e = new Button("EXIT");
            Text t = new Text(("CRITICAL ERROR: " + me.getMessage()));
            t.setWrappingWidth(200);
            VBox vbox = new VBox(t,e);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(15));
            dialogStage.setWidth(300);
            dialogStage.setAlwaysOnTop(true);
            dialogStage.setScene(new Scene(vbox));
            dialogStage.show();
            e.setOnAction(w->{
                        System.exit(1);
                    });
            dialogStage.setOnCloseRequest(w->{
                        System.exit(1);
                    });
        */
        }
    }



    // singleton pattern
    public static MongoDbDriver getInstance() {
        if(driver == null){
            driver = new MongoDbDriver();
        }
        return driver;
    }

    public void close() {
        if(mongoclient!= null){
            mongoclient.close();
        }

        System.out.println("Mongo Connection closed");
    }

    public static MongoCollection<Document> getUserCollection()
    {
           return database.getCollection("User");
    }

    public static MongoCollection<Document> getRecipeCollection()
    {
        return database.getCollection("Recipe");
    }

    public static MongoCollection<Document> getProductCollection()
    {
        return database.getCollection("Product");
    }
}

