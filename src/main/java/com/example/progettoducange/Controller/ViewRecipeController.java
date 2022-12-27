package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.RecipeDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class ViewRecipeController {
    @FXML
    private Label RecipeTitle;
    @FXML
    private Label RecipeAuthor;
    @FXML
    private TextArea RecipeIngredients;
    @FXML
    private TextArea RecipeDirections;
    @FXML
    private Text Review;
    public static RecipeDTO Recipe;

    public void initialize()
    {
        RecipeTitle.setText(Recipe.getName());
        RecipeAuthor.setText(Recipe.getAuthor());
        RecipeIngredients.setText(Recipe.getIngrients());
        RecipeDirections.setText(Recipe.getDirection());
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
