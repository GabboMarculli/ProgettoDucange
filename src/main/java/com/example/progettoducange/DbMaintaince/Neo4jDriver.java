package com.example.progettoducange.DbMaintaince;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDriver {

    private static Neo4jDriver NeoDriver = null;
    private static Driver driver;
    private String uri;
    private String user;
    private String password;

    private Neo4jDriver()
    {
        uri = "bolt://localhost:7687";
        user = "neo4j";
        password = "rootroot";
        try{
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        } catch (Exception me) {
            System.err.println(me);
        }
    }

    // singleton pattern
    public static Neo4jDriver getInstance() {
        if(NeoDriver == null){
            NeoDriver = new Neo4jDriver();
        }
        return NeoDriver;
    }

    public static Driver  getDriver(){
        return driver;
    }
}
