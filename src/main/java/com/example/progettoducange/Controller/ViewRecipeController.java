package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.ReviewDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TextArea ReviewText;
    @FXML
    private ToggleGroup Rate;
    @FXML
    private Label invalidSubmit;

    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";


    public static RecipeDTO Recipe;

    public void initialize()
    {
        RecipeTitle.setText(Recipe.getName());
        RecipeAuthor.setText(Recipe.getAuthor());
        RecipeIngredients.setText(Recipe.getIngrients());
        RecipeDirections.setText(Recipe.getDirection());
    }

    public void submitComment()
    {
        if(Rate.getSelectedToggle() == null){
            invalidSubmit.setText("Rate is empty!");
            invalidSubmit.setStyle(errorMessage);
        } else if(ReviewText.getText().isBlank()) {
            invalidSubmit.setText("Review is empty!");
            invalidSubmit.setStyle(errorMessage);
        } else {
            RadioButton btn = (RadioButton)Rate.getSelectedToggle();
            ReviewDTO rev = new ReviewDTO(Application.authenticatedUser.getUsername(), Integer.parseInt(btn.getText()) , ReviewText.getText());
            RecipeDao.addReview(rev, Recipe.getId());
            invalidSubmit.setText("Successfull add!");
            invalidSubmit.setStyle(successMessage);
        }
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
