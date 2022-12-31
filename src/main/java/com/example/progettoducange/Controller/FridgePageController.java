package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DbMaintaince.MongoDbDriver;
import com.example.progettoducange.model.ProductInFridge;
import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FridgePageController {
    @FXML
    private TableView<ProductInFridge> FridgeTable;
    @FXML
    public TableColumn<ProductInFridge, String> ProductNameColumn;
    @FXML
    public TableColumn<ProductInFridge, Integer> ProductQuantityColumn;
    @FXML
    public TableColumn<ProductInFridge, Date> ProductExpireDateColumn;
    @FXML
    private Button IncrementButton;
    @FXML
    private Button DecrementButton;
    private ObservableList<ProductInFridge> data = FXCollections.observableArrayList();
    private boolean prova = true;

    @FXML
    private void goToHome() {
        try {
            FridgeDAO.updateFridge(data);
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        fillTable();

        DecrementButton.setOnAction(event -> {
            if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
                Integer index = FridgeTable.getSelectionModel().getSelectedIndex();
                ProductInFridge prod = FridgeTable.getItems().get(index);
                if (prod.getQuantity() - 1 != 0) {
                    prod.setQuantity(prod.getQuantity() - 1);
                    FridgeTable.getItems().set(index, prod);
                } else
                    remove_product(event);
            }
        });

        IncrementButton.setOnAction(event -> {
            if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
                Integer index = FridgeTable.getSelectionModel().getSelectedIndex();
                ProductInFridge prod = FridgeTable.getItems().get(index);
                prod.setQuantity(prod.getQuantity() + 1);
                FridgeTable.getItems().set(index, prod);
            }
        });
    }

    public void fillTable() {
        if (prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge, String>("name")
            );
            ProductQuantityColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge, Integer>("quantity")
            );
            ProductExpireDateColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge, Date>("expireDate")
            );
            FridgeTable.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in frigo");
        }

        System.out.println("Inserimento dati in frigo");

        // ########################################################################################################
        // Per la quantità nel frigo, bisogna ogni volta scorrere il db per vedere, di ogni prodotto, quanti ne ho nel frigo?
        // Oppure magari, quando un utente fa login, si salva in locale il frigo con le rispettive quantità? In questo secondo caso, è un KVDB?
        // ########################################################################################################

        //retrive ingredient from fridge
        ArrayList<productDTO> ingredientList = new ArrayList<>();
        ingredientList = ProductDAO.getIngredients(Application.authenticatedUser);

        for (productDTO us : ingredientList) {
            ProductInFridge newrow = new ProductInFridge(us.getName(), us.getQuantity(), us.getDate());
            data.add(newrow);
        }

    }

    public void remove_product(ActionEvent actionEvent) {
        if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
            ProductInFridge selectedItem = FridgeTable.getSelectionModel().getSelectedItem();
            FridgeTable.getItems().remove(selectedItem);
            //create a product and remove it from the db
            productDTO product_to_delete = new productDTO(
                    selectedItem.getName(),
                    selectedItem.getQuantity(),
                    selectedItem.getExpireDate()
            );
            ProductDAO.remove_product_mongo(product_to_delete, Application.authenticatedUser.getId());
        }
    }

}


