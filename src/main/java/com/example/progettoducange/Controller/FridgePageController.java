package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.ReviewDTO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.model.*;
import com.example.progettoducange.model.ProductInFridge;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.LocalDate.*;

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

    // #############################################################################################################
    // Non ho idea se funzioni
    // ###########################################################################################################

    public void fillTable() {
        /*
        // serve una lista del genere
        final ObservableList<ProductInFridge> data = FXCollections.observableArrayList(
                new ProductInFridge("Jacob", 2, LocalDate.now()),
                new ProductInFridge("Isabella", 4,LocalDate.now()),
                new ProductInFridge("Ethan", 3,LocalDate.now())
        );
        ProductName.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,String>("name1")
        );
        ProductQuantity.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Integer>("quantity1")
        );
        ProductExpireDate.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Date>("date1")
        );

         */
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

        ReviewDTO review = new ReviewDTO(
                "ciao",
                5,
                "molto bello"
        );
        RecipeDao.addReview(review,7000);
    }
}

