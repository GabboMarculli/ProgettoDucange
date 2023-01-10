package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.*;
import com.example.SmartFridge.DTO.IngredientInTheFridgeDTO;
import com.example.SmartFridge.model.IngredientInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FridgePageController {
    @FXML
    private TableView<IngredientInFridge> FridgeTable;
    @FXML
    public TableColumn<IngredientInFridge, String> ProductNameColumn;
    @FXML
    public TableColumn<IngredientInFridge, Integer> ProductQuantityColumn;
    @FXML
    public TableColumn<IngredientInFridge, Date> ProductExpireDateColumn;
    @FXML
    private Button IncrementButton;
    @FXML
    private Button DecrementButton;
    private ObservableList<IngredientInFridge> data = FXCollections.observableArrayList();
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
                IngredientInFridge prod = FridgeTable.getItems().get(index);
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
                IngredientInFridge prod = FridgeTable.getItems().get(index);
                prod.setQuantity(prod.getQuantity() + 1);
                FridgeTable.getItems().set(index, prod);
            }
        });
    }

    public void fillTable() {
        if (prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<IngredientInFridge, String>("name")
            );
            ProductQuantityColumn.setCellValueFactory(
                    new PropertyValueFactory<IngredientInFridge, Integer>("quantity")
            );
            ProductExpireDateColumn.setCellValueFactory(
                    new PropertyValueFactory<IngredientInFridge, Date>("expireDate")
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
        ArrayList<IngredientInTheFridgeDTO> ingredientList = new ArrayList<>();
        ingredientList = IngredientInTheFridgeDAO.getProduct(Application.authenticatedUser);

        for (IngredientInTheFridgeDTO us : ingredientList) {
            IngredientInFridge newrow = new IngredientInFridge(us.getName(), us.getQuantity(), us.getDate());
            data.add(newrow);
        }

    }

    public void remove_product(ActionEvent actionEvent) {
        if (FridgeTable.getSelectionModel().getSelectedIndex() >= 0) {
            IngredientInFridge selectedItem = FridgeTable.getSelectionModel().getSelectedItem();
            FridgeTable.getItems().remove(selectedItem);
            //create a product and remove it from the db
            IngredientInTheFridgeDTO product_to_delete = new IngredientInTheFridgeDTO(
                    selectedItem.getName(),
                    selectedItem.getQuantity(),
                    selectedItem.getExpireDate()
            );
            IngredientInTheFridgeDAO.remove_product_mongo(product_to_delete, Application.authenticatedUser.getId());
        }
    }

}


