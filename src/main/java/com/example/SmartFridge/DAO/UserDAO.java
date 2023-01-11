package com.example.SmartFridge.DAO;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DTO.userDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.model.RegisteredUser;
import com.example.SmartFridge.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static org.neo4j.driver.Values.parameters;

public class UserDAO {
    // Return a user given his username. If user doesn't exists, return null
    public static String findUser(String username)
    {
        Bson projectionFields = Projections.fields(
                Projections.include("username"),
                excludeId());

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        // we search for username
        Document resultDoc = collection.find(eq("username", username)).projection(projectionFields).first();

        if(resultDoc!= null) {
            String user = resultDoc.getString("username");
            return user;
        }

        return null;
    }

    //FUNZIONE DA MODIFICARE
    public static String getID(String username)
    {
        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        // we search for username
        Document resultDoc = collection.find(eq("username", username)).first();

        if(resultDoc!= null) {
            String[] result = resultDoc.toJson().split(",");
            String id = result[0].split(":")[2]; // get username
            id = Utils.CleanString(id);

            return id;
        }

        return null;
    }
/*

    {
        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        ArrayList<Document> results = collection.find().limit(limit).skip(called_times*20).into(new ArrayList<>());

        //retrive list of unflowwed user from the graph db

        return results;
    }
    */

    /*
    MATCH (u:User {id:2}), (p:User)
WHERE NOT (u)-[:FOLLOW]->(p) AND p.id <> 2
RETURN p
SKIP 4
LIMIT 4
     */
/*
    public static List<userDTO> getListOfUser(Integer limit,Integer called_times) {
        List<userDTO> UserList = null;
        int skipped_calculated = limit*called_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            UserList = session.readTransaction((TransactionWork<List<userDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id}), (m:User) " +
                                "WHERE NOT (u)-[:FOLLOW]->(m) AND m.id <> $id " +
                                "RETURN m.id AS id, m.country AS country , m.name AS username" +
                                " SKIP $skip LIMIT $limit ",
                        parameters("id", Application.authenticatedUser.getId(),
                                "limit", limit,
                                "skip", skipped_calculated));
                List<userDTO> User_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User_to_send.add(new userDTO(
                            r.get("id").asInt(),
                            r.get("country").asString(),
                            r.get("username").asString()
                    ));

                }
                return User_to_send;
            });
            return UserList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
*/
    
    public static List<userDTO> getListOfFollowedUser(Integer limit,Integer called_times) {
        List<userDTO> UserList = null;
        int skipped_calculated = limit*called_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            UserList = session.readTransaction((TransactionWork<List<userDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (p: User)-[:FOLLOW]->(m:User) " +
                                "Where p.id = $id " +
                                " RETURN m.id AS id, m.country AS country , m.name AS username " +
                                "SKIP $skip LIMIT $limit ",
                        parameters(
                                "id", Application.authenticatedUser.getId(),
                                "limit", limit,
                                "skip", skipped_calculated
                        ));
                List<userDTO> User_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User_to_send.add(new userDTO(
                                    r.get("id").asInt(),
                                    r.get("country").asString(),
                                    r.get("username").asString()
                            ));

                }
                return User_to_send;
            });
            return UserList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    public static List<userDTO> getListOfSuggesteUser() {
        List<userDTO> UserList = null;
        try (Session session = Neo4jDriver.getDriver().session()) {
            UserList = session.readTransaction((TransactionWork<List<userDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (user:User)-[:FOLLOW]->(:User)-[:FOLLOW]->(suggestion:User)" +
                                " WHERE user.id = $id  AND NOT EXISTS ((user)<-[:FOLLOW]->(suggestion)) " +
                                " RETURN suggestion.id AS id, suggestion.country AS country , suggestion.name AS username " +
                                "LIMIT 40",
                        parameters(
                                "id", Application.authenticatedUser.getId()
                        ));
                List<userDTO> User_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User_to_send.add(new userDTO(
                            r.get("id").asInt(),
                            r.get("country").asString(),
                            r.get("username").asString()
                    ));

                }
                return User_to_send;
            });
            return UserList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public static List<userDTO> Search_for_followed_user(String name_searched) {
        List<userDTO> UserList = null;
        try (Session session = Neo4jDriver.getDriver().session()) {
            UserList = session.readTransaction((TransactionWork<List<userDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (p: User)-[:FOLLOW]->(m:User) " +
                                "Where p.id = $id " +
                                "AND m.name CONTAINS $name " +
                                "RETURN m.id AS id, m.country AS country , m.name AS username",
                        parameters("id", Application.authenticatedUser.getId(),
                                "name",name_searched));
                List<userDTO> User_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User_to_send.add(new userDTO(
                            r.get("id").asInt(),
                            r.get("country").asString(),
                            r.get("username").asString()
                    ));

                }
                return User_to_send;
            });
            return UserList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    public static List<userDTO> Search_for_Unfollowed_user(String name_searched,Integer limit,Integer called_times) {
        List<userDTO> UserList = null;
        int skipped_calculated = limit*called_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            UserList = session.readTransaction((TransactionWork<List<userDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id}), (m:User) " +
                                "WHERE NOT (u)-[:FOLLOW]->(m) AND m.id <> $id " +
                                "AND m.name CONTAINS $name " +
                                "RETURN m.id AS id, m.country AS country , m.name AS username"+
                                " SKIP $skip LIMIT $limit ",

                        parameters("id", Application.authenticatedUser.getId(),
                                "name",name_searched,
                                "limit", limit,
                                "skip", skipped_calculated));
                List<userDTO> User_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User_to_send.add(new userDTO(
                            r.get("id").asInt(),
                            r.get("country").asString(),
                            r.get("username").asString()
                    ));

                }
                return User_to_send;
            });
            return UserList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    public static String[] getUser(String username)
    {
        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        // we search for username
        Document resultDoc = collection.find(eq("username", username)).projection(excludeId()).first();

        String return_fields[]={
                String.valueOf(resultDoc.getInteger("id")),
                resultDoc.getString("username"),
                resultDoc.getString("country"),
                resultDoc.getString("name"),
                resultDoc.getString("surname"),
                resultDoc.getString("email")
        };
        return return_fields;
    }

    public static boolean checkPassword(String username, String password){

        // retrieve the user collection
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        List<DBObject> criteria = new ArrayList<DBObject>();
        criteria.add(new BasicDBObject("username", new BasicDBObject("$eq", username)));
        criteria.add(new BasicDBObject("password", new BasicDBObject("$eq", password)));
        Document resultDoc = collection.find(new BasicDBObject("$and", criteria)).first();

        if(resultDoc!= null) {
            return true; // the password associated with that user exists
        } else
            return false; // in this case, user "username" doesn't exist
    }

    // Add new user
    // In LoginPage, user write your informations and than click submit. After that click, check if inserted data are valid
    // and than create new object User, with inserted data. After, call "signup" function to insert in the Db the new user
    public static int signup(User user)
    {
        try {
            //i will assign the id to the user
            int new_index = add_user_to_mongoDB(user);
            user.setId(new_index);
            //now we will add the user to neo4j
            if(!add_user_to_neo4j(user)){
                return 0;
            };
            System.out.println("User correctly added");
            return new_index;
        } catch (MongoException error) {
            //an error occurs in mongoDB
            System.err.println( "error verified during insert a user in MongoDB" );
            return 0;
        }
    }

    private static int add_user_to_mongoDB(User user) throws MongoException{
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        String text = user.getRegistrationDate().getDayOfMonth() + "/" +
                user.getRegistrationDate().getMonthValue() + "/" +
                user.getRegistrationDate().getYear();

        // we search for the last id
        Document resultDoc = collection.find().sort(descending("id")).first();
        int new_index = resultDoc.getInteger("id") + 1;
        //insert the user in the collection
        Document doc = new Document("username",user.getUsername())
                .append("password", user.getPassword())
                .append("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("email", user.getEmail())
                .append("country",user.getCountry())
                .append("id", new_index)
                .append("registrationdate", text);
        collection.insertOne(doc);
        System.out.println("User correctly added in Mongodb");
        return new_index;
    }

    private static boolean add_user_to_neo4j(User user) {

        try (Session session = Neo4jDriver.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("MERGE (a:User {name: $name, id: $id, country: $country})",
                        parameters("name", user.getUsername(), "id", user.getId(), "country",user.getCountry())).consume();
                return 1;
            });
        }catch (Exception e){
            System.err.println( "error verified during insert a user in Neo4j" );
            //here i have to remove the user also from MongoDB
            delete_User_from_MongoDB(user);
            return false;
        }
        System.out.println("User correctly added in Neo4j");
        return true;
    }


    public static boolean deleteUser(User user)
    {
        try {
            delete_User_from_MongoDB(user);
            //delete the user from neo4j
            if(!delete_user_from_neo4j(user)){
                return false;
            };
            System.out.println( "user correctly deleted" );
            return true;
        } catch (Exception error) {
            System.err.println( "an error occurs when deleting a user from MongoDB" );
            return false;
        }
    }

    public static boolean delete_User_from_MongoDB(User user)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            collection.deleteOne(eq("id", user.getId()));
            System.out.println( "user correctly deleted from MongoDB" );
            return true;
        } catch (Exception error) {
            System.err.println( "an error occurs when deleting a user from MongoDB" );
            return false;
        }

    }


    private static boolean delete_user_from_neo4j(User user) {

        try (Session session = Neo4jDriver.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:User {id: $id}) " +
                                "DETACH DELETE a",
                        parameters("id", user.getId())).consume();
                return 1;
            });
            System.out.println( "user correctly deleted from Neo4j" );
        }catch (Exception e){
            System.err.println( "error verified during the deletion of a user in Neo4j" );
            //here i have to insert the user also from MongoDB
            add_user_to_mongoDB(user);
            return false;
        }
        return true;

    }

    public static boolean changePassword(RegisteredUser user, String newPassword)
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

    //function to follow a user
    public static void follow_a_user(long id_user1, long id_user2){

        try (Session session = Neo4jDriver.getDriver().session()) {

            session.writeTransaction(tx -> {
                tx.run( "MATCH (a:User) WHERE a.id = $id1 " +
                                "MATCH (b:User) WHERE b.id = $id2 " +
                                "CREATE (a)-[:FOLLOW]->(b)",
                        parameters("id1", id_user1, "id2", id_user2)).consume();
                System.out.println("I due utenti si seguono");
                return 1;
            });
        }
    }

    //function to unfollow a user
    public static boolean unfollowUser(long id_user1, long id_user2){

        try (Session session = Neo4jDriver.getDriver().session()) {

            session.writeTransaction(tx -> {
                tx.run( "MATCH (a:User) WHERE a.id = $id1 " +
                                "MATCH (b:User) WHERE b.id = $id2 " +
                                "MATCH (a)-[r:FOLLOW]->(b)"+
                                "DELETE r",
                        parameters("id1", id_user1, "id2", id_user2)).consume();
                System.out.println("I due utenti hanno smesso di seguirsi");
                return 1;
            });
        }catch (Exception e){
            System.err.println("errore nella unfollow");
            return false;
        }
        return true;
    }

    public static boolean delete_user(userDTO user)
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
}

