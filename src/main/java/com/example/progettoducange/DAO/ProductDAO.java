package com.example.progettoducange.DAO;
import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.ProductInFridge;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import com.mongodb.BasicDBList;
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
    public static LocalDate getExpiringDateFormatted(String myinput){
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


    //function to remore an element
    public static void remove_product_mongo(productDTO product_to_delete, int id) {

        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            Document doc = collection.find(eq("id", id)).first();

            String date = product_to_delete.getDate().getDayOfMonth() + "/" +
                    product_to_delete.getDate().getMonthValue() + "/" +
                    product_to_delete.getDate().getYear();

            Bson query = eq("id", Application.authenticatedUser.getId());

            BasicDBObject update =
                    new BasicDBObject("fridge",
                            new BasicDBObject("name", product_to_delete.getName())
                                    .append("expiringDate", date)
                    );
            collection.updateOne(query, new BasicDBObject("$pull", update));

            } catch (Exception error) {
            System.out.println( error );
        }
    }

    public static void add_product(productDTO p){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            BasicDBObject query = new BasicDBObject();
            query.put( "id", Application.authenticatedUser.getId());

            BasicDBObject product_mongo = new BasicDBObject();
            product_mongo.put("name", p.getName());
            product_mongo.put("quantity", p.getQuantity());
                LocalDate date = p.getDate();
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
                String text = date.format(formatters);
            product_mongo.put("expiringDate", text);

            BasicDBObject update = new BasicDBObject();
            update.put("$push", new BasicDBObject("fridge",product_mongo));

            collection.updateOne(query, update);
        } catch (Exception error) {
            System.err.println( error );
        }
    }

    public static boolean deleteProduct(IngredientDTO ingred)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            collection.deleteOne(eq("food", ingred.getFood()));
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }
}



/*
    Bson query = and(
            eq("_id", Application.authenticatedUser.getId()),
            eq("fridge.name", p.getName()),
            eq("fridge.expiringDate", date )
    );

    Document documentList = new Document();
        documentList.append(String.format("%s.$.%s","fridge","quantity"), "quantity update 1");
        Document document = new Document("$set",documentList));

    Single.fromPublisher(
            this.repository.getCollection(
            ConstantValues.PRODUCT_CATEGORY_COLLECTION_NAME, Category.class)
            .updateOne(query, document)).subscribe();


Bson query = and(
        eq("id", Application.authenticatedUser.getId()),
        eq("fridge.name", p.getName()),
        eq("fridge.expiringDate", date )
);

    BasicDBObject set = new BasicDBObject(
            "$set",
            new BasicDBObject("System.system_type.Tenant.$.Tenant_Info", p.getQuantity())
    );
collection.updateOne(query, set);



 */

