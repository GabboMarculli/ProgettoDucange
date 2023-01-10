package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HomePageController {
    @FXML
    private Label Username;
    public void initialize() {
        Username.setText(Application.authenticatedUser.getUsername());
    }
    @FXML
    protected void logout() throws IOException {
        Application.authenticatedUser = null;
        goToLogin();
    }
    @FXML
    protected void goToLogin() throws IOException {
        Application.changeScene("LoginPage");
    }
    @FXML
    protected void goToProfile() throws IOException {
        Application.changeScene("ProfilePage");
    }
    @FXML
    protected void goToFridge() throws IOException {
        Application.changeScene("FridgePage");
    }

    @FXML
    protected void goToUsers() throws IOException {
        Application.changeScene("AllUsers");
    }

    @FXML
    protected void goToProducts() throws IOException {
        Application.changeScene("AllIngredient");
    }

    @FXML
    protected void goToRecipes() throws IOException {
        Application.changeScene("AllRecipes");
    }

    @FXML
    protected void goToFolloewdUser() throws IOException {
        Application.changeScene("FollowedUser");
    }
}
