package com.example.progettoducange.DbMaintaince;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

import org.bson.conversions.Bson;

public class MongoDbDriver {
    private static MongoDbDriver driver = null;
    private final MongoClient mongoclient;
    private static MongoDatabase database;

    private MongoDbDriver()
    {
        //---Connect to the MongoDB---
        ConnectionString uri = new ConnectionString("mongodb://localhost:27017");
        mongoclient = MongoClients.create(uri);
        database = mongoclient.getDatabase("Progetto");
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

    public MongoCollection<Document> getRecipeCollection()
    {
        return database.getCollection("Recipe");
    }

    public MongoCollection<Document> getProductCollection()
    {
        return database.getCollection("Product");
    }
}

