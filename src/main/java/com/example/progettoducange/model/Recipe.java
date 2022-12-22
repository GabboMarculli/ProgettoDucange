package com.example.progettoducange.model;

import java.util.List;

public class Recipe {
    private Integer id;
    private User author;
    private Integer like;
    private List<Product> ingredients;
    private String title;
    private List<Review> reviews;
    private String directions;

    public Recipe(User author, List<Product> ingredients, String title, String directions, Integer id)
    {
        this.id = id;
        this.author= author;
        this.ingredients = ingredients;
        this.title = title;
        this.directions = directions;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Integer getLike() {
        return like;
    }

    public List<Product> getIngredients() {
        return ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public void setIngredients(List<Product> ingredients) {
        this.ingredients = ingredients;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", author=" + author +
                ", like=" + like +
                ", ingredients=" + ingredients +
                ", title='" + title + '\'' +
                ", reviews=" + reviews +
                ", directions='" + directions + '\'' +
                '}';
    }
}
