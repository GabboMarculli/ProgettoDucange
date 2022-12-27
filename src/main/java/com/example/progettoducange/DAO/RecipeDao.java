package com.example.progettoducange.DAO;

import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.ReviewDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.DbMaintaince.Neo4jDriver;
import com.example.progettoducange.model.Recipe;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;
import static org.neo4j.driver.Values.parameters;

import org.json.*;
import org.neo4j.driver.Session;

public class RecipeDao {

    public static boolean addRecipe(RecipeDTO recipe) {
        //save recipe into mongoDB
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

            Document doc =
                    new Document("RecipeName", recipe.getName())
                            .append("RecipeID", 3)
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
        } catch (Exception error) {
            System.out.println(error);
            return false;
        }

        //save recipe in Neo4J
        try (Session session = Neo4jDriver.getDriver().session()) {
                String name = recipe.getName();
                int id_receipe = recipe.getId();
                int id_user = Application.authenticatedUser.getId();

                session.writeTransaction(tx -> {
                        tx.run("MERGE (a:Receipe {name: $name, id: $id}) ",
                            parameters("name", name, "id", id_receipe)).consume();
                    //create a relathionship between the user and the receipe

                    tx.run( "MATCH (a:User) WHERE a.id = $id " +
                                    "MATCH (b:Receipe) WHERE b.id = $id1 " +
                                    "CREATE (a)-[:SHARE]->(b)",
                            parameters("id", id_user, "id1",id_receipe)).consume();
                    return 1;
                });
            }
        catch (Exception error) {
            System.out.println(error);
            return false;
        }
        return true;
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
    // Devo fare anche la "removeLike"  ...? -> no
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

    private static ReviewDTO[] return_array_reviews(String reviews) throws JSONException {
        JSONObject obj = new JSONObject(reviews);
        JSONArray arr = obj.getJSONArray("reviews");
        ReviewDTO[] array_of_reviews = new ReviewDTO[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            String user = arr.getJSONObject(i).getString("profileID");
            int rating = arr.getJSONObject(i).getInt("Rate");
            String Description = arr.getJSONObject(i).getString("Comment");
            array_of_reviews[i] = new ReviewDTO(user, rating, Description);
        }
        return array_of_reviews;
    }
    public static void addReview(ReviewDTO review, int id_recipe){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            BasicDBObject query = new BasicDBObject();
            query.put( "RecipeID", id_recipe);

            BasicDBObject review_mongo = new BasicDBObject();
            review_mongo.put("profileId", Application.authenticatedUser.getUsername());
            review_mongo.put("Rate", review.getRate());
            review_mongo.put("Comment", review.getComment());

            BasicDBObject update = new BasicDBObject();
            update.put("$push", new BasicDBObject("reviews",review_mongo));

            collection.updateOne(query, update);

        } catch (Exception error) {
            System.out.println( error );
        }
    }
    public static void removerecipe(RecipeDTO recipe) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            collection.deleteOne(eq("RecipeID", recipe.getId()));
        } catch (Exception error) {
            System.out.println( error );
        }
    }
}

