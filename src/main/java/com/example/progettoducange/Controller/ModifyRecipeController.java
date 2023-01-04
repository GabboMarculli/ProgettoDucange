package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.IngredientDAO;
import com.example.progettoducange.DAO.ProductDAO;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.DTO.RecipeDTO;
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

}
