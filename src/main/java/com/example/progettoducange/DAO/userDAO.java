package com.example.progettoducange.DAO;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.model.Recipe;
import com.example.progettoducange.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import com.mongodb.internal.connection.tlschannel.util.Util;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;

public class userDAO {
    // Return a user given his username. If user doesn't exists, return null
    public static String findUser(String username)
    {
        Bson p1 = include("username");
        Bson p2 = excludeId();

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        // we search for username
        Document resultDoc = collection.find(eq("username", username)).projection(p1).projection(p2).first();

        if(resultDoc!= null) {
            String[] result = resultDoc.toJson().split(",");
            String user = result[0].split(":")[1]; // get username
            user = Utils.CleanString(user);

            return user;
        }

        return null;
    }

    // ##############################################################################################################
    // con alcuni username funziona, mentre con altri dà che la resultDoc è null e non funziona
    // ##############################################################################################################

    // Return true if the password of the username "username" is equal to the parameter "password". Return false otherwise
    public static boolean checkPassword(String username, String password)
    {
        Bson projectionFields = Projections.fields(
                Projections.include("password"),
                Projections.excludeId());

        // retrieve the user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        Document resultDoc = collection.find(eq("username", username)).projection(projectionFields).first();

        if(resultDoc!= null) {
            String[] result = resultDoc.toJson().split(",");
            String pass = result[1].split(":")[1];
            pass = Utils.CleanString(pass);

            System.out.println(pass);
            System.out.println(password);
            System.out.println(password.equals(pass));

            return (password.equals(pass));
        } else
            return false; // in this case, user "username" doesn't exist
    }

    // Add new user
    // In LoginPage, user write your informations and than click submit. After that click, check if inserted data are valid
    // and than create new object User, with inserted data. After, call "signup" function to insert in the Db the new user
    public static boolean signup(User user)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

            Document doc = new Document("username", user.getUsername()).append("password", user.getPassword()).append("firstName", user.getFirstName()).
                    append("lastName", user.getLastName()).append("profilePic", user.getProfilePicUrl()).append("email", user.getEmail());

            collection.insertOne(doc);
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    public boolean deleteUser(User user)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            collection.deleteOne(eq("username", user.getUsername()));
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    // #################################################################################
    // Credo che si faccia così
    // #################################################################################
    public boolean changePassword(User user, String newPassword)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            Document doc = new Document().append("password", newPassword);

            Bson query = new Document("$set", doc);
            collection.updateOne(new Document("password", user.getPassword()), query);

            user.setPassword(newPassword);
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }
}

