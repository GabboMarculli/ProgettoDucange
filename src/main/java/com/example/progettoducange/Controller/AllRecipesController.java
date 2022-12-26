package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.*;
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


public class AllRecipesController {

    @FXML
    private TableView<RecipeDTO> AllRecipesTable;
    @FXML
    public TableColumn<RecipeDTO, String> RecipeNameColumn;
    @FXML
    public TableColumn<RecipeDTO, String> NumberOfLike;

    private ObservableList<RecipeDTO> data = FXCollections.observableArrayList();

    public AllRecipesController(){

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

    public void initialize(){
        RecipeNameColumn.setCellValueFactory(
                new PropertyValueFactory<RecipeDTO,String>("Name")
        );
        NumberOfLike.setCellValueFactory(
                new PropertyValueFactory<RecipeDTO,String>("Like")
        );

        AllRecipesTable.setItems(data);
        FillTable();
    }

    public void FillTable()
    {

        System.out.println("Inserimento dati in frigo");

        ArrayList<RecipeDTO> recipes = RecipeDao.getRecipe(20);

        for(RecipeDTO us : recipes) {
            data.add(us);
        }

    }


    @FXML
    private void addRecipe()
    {
        try {
            Application.changeScene("AddRecipe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
