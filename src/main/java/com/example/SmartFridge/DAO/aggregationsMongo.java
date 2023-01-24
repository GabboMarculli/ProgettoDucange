package com.example.SmartFridge.DAO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
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
        query.add(group("$RecipeName", avg("reviews.Rate", "$avg")));
        query.add(Sorts.descending("count"));
        query.add(project(fields(excludeId(), include("RecipeID"), include("avg"), include("$id.RecipeName"))));
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
        query.add(Sorts.descending("count"));
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

    public static List<Document> top10Ingredients(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        List<Bson> query = new ArrayList<>();

        query.add(unwind("$IngredientList"));
        query.add(group("$IngredientList", sum("count",1)));
        query.add(Sorts.descending("count"));
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

        query.add(unwind("$fridge"));
        query.add(group("$fridge.name"  , sum("$fridge.quantity",1)));
        query.add(group("$id.country"));
        query.add(Sorts.descending("sum"));
        query.add(project(fields(excludeId(), include("RecipeID"), include("ingredient"), include("$id.country"), include("sum"))));

        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }

        return results;
    }

    // ritorna username e numero di prodotti scaduti nel suo frigo
    public List<Document> expireProductByUser()
    {
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        List<Bson> query = new ArrayList<>();

        query.add(unwind("$fridge"));
        query.add(group("$fridge.name"  , sum("$fridge.quantity",1)));

        // date di scadenza <= timestamp corrente
        query.add(Aggregates.match(and(Filters.lte("$fridge.expiringDate", System.currentTimeMillis()))));

        /*
        // se expiringDate = 20/1/2023, per esempio, controlla se 20/1/2023 >= oggi + 1 giorno
        // quindi se 20/1/2023 >= 19/1/2023 + 1 cioè 20/1 >= 20/1. La risposta è si, quindi il prodotto sta per scadere
        // e infatti se oggi è il 19 e scade il 20, significa che sta per scadere

        query.add(Aggregates.match(and(Filters.gte("$fridge.expiringDate", System.currentTimeMillis() + 86400000))));
         */
        query.add(group("$id"));
        query.add(Sorts.descending("sum"));
        query.add(project(fields(excludeId(), include("$username"), include("sum"))));

        List<Document> results = null;
        try{
            results = collection.aggregate(query).into(new ArrayList<>());
        } catch (MongoException ex){
            System.out.println(ex);
        }

        return results;
    }


}
