package com.example.progettoducange.DAO;

import com.example.progettoducange.Application;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.ProductInFridge;
import com.example.progettoducange.model.RegisteredUser;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;

public class FridgeDAO {

    // ############################################################################
    //  NON FUNZIONA
    // ###########################################################################

    public static boolean updateFridge(ObservableList<ProductInFridge> fridge)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

            Document doc = new Document();
            for(ProductInFridge p : fridge){
                doc.append("name", p.getName()).append("quantity", p.getQuantity()).append("expiringDate", p.getExpireDate());
            }

            Bson query = new Document("$set", doc);
            collection.updateOne(new Document("fridge", Application.authenticatedUser.getFridge()), query);

            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }
}
