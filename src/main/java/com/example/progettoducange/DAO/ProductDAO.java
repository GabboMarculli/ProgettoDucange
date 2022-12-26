package com.example.progettoducange.DAO;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;

public class ProductDAO {
    public static ArrayList<Document> getListOfProduct(Integer limit)
    {
        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getProductCollection();

        // we search for username
        MongoCursor<Document> cursor =  collection.find().iterator();

        //List<userDTO> resultDoc = FXCollections.observableArrayList();
        ArrayList<Document> results = collection.find().limit(limit).into(new ArrayList<>());

        System.out.println(results);

        return results;
    }
}
