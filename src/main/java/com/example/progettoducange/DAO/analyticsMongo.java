package com.example.progettoducange.DAO;
import com.example.progettoducange.Application;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.IngredientInFridge;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


public class analyticsMongo {
    public void top10votedrecipe(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

    }
}
