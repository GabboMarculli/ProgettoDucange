package com.example.progettoducange.DAO;

import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;
import static org.neo4j.driver.Values.parameters;

import org.json.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class RecipeDao {

    public static boolean addRecipe(RecipeDTO recipe) {
        //save recipe into mongoDB
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

            Document doc =
                    new Document("RecipeName", recipe.getName())
                            .append("RecipeID", recipe.getId())
                            .append("ReviewCount", 0)
                            .append("RecipePhoto", recipe.getPhoto())
                            .append("Author", recipe.getAuthor())
                            .append("PrepareTime", recipe.getPreparationTime())
                            .append("CookTime", recipe.getCooktime())
                            .append("TotalTime", recipe.getTotalTime())
                            .append("Ingredients", recipe.getIngrients())
                            .append("Direction", recipe.getDirection())
                            .append("IngredientList", Arrays.asList(recipe.getIngredientsList()))
                            ;
            collection.insertOne(doc);
        } catch (Exception error) {
            System.err.println(error);
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

    //function to return the index used for creating a new recipe
    public static int get_id_recipe() {
        //i will assign the id to the user
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        // we search for the last id
        Document resultDoc = collection.find().sort(descending("RecipeID")).first();
        int index = resultDoc.getInteger("RecipeID") + 1;
        return index;
    }

    public static List<RecipeDTO> recipe_of_followed_user() {
        List<RecipeDTO> RecipeList = null;
        try (Session session = Neo4jDriver.getDriver().session()) {
            RecipeList = session.readTransaction((TransactionWork<List<RecipeDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id})-[:FOLLOW]->(u1: User)-[:SHARE]->(r: Recipe)"+
                                "RETURN r.id AS id, r.name AS name, " +
                                "r.review_count as ReviewCount, r.totalTime as totalTime ",
                        parameters("id", Application.authenticatedUser.getId()
                                ));
                List<RecipeDTO> Recipe_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    Recipe_to_send.add(new RecipeDTO(
                            r.get("name").asString(),
                            r.get("id").asInt(),
                            r.get("ReviewCount").asInt(),
                            r.get("totalTime").asString()
                    ));
                }
                return Recipe_to_send;
            });
            return RecipeList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    // function will find the recipe that has the same ingredient present in the list_of_ingredient;
    public static ArrayList<RecipeDTO> get_suggested_recipe_by_ingredient(String[] list_of_product) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        Bson projectionFields = Projections.fields(
                Projections.include("RecipeName"),
                Projections.include("RecipeID"),
                Projections.include("ReviewCount"),
                Projections.include("TotalTime"),
                Projections.excludeId());

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("IngredientsList", new BasicDBObject("$all", list_of_product));

        MongoCursor<Document> cursor = collection.find(query).projection(projectionFields).limit(20).iterator();

        try {
            if(!cursor.hasNext()){
                query = new BasicDBObject("IngredientsList", new BasicDBObject("$in", list_of_product));
                cursor = collection.find(query).projection(projectionFields).limit(20).iterator();
            }
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                JSONObject obj = new JSONObject(text);
                recipes_to_return.add(
                        new RecipeDTO(
                                obj.getString("RecipeName"),
                                Integer.parseInt(obj.getString("RecipeID")),
                                Integer.parseInt(obj.getString("ReviewCount")),
                                obj.getString("TotalTime")
                        )
                );
            }
            cursor.close();
            return recipes_to_return;
        } catch (JSONException e) {
            cursor.close();
            throw new RuntimeException(e);
        }
    }

    public static List<RecipeDTO> getMyRecipe(int limit, int skipped_times) {
        List<RecipeDTO> RecipeList = null;
        int skipped_calculated = limit*skipped_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            RecipeList = session.readTransaction((TransactionWork<List<RecipeDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id})-[:SHARE]->(r:Recipe) " +
                                "RETURN r.id AS id, r.name AS name, " +
                                "r.review_count as ReviewCount, r.totalTime as totalTime" +
                                " SKIP $skip LIMIT $limit ",

                        parameters("id", Application.authenticatedUser.getId(),
                                "limit", limit,
                                "skip", skipped_calculated));
                List<RecipeDTO> Recipe_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    Recipe_to_send.add(new RecipeDTO(
                            r.get("name").asString(),
                            r.get("id").asInt(),
                            r.get("ReviewCount").asInt(),
                            r.get("totalTime").asString()
                    ));
                }
                return Recipe_to_send;
            });
            return RecipeList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
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

    //skipped_time is used for retriving limit recipe at a time belonging to interval [skipped_times*limit, (skipped_times+1)*limit]
    /*THIS WAS FOR RETRIVING RECIPE FROM MONGODB
    public static ArrayList<RecipeDTO> getRecipe(int limit, int skipped_times) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();
        Bson projectionFields = Projections.fields(
                Projections.include("RecipeName"),
                Projections.include("RecipeID"),
                Projections.include("ReviewCount"),
                Projections.include("TotalTime"),
                Projections.excludeId());

        try (MongoCursor<Document> cursor = collection.find().skip(skipped_times*limit).limit(limit).projection(projectionFields).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                JSONObject obj = new JSONObject(text);
                recipes_to_return.add(
                        new RecipeDTO(
                                obj.getString("RecipeName"),
                                Integer.parseInt(obj.getString("RecipeID")),
                                Integer.parseInt(obj.getString("ReviewCount")),
                                obj.getString("TotalTime")
                        )
                );
            }
            return recipes_to_return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }*/
    public static List<RecipeDTO> getRecipe(int limit, int skipped_times) {
        List<RecipeDTO> RecipeList = null;
        int skipped_calculated = limit*skipped_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            RecipeList = session.readTransaction((TransactionWork<List<RecipeDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id}), (r:Recipe) " +
                                "WHERE NOT (u)-[:SHARE]->(r) " +
                                "RETURN r.id AS id, r.name AS name, " +
                                "r.review_count as ReviewCount, r.totalTime as totalTime" +
                                " SKIP $skip LIMIT $limit ",

                        parameters("id", Application.authenticatedUser.getId(),
                                "limit", limit,
                                "skip", skipped_calculated));
                List<RecipeDTO> Recipe_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    Recipe_to_send.add(new RecipeDTO(
                            r.get("name").asString(),
                            r.get("id").asInt(),
                            r.get("ReviewCount").asInt(),
                            r.get("totalTime").asString()
                    ));
                }
                return Recipe_to_send;
            });
            return RecipeList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public static RecipeDTO getSingleRecipe(RecipeDTO recipe) {

        // retrieve information
        Bson projectionFields = Projections.fields(
                Projections.exclude("RecipeName"),
                Projections.exclude("RecipeID"),
                Projections.exclude("ReviewCount"),
                Projections.exclude("TotalTime"),
                excludeId());

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        // we search for username
        Document obj = collection.find(eq("RecipeID", recipe.getId())).projection(projectionFields).first();

        try{
            recipe.setPhoto(obj.getString("RecipePhoto"));
            recipe.setAuthor(obj.getString("Author"));
            recipe.setPreparationTime(obj.getString("PrepareTime"));
            recipe.setCooktime(obj.getString("CookTime"));
            recipe.setIngrients(obj.getString("Ingredients"));
            recipe.setDirection(obj.getString("Directions"));
            //this 3 following line to get the list of ingredient
            List<String> support = obj.getList("IngredientsList",String.class);
            String[] return_list_ingredient = support.toArray(new String[0]);
            recipe.setIngredientsList(return_list_ingredient);

            List<Object> l = obj.getList("reviews",Object.class);
            recipe.setReviews(return_array_reviews(l));
            return recipe;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<RecipeDTO> getSearchedRecipe(String recipeName) {
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();
        JSONObject obj;

        try (MongoCursor<Document> cursor = collection.find(regex("RecipeName", ".*" + Pattern.quote(recipeName) + ".*")).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson();
                obj = new JSONObject(text);
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
                                return_array_reviews_json("{ reviews: " + obj.getString("reviews") + "}")
                        )
                );
            }
            return recipes_to_return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReviewDTO[] return_array_reviews(List<Object> l) {
        ReviewDTO return_Array[] = new ReviewDTO[l.size()];
        for(int i=0; i<l.size();i++){
            Object support = l.get(i);
            return_Array[i] = new ReviewDTO(
                    ((Document) support).getString("profileID"),
                    ((Document) support).getInteger("Rate"),
                    ((Document) support).getString("Comment")
            );
        }
        return return_Array;
    }

    public static String[] getIngedientList(String d){
        return rubahFormat(d).split(",");
    }

    public static String rubahFormat(String d){
        return d.replaceAll("[\\[\\]\\\"]","");
    }

    private static ReviewDTO[] return_array_reviews_json(String reviews) throws JSONException {
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
            review_mongo.put("profileID", Application.authenticatedUser.getUsername());
            review_mongo.put("Rate", review.getRate());
            review_mongo.put("Comment", review.getComment());


            BasicDBObject update = new BasicDBObject().append("$push", new BasicDBObject().append("reviews",review_mongo));
            update = update.append("$inc", new BasicDBObject().append("ReviewCount", 1));

            collection.updateOne(query, update);

            //to manteining consistency, update the review count also on the graph
            incremente_review_count_on_the_graph(id_recipe);


        } catch (Exception error) {
            System.out.println( error );
        }
    }

    private static void incremente_review_count_on_the_graph(int id_recipe) {
        try (Session session = Neo4jDriver.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:Recipe { id: $id}) " +
                                "SET a.review_count = a.review_count+1",
                        parameters("id", id_recipe)).consume();
                return 1;
            });
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

