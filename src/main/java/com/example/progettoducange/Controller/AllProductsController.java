package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.IngredientDAO;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DTO.userDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.progettoducange.DAO.userDAO.getListOfUser;

public class AllProductsController {
    @FXML
    public TableView<IngredientDTO> AllProductsTable;
    @FXML
    public TableColumn<IngredientDTO, String> ProductNameColumn;
    @FXML
    public TableColumn<IngredientDTO, String> QuantityInMyFridge;

    public boolean prova = true;

    private ObservableList<IngredientDTO> data = FXCollections.observableArrayList();

    public void initialize()
    {
        fillTable();
    }

    public void fillTable()
    {
        if(prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<IngredientDTO,String>("food")
            );
            QuantityInMyFridge.setCellValueFactory(
                    new PropertyValueFactory<IngredientDTO,String>("measure")
            );

            AllProductsTable.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in Product");
        }

        ArrayList<IngredientDTO> ingredientList = IngredientDAO.getListOfIngredient(20);

        // ########################################################################################################
        // Per la quantità nel frigo, bisogna ogni volta scorrere il db per vedere, di ogni prodotto, quanti ne ho nel frigo?
        // Oppure magari, quando un utente fa login, si salva in locale il frigo con le rispettive quantità? In questo secondo caso, è un KVDB?
        // ########################################################################################################

        for(IngredientDTO us : ingredientList) {
            data.add(us);
        }


    }

    @FXML
    private void goToHome()
    {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
