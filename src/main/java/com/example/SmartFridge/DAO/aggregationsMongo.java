package com.example.SmartFridge.DAO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


public class aggregationsMongo {
    public void top10votedrecipe(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

    }
}
