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





import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import com.google.gson.*;
import com.google.gson.*;
import com.google.gson.GsonBuilder;
import org.json.*;
import org.json.simple.parser.JSONParser;
public class RecipeDao {

    public static boolean addRecipe(RecipeDTO recipe) {
        //save recipe into mongoDB
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

            Document doc =
                    new Document("RecipeName", recipe.getName())
                            .append("RecipeID", 0)
                            .append("ReviewCount", 0)
                            .append("RecipePhoto", recipe.getPhoto())
                            .append("Author", recipe.getAuthor())
                            .append("PrepareTime", recipe.getPreparationTime())
                            .append("CookTime", recipe.getCooktime())
                            .append("TotalTime", recipe.getTotalTime())
                            .append("Ingredients", recipe.getIngrients())
                            .append("Direction", recipe.getDirection())
                            .append("IngredientList", recipe.getIngredientsList())
                            .append("reviews", null);
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

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().limit(limit).projection(Projections.excludeId()).iterator()) {
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
                                getIngedientList(obj.getString("IngredientsList")),
                                return_array_reviews("{ reviews: " + obj.getString("reviews") + "}")
                        )
                );
            }
            return recipes_to_return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getIngedientList(String d){
        return rubahFormat(d).split(",");
    }

    public static String rubahFormat(String d){
        return d.replaceAll("[\\[\\]\\\"]","");
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
