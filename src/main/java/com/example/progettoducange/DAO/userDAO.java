package com.example.progettoducange.DAO;

import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.model.User;
import com.mongodb.client.MongoCollection;
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

    // Return true if the password of the username "username" is equal to the parameter "password". Return false otherwise
    public static boolean checkPassword(String username, String password)
    {
        Bson p1 = include("password");
        Bson p2 = excludeId();

        // retrieve the user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        Document resultDoc = collection.find(eq("username", username)).projection(p1).projection(p2).first();

        if(resultDoc!= null) {
            String[] result = resultDoc.toJson().split(",");
            String pass = result[1].split(":")[1];
            pass = Utils.CleanString(pass);

            return (password.equals(pass));
        } else
            return false; // in this case, user "username" doesn't exist
    }

    // Add new user
    // In LoginPage, user write your informations and than click submit. After that click, check if inserted data are valid
    // and than create new object User, with inserted data. After, call "signup" function to insert in the Db the new user
    public static boolean signup(User user)
    {
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        Document doc = new Document("username", user.getUsername()).append("password", user.getPassword()).append("firstName", user.getFirstName()).
                            append("lastName", user.getLastName()).append("profilePic", user.getProfilePicUrl()).append("email", user.getEmail());

        collection.insertOne(doc);
        return true;
    }
}

