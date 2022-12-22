package com.example.progettoducange.DbMaintaince;

import com.mongodb.ConnectionString;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

// ##############################################################
// QUESTA E' LA BASE, DA IMPLEMENTARE MEGLIO
// ##############################################################

import org.neo4j.driver.*;

import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class Neo4jDriver{
    private final Driver driver;
    Neo4jDriver(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    void create_User(String name, int id){ //ok
        try (Session session = driver.session()) {
            //merge cause maybe the user may not be present
            session.writeTransaction(tx -> {
                tx.run("MERGE (a:User {name: $name, id: $id})",
                        parameters("name", name, "id", id)).consume();
                return 1;
            });
        }
    }

    String show_User(int id){
        try (Session session = driver.session()) {
            //merge cause maybe the user may not be present
            String name = session.readTransaction((TransactionWork<String>) tx -> {

                Result result = tx.run("MATCH (a:User) WHERE a.id = $id"+
                                " RETURN a.name as name",
                        parameters("id", id));
                if(result.hasNext())
                {
                    String c = result.next().get("name").asString();
                    return c;
                }
                return null;
            });

        }
        return null;
    }

    void follow_a_user(int id_user1, int id_user2){

        try (Session session = driver.session()) {
            //merge cause maybe the user may not be present
            session.writeTransaction(tx -> {
                tx.run( "MATCH (a:User) WHERE a.id = $id1 " +
                                "MATCH (b:User) WHERE b.id = $id2 " +
                                "CREATE (a)-[:FOLLOW]->(b)",
                        parameters("id1", id_user1, "id2", id_user2)).consume();
                return 1;
            });
        }
    }

    void create_recipe(String name, int id_receipe, int id_user ){
        try (Session session = driver.session()) {
            //create a recipe
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
    }}

//function of analytics