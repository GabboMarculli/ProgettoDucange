package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.RecipeDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ModifyRecipeController {
    @FXML
    public TextField RecipeTitle;
    @FXML
    public TextField RecipeAuthor;
    @FXML
    public TextArea RecipeIngredients;
    @FXML
    public TextArea RecipeDirections;
    @FXML
    public Button Modify;

    public static RecipeDTO Recipe;

    public void initialize()
    {
        RecipeTitle.setText(Recipe.getName());
        RecipeAuthor.setText(Recipe.getAuthor());
        RecipeIngredients.setText(Recipe.getIngrients());
        RecipeDirections.setText(Recipe.getDirection());

        Modify.setOnAction(actionEvent -> {
            RecipeDTO new_recipe = Recipe;
            RecipeDao.removerecipe(Recipe);
            Recipe = new_recipe;
            RecipeDao.addRecipe(new_recipe);
        });
    }

    public void goBack()
    {
        try {
            Recipe = null;
            Application.changeScene("AllRecipes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToComments()
    {
        try {
            AllCommentsController.Recipe = Recipe;
            Application.changeScene("AllComments");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
