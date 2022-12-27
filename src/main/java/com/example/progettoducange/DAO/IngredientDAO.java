package com.example.progettoducange.DAO;

import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class IngredientDAO {
    public static ArrayList<IngredientDTO> getListOfIngredient(int limit){
        // retrieve ingredient collection
        MongoCollection<Document> collection = MongoDbDriver.getProductCollection();

        ArrayList<IngredientDTO> ingredients_to_return = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().limit(limit).projection(Projections.excludeId()).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                JSONObject obj = new JSONObject(text);
                ingredients_to_return.add(
                        new IngredientDTO(
                                obj.getString("food"),
                                obj.getString("measure"),
                                obj.getString("grams"),
                                obj.getString("calories"),
                                obj.getString("protein"),
                                obj.getString("fat"),
                                obj.getString("fiber"),
                                obj.getString("carbs"),
                                obj.getString("category")
                        )
                );
            }
            return ingredients_to_return;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addToFridge(IngredientDTO ingredientDTO) {
        //add the ingredient to user fridge
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        BasicDBObject query = new BasicDBObject();
        query.put( "id", Application.authenticatedUser.getId());

        BasicDBObject ingredient = new BasicDBObject();
        ingredient.put("name", ingredientDTO.getFood());
        ingredient.put("quantity", 2);
        ingredient.put("expiringDate", "21/9/2022");

        BasicDBObject update = new BasicDBObject();
        update.put("$push", new BasicDBObject("fridge",ingredient));

        collection.updateOne(query, update);

        //action to be implemented
        System.out.println("Prodotto aggiunto");
    }

    public static void removeIngredient(IngredientDTO ingredient) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getProductCollection();
            collection.deleteOne(eq("food", ingredient.getFood()));
        } catch (Exception error) {
            System.out.println( error );
        }
    }

    public static void addIngredient(IngredientDTO ingredient) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getProductCollection();
            Document doc = new Document()
                    .append("food",ingredient.getFood())
                    .append("measure",ingredient.getMeasure())
                    .append("grams",ingredient.getGrams())
                    .append("calories",ingredient.getCalories())
                    .append("protein",ingredient.getProtein())
                    .append("fat",ingredient.getFat())
                    .append("fiber",ingredient.getFiber())
                    .append("carbs",ingredient.getCarbs())
                    .append("category", ingredient.getCategory());
            collection.insertOne(doc);
        } catch (Exception error) {
            System.out.println( error );
        }
    }



}
