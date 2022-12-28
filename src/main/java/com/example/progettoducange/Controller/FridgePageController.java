package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.model.ProductInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private void goToHome()
    {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize()
    {
        fillTable();
    }

    public void DecrementQuantity()
    {

        DecrementButton.setOnAction(event -> {

            List prod = FridgeTable.getItems();
            ProductInFridge f = (ProductInFridge) prod.get(0);
            f.setQuantity(0);
            System.out.println(f);

            //prod.setQuantity(prod.getQuantity() - 1);
            //FridgeTable.getItems().add(FridgeTable.getSelectionModel().getSelectedIndex(), prod);
            //FridgeTable.getItems().remove(FridgeTable.getSelectionModel().getSelectedIndex()-1);
        });
        /*
        DecrementButton.setOnAction(event -> {
            ProductInFridge fridge = FridgeTable.getItems().get(FridgeTable.getSelectionModel().getSelectedIndex());

            for(ProductInFridge p : data)
            {
                System.out.println(p.getQuantity());
                if(p.equals(fridge))
                    if(fridge.getQuantity() != 0)
                        data.get(data.indexOf(p)).setQuantity(p.getQuantity() - 1);
            }
        });
        FridgeTable.setItems(data);
        FridgeTable.refresh();*/
    }

    public void IncrementQuantity()
    {
        System.out.println("Ciao");
        DecrementButton.setOnAction(event -> {
            System.out.println("Ciao2");
            ProductInFridge fridge = FridgeTable.getItems().get(FridgeTable.getSelectionModel().getSelectedIndex());
            System.out.println(fridge);
            fridge.setQuantity(fridge.getQuantity() + 1);
        });
    }

    public void fillTable() {
        if(prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,String>("name")
            );
            ProductQuantityColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,Integer>("quantity")
            );
            ProductExpireDateColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,Date>("expireDate")
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

        for(productDTO us : ingredientList){
            ProductInFridge newrow = new ProductInFridge(us.getName(),us.getQuantity(), us.getDate());
            data.add(newrow);
        }

    }
}

