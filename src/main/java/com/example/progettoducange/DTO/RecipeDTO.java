package com.example.progettoducange.DTO;

import javafx.beans.property.*;

public class RecipeDTO {
    private String Name;
    private int Id ;
    private int  ReviewCount;
    private String Photo;
    private String Author;
    private String PreparationTime;
    private String Cooktime;
    private String TotalTime;
    private String Ingredients;
    private String Direction;
    private String[] IngredientsList;
    private ReviewsDTO[] reviews;


    public RecipeDTO(String name, int id, int reviewCount, String photo, String author, String preparationTime, String cooktime, String totalTime, String ingrients,String direction, String[] ingredientsList, ReviewsDTO[] reviews) {
        Name = name;
        Id = id;
        Direction = direction;
        ReviewCount = reviewCount;
        Photo = photo;
        Author = author;
        PreparationTime = preparationTime;
        Cooktime = cooktime;
        TotalTime = totalTime;
        Ingredients = ingrients;
        IngredientsList = ingredientsList;
        this.reviews = reviews;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getReviewCount() {
        return ReviewCount;
    }

    public void setReviewCount(int reviewCount) {
        ReviewCount = reviewCount;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPreparationTime() {
        return PreparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        PreparationTime = preparationTime;
    }

    public String getCooktime() {
        return Cooktime;
    }

    public void setCooktime(String cooktime) {
        Cooktime = cooktime;
    }

    public String getTotalTime() {
        return TotalTime;
    }

    public void setTotalTime(String totalTime) {
        TotalTime = totalTime;
    }

    public String getIngrients() {
        return Ingredients;
    }

    public void setIngrients(String ingrients) {
        Ingredients = ingrients;
    }

    public String[] getIngredientsList() {
        return IngredientsList;
    }

    public void setIngredientsList(String[] ingredientsList) {
        IngredientsList = ingredientsList;
    }

    public ReviewsDTO[] getReviews() {
        return reviews;
    }

    public void setReviews(ReviewsDTO[] reviews) {
        this.reviews = reviews;
    }
}
