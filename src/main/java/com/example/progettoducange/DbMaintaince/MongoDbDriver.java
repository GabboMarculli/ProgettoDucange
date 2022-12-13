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
    public static void main(String[] args)
    {
        visualizeData();
        //updateData(3,"name","Jessica");// al posto dei vari valori ci vanno le robe prese dall'interfaccia

    }
    static void visualizeData(){
        //---Connect to the MongoDB---
        ConnectionString uri = new ConnectionString("mongodb://localhost:27017");
        MongoClient mongoClient = MongoClients.create(uri);

        // Get DB
        MongoDatabase db = mongoClient.getDatabase("mongo_project");

        //acess to the collection user
        MongoCollection<Document> collection = db.getCollection("user");

        // Get list of existing Collections
        Bson p1 = include("username","password","country","registrationdate","name","surname");
        Bson p2 = excludeId();
        Document resultDoc = collection.find(eq("username", "Finochio")).projection(p1).projection(p2).first();
        System.out.println(resultDoc.toJson());
        String[] result = resultDoc.toJson().split(",");
        //username;
        System.out.println(result[0].split(":")[1]);
        //password;
        System.out.println(result[1].split(":")[1]);
        //country;
        System.out.println(result[2].split(":")[1]);
        //registrationdate;
        System.out.println(result[3].split(":")[1]);
        //name;
        System.out.println(result[4].split(":")[1]);
        //surname;
        System.out.println(result[5].split(":")[1]);
        //id;
        System.out.println(result[6].split(":")[1]);
        //fridge; //vedere meglio
        // System.out.println(result[7]);
    }
    static void updateData(int id, String key, String value){
        ConnectionString uri = new ConnectionString("mongodb://localhost:27017");
        MongoClient mongoClient = MongoClients.create(uri);

        // Get DB
        MongoDatabase db = mongoClient.getDatabase("mongo_project");

        //acess to the collection user
        MongoCollection<Document> collection = db.getCollection("user");

        collection.updateOne(
                Filters.eq("id", id),
                Updates.set(key, value)
        );
    }
}

