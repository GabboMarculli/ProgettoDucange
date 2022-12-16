package com.example.progettoducange.DAO;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.Recipe;
import com.example.progettoducange.model.User;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class RecipeDao {
    public static boolean addRecipe(Recipe recipe) {
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
}
