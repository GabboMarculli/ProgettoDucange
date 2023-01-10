package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DTO.RecipeDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;

import static com.example.SmartFridge.DAO.RecipeDao.updateRecipe;

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
            boolean[] updating = new boolean[4];
            if(!Recipe.getName().equals(RecipeTitle.getText())){
                updating[0] = true;
                Recipe.setName(RecipeTitle.getText());
            }  if(!Recipe.getAuthor().equals(RecipeAuthor.getText())) {
                updating[1] = true;
                Recipe.setAuthor(RecipeAuthor.getText());
            }  if(!Recipe.getIngrients().equals(RecipeIngredients.getText())) {
                updating[2] = true;
                Recipe.setIngrients(RecipeIngredients.getText());
            }  if(!Recipe.getDirection().equals(RecipeDirections.getText())){
                updating[3] = true;
                Recipe.setDirection(RecipeDirections.getText());
            }

            updateRecipe(Recipe, updating);
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
