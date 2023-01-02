package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.RecipeDao;
import com.example.progettoducange.DTO.RecipeDTO;
import com.example.progettoducange.DTO.ReviewDTO;
import com.example.progettoducange.model.ProductInFridge;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AllCommentsController {
    @FXML
    private ScrollPane Box;
    @FXML
    private VBox content;
    private Integer called_times_reviews = 0;
    private final Integer how_much_comments = 20;
    public static RecipeDTO Recipe;

    public void printSingleComment(VBox content, ReviewDTO[] r, Integer i)
    {
        String name = "Username: " + r[i].getProfile() + "\t \t \t" + "Rate: " + Integer.toString(r[i].getRate());
        Label profile = new Label(name);
        content.setPrefHeight(content.getPrefHeight() + profile.getPrefHeight());
        content.getChildren().add(profile);

        TextArea field = new TextArea(r[i].getComment());
        Integer charachters = r[i].getComment().length();
        Integer division = (charachters > 500)? 5 : (charachters > 300)? 3 : (charachters < 70) ? 1 : 2;
        Float size = (float) (charachters / division);
        field.setMinHeight(size);
        field.setMinWidth(Double.parseDouble("200"));

        field.setWrapText(true);
        content.getChildren().add(field);
    }

    public void show_more(ReviewDTO[] r, Button button)
    {
        Integer i = 0;
        while(called_times_reviews < r.length && i++ < how_much_comments){
            printSingleComment(content, r, called_times_reviews++);
        }

        /* // si bugga, riporta sempre al primo commento con la visuale
        if(called_times_reviews != how_much_comments)
            content.getChildren().remove(button);


        create_button("Show more", "Show_more",r ); */
    }

    public void create_button(String name, String id, ReviewDTO[] r)
    {
        Button button = new Button(name);
        button.setId(id);

        button.setOnAction(event -> {
            if(name.equals("Back"))
                goBack();
            else if(name.equals("Show more"))
                show_more(r, button);
        });

        content.getChildren().add(button);
    }

    public void printComments(ReviewDTO[] r) {
        create_button("Back", "Back", r);
        create_button("Show more", "Show_more",r );
        show_more(r, null);
    }

    public void initialize(){
        ReviewDTO[] review = Recipe.getReviews();

        content = new VBox();
        Box.setContent(content);

        printComments(review);
    }

    public void goBack()
    {
        try {
            Recipe = null;
            Application.changeScene("ViewRecipe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
