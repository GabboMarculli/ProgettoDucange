package com.example.progettoducange.DAO;

import com.example.progettoducange.Application;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.ProductInFridge;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FridgeDAO {

    // ############################################################################
    //  NON FUNZIONA
    // ###########################################################################

    public static boolean updateFridge2(ObservableList<ProductInFridge> fridge)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

            Document doc = new Document();
            for(ProductInFridge p : fridge){
                String date = p.getExpireDate().getDayOfMonth()+"/"+
                        p.getExpireDate().getMonthValue()+"/"+
                        p.getExpireDate().getYear();
                doc.append("name", p.getName()).append("quantity", p.getQuantity()).append("expiringDate", date);
            }

            Bson query = new Document("$set", doc);
            collection.updateOne(new Document("fridge", Application.authenticatedUser.getFridge()), query);

            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    public static boolean updateFridge22(ObservableList<ProductInFridge> fridge)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();


            BasicDBObject query = new BasicDBObject();
            query.put( "id", Application.authenticatedUser.getId());


            for(ProductInFridge p : fridge) {

                BasicDBObject product_mongo = new BasicDBObject();
                product_mongo.put("name", p.getName());
                product_mongo.put("quantity", p.getQuantity());
                        String date = p.getExpireDate().getDayOfMonth() + "/" +
                            p.getExpireDate().getMonthValue() + "/" +
                            p.getExpireDate().getYear();
                product_mongo.put("expiringDate", date);
                BasicDBObject update = new BasicDBObject();
                update.put("$", new BasicDBObject("fridge",product_mongo));
                collection.updateOne(query, update);
            }
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    public static boolean updateFridge(ObservableList<ProductInFridge> fridge)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

            for(ProductInFridge p : fridge) {

                String date = p.getExpireDate().getDayOfMonth() + "/" +
                        p.getExpireDate().getMonthValue() + "/" +
                        p.getExpireDate().getYear();

                Bson query = and(
                        eq("id", Application.authenticatedUser.getId()),
                        eq("fridge.name", p.getName()),
                        eq("fridge.expiringDate", date )

                );

                Document documentList = new Document();
                documentList.append(String.format("%s.$.%s","fridge","quantity"), p.getQuantity());
                Document document = new Document("$set",documentList);//$set

                collection.updateOne(query, document);

                remove_product_with_0_as_quantity();

            }
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    private static void remove_product_with_0_as_quantity() {
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        Bson query = eq("id", Application.authenticatedUser.getId());

        BasicDBObject update =
                new BasicDBObject("fridge",
                        new BasicDBObject("quantity",0)
                );
        collection.updateOne(query, new BasicDBObject("$pull", update));

    }
}
