package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.IngredientDAO;
import com.example.progettoducange.DAO.ProductDAO;
import com.example.progettoducange.DTO.IngredientDTO;
import com.example.progettoducange.Utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;

public class AddProductController {
    @FXML
    public TextField Name;
    @FXML
    public TextField Category;
    @FXML
    public TextField Carbs;
    @FXML
    public TextField Fat;
    @FXML
    public TextField Measure;
    @FXML
    public TextField Grams;
    @FXML
    public TextField Calories;
    @FXML
    public TextField Protein;
    @FXML
    public TextField Fiber;
    @FXML
    public Label invalidAdd;
    @FXML
    public Button add;

    public static IngredientDTO row;
    public static boolean modify;

    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";

    public void initialize()
    {
        if(modify)
        {
            Name.setText(row.getFood());
            Grams.setText(row.getGrams());
            Fat.setText(row.getFat());
            Fiber.setText(row.getFiber());
            Measure.setText(row.getMeasure());
            Calories.setText(row.getCalories());
            Category.setText(row.getCategory());
            Protein.setText(row.getCategory());
            Carbs.setText(row.getCarbs());

            add.setOnAction(actionEvent -> {
                IngredientDTO new_row = row;
                ProductDAO.deleteProduct(row);
                row = new_row;
                IngredientDAO.addIngredient(new_row);
            });
        } else
            add.setOnAction(event->{
                AddProduct();
            });
    }

    public boolean checkInput()
    {
        if(Name.getText().isBlank() || Grams.getText().isBlank() || Category.getText().isBlank() || Calories.getText().isBlank()
         || Fat.getText().isBlank() || Fiber.getText().isBlank() || Protein.getText().isBlank() || Carbs.getText().isBlank() ||
                Measure.getText().isBlank())
                return false;

        if(!Utils.isNumeric(Grams.getText()) || !Utils.isNumeric(Calories.getText()) || !Utils.isNumeric(Fat.getText())
          || !Utils.isNumeric(Fiber.getText()) || !Utils.isNumeric(Protein.getText()) || !Utils.isNumeric(Carbs.getText()))
            return false;

        return true;
    }

    public void AddProduct()
    {
        if(checkInput())
        {
            IngredientDTO ingredient = new IngredientDTO(Name.getText(), Measure.getText(), Grams.getText(), Calories.getText(),
                    Protein.getText(), Fat.getText(), Fiber.getText(), Carbs.getText(), Category.getText());

            IngredientDAO.addIngredient(ingredient);
            invalidAdd.setText("Product added ");
            invalidAdd.setStyle(successMessage);
        } else {
            invalidAdd.setText("The Login fields are required!");
            invalidAdd.setStyle(errorMessage);
        }
    }
    @FXML
    private void goToAllProducts()
    {
        try {
            row = null;
            modify = false;
            Application.changeScene("AllProducts");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
