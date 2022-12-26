package com.example.progettoducange.DAO;

import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
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

}
