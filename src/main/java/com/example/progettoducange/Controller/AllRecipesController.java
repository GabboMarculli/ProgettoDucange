package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.*;
import com.example.progettoducange.model.ProductInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
    public TableColumn<RecipeDTO, String> ReviewCount;
    @FXML
    public TableColumn<RecipeDTO, String> TotalTime;
    @FXML
    public GridPane Right;


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
        ReviewCount.setCellValueFactory(
                new PropertyValueFactory<RecipeDTO,String>("ReviewCount")
        );
        TotalTime.setCellValueFactory(
                new PropertyValueFactory<RecipeDTO,String>("TotalTime")
        );

        AllRecipesTable.setItems(data);

        AllRecipesTable.setRowFactory( tv -> {
            TableRow<RecipeDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    RecipeDTO rowData = row.getItem();
                    viewRecipe(rowData);
                }
            });
            return row ;
        });
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

    @FXML
    private void viewRecipe(RecipeDTO rowData)
    {
        try {
            ViewRecipeController.Recipe = rowData;
            Application.changeScene("ViewRecipe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
