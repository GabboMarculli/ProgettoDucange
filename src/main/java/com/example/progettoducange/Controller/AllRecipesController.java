package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.FridgeDAO;
import com.example.progettoducange.DAO.IngredientDAO;
import com.example.progettoducange.DAO.ProductDAO;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.*;
import com.example.progettoducange.DbMaintaince.Neo4jDriverExample;
import com.example.progettoducange.model.ProductInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public TextField SearchRecipe;
    @FXML
    public GridPane Right;
    @FXML
    private AnchorPane bottom;

    @FXML
    private Button ShowMoreRecipe;

    private ObservableList<RecipeDTO> data = FXCollections.observableArrayList();

    public AllRecipesController(){

    }
    @FXML
    private void goToHome()
    {
        try {
            if(Application.authenticatedUser.getUsername().equals("admin"))
                Application.changeScene("HomePageAdmin");
            else
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
                    if(Application.authenticatedUser.getUsername().equals("admin"))
                    {
                        modifyRecipe(rowData);
                    } else {
                        viewRecipe(rowData);
                    }
                }
            });
            return row ;
        });
        FillTable();

        // se l'utente è l'admin, metto il pulsante "elimina ricetta", altrimenti se sei un utente normale vedi "aggiungi ricetta"
        Button button = new Button();
        button.setAlignment(Pos.valueOf("CENTER"));
        button.setContentDisplay(ContentDisplay.valueOf("CENTER"));
        button.setLayoutX(203);
        button.setLayoutY(552);
        button.setMnemonicParsing(false);

        if(Application.authenticatedUser.getUsername().equals("admin"))
            button.setText("Delete recipe");
        else
            button.setText("Add a recipe");

        button.setOnAction(event->{
            if(Application.authenticatedUser.getUsername().equals("admin")) {
                if (AllRecipesTable.getSelectionModel().getSelectedIndex() >= 0) {
                    RecipeDTO selectedItem = AllRecipesTable.getSelectionModel().getSelectedItem();
                    AllRecipesTable.getItems().remove(selectedItem);
                    RecipeDao.removerecipe(selectedItem);
                    Neo4jDriverExample.delete_Recipe(selectedItem.getName(), selectedItem.getId());
                }
            } else {
                    addRecipe();
            }
        });

        bottom.getChildren().add(button);


    }

    //funcion that pressed will show "limit" recipe at a time.
    //the use of called time is specified in getRecipe function
    int called_times = 0;
    public void FillTable()
    {
        ShowMoreRecipe.setDisable(false);
        int limit_views_recipe = 20;
        List<RecipeDTO> recipes = RecipeDao.getRecipe(limit_views_recipe,called_times);
        for(RecipeDTO us : recipes) {
            data.add(us);
        }
        called_times++;
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
    private void modifyRecipe(RecipeDTO rowData)
    {
        try {
            rowData = RecipeDao.getSingleRecipe(rowData);
            ModifyRecipeController.Recipe = rowData;
            Application.changeScene("ModifyRecipe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function called when visualize all the characterstic of a recipe
    @FXML
    private void viewRecipe(RecipeDTO rowData)
    {
        try {
            rowData = RecipeDao.getSingleRecipe(rowData);
            ViewRecipeController.Recipe = rowData;
            Application.changeScene("ViewRecipe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function that will search for a user

    public void Search_for_recipe(ActionEvent actionEvent) {
        String recipeName = SearchRecipe.getText();
        if(!recipeName.equals("")) {
            try {
                ArrayList<RecipeDTO> searched_recipe = RecipeDao.getSearchedRecipe(recipeName);
                if(searched_recipe != null)
                {
                    data.clear();
                    data.addAll(searched_recipe);
                    AllRecipesTable.setItems(data);
                }
            } catch (Error e){
                System.out.println(e);
            }
        }
    }

    public void show_recipe_of_followed_user(ActionEvent actionEvent) {
        ShowMoreRecipe.setDisable(true);
        List<RecipeDTO> searched_recipe = RecipeDao.recipe_of_followed_user();
        if(searched_recipe != null)
        {

            data.clear();
            data.addAll(searched_recipe);
            AllRecipesTable.setItems(data);
        }

    }

    //will retrive the product that she has in their fridge and the system will
    // suggest the recipe that has the same ingredient
    public void show_suggested_recipe(ActionEvent actionEvent) {
        ShowMoreRecipe.setDisable(true);
        //retrive fridge of the user
        ArrayList<productDTO> list_of_product = ProductDAO.getProduct(Application.authenticatedUser);
        //retrive the suggested recipe
        String[] searched_ingredients = new String[list_of_product.size()];
        for(int i=0; i<list_of_product.size();i++){
            searched_ingredients[i] = list_of_product.get(i).getName();
        }
        ArrayList<RecipeDTO> list_of_recipe = RecipeDao.get_suggested_recipe_by_ingredient(searched_ingredients);
        if(list_of_recipe != null)
        {
            data.clear();
            data.addAll(list_of_recipe);
            AllRecipesTable.setItems(data);
        }
    }

    public void showMyRecipe(ActionEvent actionEvent) {
        ShowMoreRecipe.setDisable(true);
        data.clear();
        int limit_views_recipe = 20;
        List<RecipeDTO> recipes = RecipeDao.getMyRecipe(limit_views_recipe,0);
        for(RecipeDTO us : recipes) {
            data.add(us);
        }
        AllRecipesTable.setItems(data);
        called_times++;
    }
}
