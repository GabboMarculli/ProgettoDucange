package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.IngredientInTheFridgeDAO;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.*;
import com.example.SmartFridge.Utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    private Button add_recipe;
    //@FXML
    //private Button ShowMoreRecipe;
    @FXML
    private  Button show_recipe_of_followed_user;
    @FXML
    private  Button show_suggested_recipe;
    @FXML
    private  Button showMyRecipe;
    @FXML
    private Button previousButton;
    @FXML
    Button nextButton;
    private int page = 0;
    @FXML
    private Text pagenumber;
    public static String utente = null;

    private ObservableList<RecipeDTO> data = FXCollections.observableArrayList();
    public void setClick(MouseEvent mouseEvent) {
        Utils.setClick(mouseEvent);
    }
      public void unsetClick(MouseEvent mouseEvent) {
        Utils.unsetClick(mouseEvent);
    }
    public void setOver(MouseEvent mouseEvent) {
        Utils.setOver(mouseEvent);
    }
    public void unsetOver(MouseEvent mouseEvent) {
        Utils.unsetOver(mouseEvent);
    }
    @FXML
    protected void onNextClick(){
        page++;
        previousButton.setDisable(false);
        //List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        //data.clear();
        //for(RecipeDTO r : recipes)
        //    data.add(r);
        AllRecipesTable.getSelectionModel().select(0);
        pagenumber.setText(Integer.toString(page));
        //FillTable();
    }
    @FXML
    protected  void onPreviousClick(){
        if(page <= 0){
            return;
        }
        page--;
        pagenumber.setText(Integer.toString(page));
        if(page == 0){
            previousButton.setDisable(true);
        }
        //List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        //data.clear();
        //for(RecipeDTO r : recipes)
        //    data.add(r);
        //listview.getSelectionModel().select(0);
    }
    @FXML
    private void goToHome() throws IOException {
        utente = null;
        /*
        try {
            if(Application.authenticatedUser.getUsername().equals("admin"))
                Application.changeScene("HomePageAdmin");
            else
                Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */
        Application.changeScene("MainTable");
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
        /*
        // se l'utente è l'admin, metto il pulsante "elimina ricetta", altrimenti se sei un utente normale vedi "aggiungi ricetta"
        Button button = new Button();
        button.setAlignment(Pos.valueOf("CENTER"));
        button.setContentDisplay(ContentDisplay.valueOf("CENTER"));
        button.setLayoutX(203);
        button.setLayoutY(552);
        button.setMnemonicParsing(false);
        */

        if(Application.authenticatedUser.getUsername().equals("admin"))
            add_recipe.setText("Delete recipe");
        else
            add_recipe.setText("Add a recipe");

        add_recipe.setOnAction(event->{
            if(Application.authenticatedUser.getUsername().equals("admin")) {
                if (AllRecipesTable.getSelectionModel().getSelectedIndex() >= 0) {
                    RecipeDTO selectedItem = AllRecipesTable.getSelectionModel().getSelectedItem();
                    AllRecipesTable.getItems().remove(selectedItem);
                    RecipeDao.removerecipe(selectedItem);
                }
            } else {
                    addRecipe();
            }
        });

        //bottom.getChildren().add(button);

        if(Application.authenticatedUser.getUsername().equals("admin")) {
            show_recipe_of_followed_user.setDisable(true);
            show_suggested_recipe.setDisable(true);
            showMyRecipe.setDisable(true);
        }
    }



    //funcion that pressed will show "limit" recipe at a time.
    //the use of called time is specified in getRecipe function
    int called_times = 0;
    public void FillTable()
    {

        //ShowMoreRecipe.setDisable(false);
        int limit_views_recipe = 20;
        List<RecipeDTO> recipes = RecipeDao.getRecipe(limit_views_recipe,called_times,utente);


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
        utente = null;
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
        utente = null;
        //ShowMoreRecipe.setDisable(true);
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
        utente = null;
        if(Application.authenticatedUser.getUsername().equals("admin"))

            return;

        //ShowMoreRecipe.setDisable(true);
        //retrive fridge of the user
        ArrayList<IngredientInTheFridgeDTO> list_of_product = IngredientInTheFridgeDAO.getProduct(Application.authenticatedUser);
        //retrive the suggested recipe
        System.out.println(list_of_product);
        if(list_of_product == null || list_of_product.isEmpty()) {
            dropTable();
        }
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

    private void dropTable() {
        data.clear();
    }

    public void showMyRecipe(ActionEvent actionEvent) {
        utente = null;
        //ShowMoreRecipe.setDisable(true);
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
