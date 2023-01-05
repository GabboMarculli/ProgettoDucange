package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class AddRecipeController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";
    @FXML
    private TextField RecipeTitle;
    @FXML
    private TextField PreparationTime;
    @FXML
    private TextField CookTime;
    @FXML
    private TextField TotalTime;
    @FXML
    private TextArea Ingredients;
    @FXML
    private TextArea Directions;
    @FXML
    private Label invalidRecipe;

    @FXML
    private void addRecipe()
    {
        if (RecipeTitle.getText().isBlank() || Ingredients.getText().isBlank() || Directions.getText().isBlank()
            || CookTime.getText().isBlank() || TotalTime.getText().isBlank() || PreparationTime.getText().isBlank()) {
            invalidRecipe.setText("The recipe fields are required!");
            invalidRecipe.setStyle(errorMessage);

            if (RecipeTitle.getText().isBlank()) {
                RecipeTitle.setStyle(errorStyle);
            } else if (PreparationTime.getText().isBlank()) {
                PreparationTime.setStyle(errorStyle);
            } else if (CookTime.getText().isBlank()) {
                CookTime.setStyle(errorStyle);
            } else if (TotalTime.getText().isBlank()){
                TotalTime.setStyle(errorStyle);
            }else if (Ingredients.getText().isBlank()) {
                Ingredients.setStyle(errorStyle);
            } else if (Directions.getText().isBlank()) {
                Directions.setStyle(errorStyle);
            }
        } else if(!Utils.isNumeric(CookTime.getText()) || !Utils.isNumeric(TotalTime.getText()) ||
                !Utils.isNumeric(PreparationTime.getText())){
            invalidRecipe.setText("The time fields required integer values!");
            invalidRecipe.setStyle(errorMessage);

            if(!Utils.isNumeric(CookTime.getText())) {
                CookTime.setStyle(errorStyle);
            } else if(!Utils.isNumeric(TotalTime.getText())) {
                TotalTime.setStyle(errorStyle);
            } else if(!Utils.isNumeric(PreparationTime.getText())) {
                PreparationTime.setStyle(errorStyle);
            }
        } else{
            RecipeDTO new_recipe = new RecipeDTO(
                        RecipeTitle.getText(),
                        RecipeDao.get_id_recipe(), //add id
                        0, // at the beginning it has no reviews
                        null, //add photo
                        Application.authenticatedUser.getUsername(),
                        PreparationTime.getText(),
                        CookTime.getText(),
                        TotalTime.getText(),
                        Ingredients.getText(),
                        Directions.getText(),
                        return_list_of_ingredient(Ingredients.getText()),
                        null
                );

                if (!RecipeDao.addRecipe(new_recipe)) {
                    invalidRecipe.setText("Recipe adding is failed!");
                    invalidRecipe.setStyle(errorMessage);
                } else {
                    invalidRecipe.setText("Add Successful!");
                    invalidRecipe.setStyle(successMessage);
                }
            }
    }

    private String[] return_list_of_ingredient(String list){

        String array[] = list.split(",");
        String[] return_list_of_ingredient = new String[array.length];
        for(int i=0; i<array.length;i++){
            String supporto[] = array[i].split(":");
            return_list_of_ingredient[i] = supporto[0];
        }
        return return_list_of_ingredient;
    }

    @FXML
    private void goToAllRecipe() throws IOException {
        Application.changeScene("AllRecipes");
    }

}
