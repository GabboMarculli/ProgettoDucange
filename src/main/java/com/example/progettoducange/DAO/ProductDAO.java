package com.example.progettoducange.DAO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.RegisteredUser;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.RegisteredUser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class ProductDAO {

    //get ingredients from the fridge
    public static ArrayList<productDTO> getIngredients(RegisteredUser user){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            Bson projectionFields = Projections.fields(
                    Projections.include("fridge"),
                    Projections.excludeId());

            ArrayList<productDTO> products_return = new ArrayList<>();
            Document obj = collection.find(eq("username", user.getUsername())).projection(projectionFields).first();
            ArrayList<Document> array_of_document = (ArrayList<Document>) obj.get("fridge");

            for (int i = 0; i < array_of_document.size(); i++) {
                Document appoggio = array_of_document.get(i);
                products_return.add(
                        new productDTO(
                                appoggio.getString("name"),
                                appoggio.getInteger("quantity"),
                                getExpiringDateFormatted(appoggio.getString("expiringDate"))
                        )
                );
            }return products_return;

        } catch (Exception error) {
            System.out.println( error );
            return null;
        }
    }

    //function to get a LocalDate type from a String. I'll do the cascade of try-catch cause the format of the date may vary
    private static LocalDate getExpiringDateFormatted(String myinput){
        DateTimeFormatter pattern;
        LocalDate datetime;
        try {
            pattern = DateTimeFormatter.ofPattern("dd/M/yyyy");
            datetime = LocalDate.parse(myinput, pattern);
        } catch (DateTimeParseException e) {
            try {
                pattern = DateTimeFormatter.ofPattern("d/M/yyyy");
                datetime = LocalDate.parse(myinput, pattern);
            } catch (DateTimeParseException e1) {
                try {
                    pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    datetime = LocalDate.parse(myinput, pattern);
                } catch (DateTimeParseException e2) {
                    try {
                        pattern = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        datetime = LocalDate.parse(myinput, pattern);
                    } catch (DateTimeParseException e3) {
                        System.err.println(e3);
                        return null;
                    }
                }
            }
        }
        return datetime;
    }

    //DA COMPLETARE -> non so come rimuovere un prodotto dal frigo
    /*
        db.User.aggregate([
	        { $unwind: "$fridge" },
	        { $match: { "fridge.name": "YYmargarine" }
        }])

     */
    public static void remove_product_mongo(productDTO product_to_delete, int id) {
        Document ciao;
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();


            List<DBObject> criteria = new ArrayList<DBObject>();
            criteria.add(new BasicDBObject("id", new BasicDBObject("$eq", id)));
            criteria.add(new BasicDBObject("fridge.name", new BasicDBObject("$eq", product_to_delete.getName())));
            criteria.add(new BasicDBObject("fridge.quantity", new BasicDBObject("$eq", product_to_delete.getQuantity())));
            //criteria.add(new BasicDBObject("fridge.expiringDate", new BasicDBObject("$eq", product_to_delete.getDate())));
            ciao = collection.find(new BasicDBObject("$and", criteria)).first();




        } catch (Exception error) {
            System.out.println( error );

        }
    }
}




