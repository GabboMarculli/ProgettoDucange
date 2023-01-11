package com.example.SmartFridge.DAO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static java.util.Collections.sort;


public class aggregationsMongo {
    public List<Document> top10votedrecipe(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        List<Bson> query = new ArrayList<>();

        query.add(unwind("$reviews"));
        query.add(group("$RecipeName", averageRate("reviews.Rate", "$avg")));
        query.add(sort("count"));
        query.add(limit(10));

        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }
        return results;
    }

    public List<Document> userMostCommented(){
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        List<Bson> query = new ArrayList<>();

        query.add(unwind("$reviews"));
        query.add(group("Reviews.profileID", sum("count",1)));
        query.add(sort("count"));
        query.add(project(fields(excludeId(), include("id"), include("count"), include("$id.profile"))));
        query.add(limit(10));

        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }

        return results;
    }

    public List<Document> top10Ingredients(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        List<Bson> query = new ArrayList<>();

        query.add(unwind("$IngredientList"));
        query.add(group("$IngredientList", sum("count",1)));
        query.add(sort("count"));
        query.add(project(fields(excludeId(), include("RecipeID"), include("count"), include("$id.ingredient"))));
        query.add(limit(10));

        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }

        return results;
    }

    public List<Document> ingredientsByCountry()
    {
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        List<Bson> query = new ArrayList<>();





        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }

        return results;
    }
}
