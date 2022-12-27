package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class AddRecipeController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    @FXML
    private TextField RecipeTitle;
    @FXML
    private TextArea Ingredients;
    @FXML
    private TextArea Directions;
    @FXML
    private Label invalidRecipe;

    @FXML
    private void addRecipe()
    {
        if (RecipeTitle.getText().isBlank() || Ingredients.getText().isBlank() || Directions.getText().isBlank()) {
            invalidRecipe.setText("The recipe fields are required!");
            invalidRecipe.setStyle(errorMessage);

            if (RecipeTitle.getText().isBlank()) {
                RecipeTitle.setStyle(errorStyle);
            } else if (Ingredients.getText().isBlank()) {
                Ingredients.setStyle(errorStyle);
            } else if (Directions.getText().isBlank()) {
                Directions.setStyle(errorStyle);
            }
        }

        // ######################################################################
        // Come glielo passo l'id?
        // ingredients dev'essere una lista di prodotti o va bene metterlo stringa dentro 'recipe' ?  -> va messo come array
        // E poi, possono esistere due ricette con lo stesso nome? -> ma si
        // ######################################################################

        // Commento perchè altrimenti darebbe errore
        /*
        RecipeDTO new_recipe = new RecipeDTO(
                Application.authenticatedUser,
                Ingredients.getText() ,
                RecipeTitle.getText(),
                Directions.getText(), 0);
        */
        RecipeDTO new_recipe = new RecipeDTO(
                RecipeTitle.getText(),
                0, //add id
                0, // at the beginning it has no reviews
                null, //add photo
                Application.authenticatedUser.getUsername(),
                "0",
                "0",
                "0",
                Ingredients.getText(),
                Directions.getText(),
                null,
                null
                );

        //insert this recipe into mongodB and in Neo4j;
        RecipeDao.addRecipe(new_recipe);

        /*
        if(RecipeDao.addRecipe(new_recipe)){
            invalidRecipe.setText("Recipe adding is failed!");
            invalidRecipe.setStyle(errorMessage);
        } else {
            invalidRecipe.setText("Add Successful!");
            invalidRecipe.setStyle(successMessage);
        }
         */
    }

    @FXML
    private void goToAllRecipe() throws IOException {
        Application.changeScene("AllRecipes");
    }

}
