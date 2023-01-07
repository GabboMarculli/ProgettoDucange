package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.model.RegisteredUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class HomePageAdminController {
    @FXML
    private Label Username;
    @FXML
    private Button Products;
    @FXML
    private Button Recipes;
    @FXML
    private Button Users;

    public void initialize() {
        Username.setText("Admin");

        Application.authenticatedUser = new RegisteredUser(-1, "admin", null, null, null);
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
    protected void goToProducts() throws IOException {
        Application.changeScene("AllProducts");
    }

    @FXML
    protected void goToRecipes() throws IOException {
        Application.changeScene("AllRecipes");
    }

    @FXML
    protected void goToUsers() throws IOException {
        Application.changeScene("AllUsers");
    }
}
