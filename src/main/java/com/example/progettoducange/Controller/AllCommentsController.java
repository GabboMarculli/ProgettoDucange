package com.example.progettoducange.Controller;

import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.ReviewDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class AllCommentsController {
    @FXML
    private ScrollPane Box;
    public static RecipeDTO Recipe;

    public void addSingleComment(VBox content, ReviewDTO[] r, Integer i)
    {
        String name = "Username: " + r[i].getProfile() + "\t \t \t" + "Rate: " + Integer.toString(r[i].getRate());
        Label profile = new Label(name);
        content.setPrefHeight(content.getPrefHeight() + profile.getPrefHeight());
        content.getChildren().add(profile);

        TextArea field = new TextArea(r[i].getComment());
        Integer charachters = r[i].getComment().length();
        Integer division = (charachters > 500)? 5 : (charachters > 300)? 3 : 2;
        Float size = (float) (charachters / division);
        field.setMinHeight(size);
        field.setMinWidth(Double.parseDouble("200"));

        field.setWrapText(true);
        content.getChildren().add(field);
    }

    public void addComments(ReviewDTO[] r)
    {
        VBox content = new VBox();
        Box.setContent(content);

        for (int i = 0; i < r.length; i++)
        {
           addSingleComment(content, r, i);
        }
    }

    public void initialize(){
        ReviewDTO[] review = Recipe.getReviews();

        Integer i = 0;
        while(i++ < review.length)
        {
            addComments(review);
        }
    }
}
