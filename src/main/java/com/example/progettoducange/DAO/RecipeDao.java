package com.example.progettoducange.DAO;

import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.ReviewsDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.model.Recipe;
import com.example.progettoducange.model.User;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import com.mongodb.client.model.Projections;
import javafx.scene.chart.PieChart;
import org.bson.Document;
import org.bson.conversions.Bson;

/////////
/*
* PROBLEMA: NELLA GETRECIPES L'ARRAY DI INGREDIENTI DEL FRIGO VIENE FORMATTATO MALE: RIVEDERE
* */
/////////



import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import com.google.gson.*;
import com.google.gson.*;
import com.google.gson.GsonBuilder;
import org.json.*;
import org.json.simple.parser.JSONParser;
public class RecipeDao {

    public boolean addRecipe(Recipe recipe) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

            Document doc = new Document("title", recipe.getTitle()).append("author", recipe.getAuthor()).
                    append("ingredients", recipe.getIngredients()).append("review", recipe.getReviews()).
                    append("directions", recipe.getDirections()).append("like", 0);

            collection.insertOne(doc);
            return true;
        } catch (Exception error) {
            System.out.println(error);
            return false;
        }
    }

    public boolean deleteRecipe(Recipe recipe)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            collection.deleteOne(eq("id", recipe.getId()));
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    // ################################################################################
    // non sono sicuro funzioni, è per aumentare di uno i like.
    // Devo fare anche la "removeLike"  ...?
    // ################################################################################
    public boolean addLike(Recipe recipe)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            Document doc = new Document().append("like", recipe.getLike());

            Bson query = new Document("$inc", doc);
            collection.updateOne(new Document("id", recipe.getId()), query);

            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }



    public static ArrayList<RecipeDTO> getRecipe(int limit) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        // we search for username
        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().limit(limit).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                JSONObject obj = new JSONObject(text);
                recipes_to_return.add(
                        new RecipeDTO(
                                obj.getString("RecipeName"),
                                Integer.parseInt(obj.getString("RecipeID")),
                                Integer.parseInt(obj.getString("ReviewCount")),
                                obj.getString("RecipePhoto"),
                                obj.getString("Author"),
                                obj.getString("PrepareTime"),
                                obj.getString("CookTime"),
                                obj.getString("TotalTime"),
                                obj.getString("Ingredients"),
                                obj.getString("Directions"),
                                obj.getString("IngredientsList").split(","),
                                return_array_reviews("{ reviews: " + obj.getString("reviews") + "}")
                        )
                );
            }


            return recipes_to_return;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

        private static ReviewsDTO[] return_array_reviews(String reviews) throws JSONException {
        JSONObject obj = new JSONObject(reviews);
        JSONArray arr = obj.getJSONArray("reviews");
        ReviewsDTO[] array_of_reviews = new ReviewsDTO[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            String user = arr.getJSONObject(i).getString("profileID");
            int rating = arr.getJSONObject(i).getInt("Rate");
            String Description = arr.getJSONObject(i).getString("Comment");
            array_of_reviews[i] = new ReviewsDTO(user, rating, Description);
        }
        return array_of_reviews;
    }
}
