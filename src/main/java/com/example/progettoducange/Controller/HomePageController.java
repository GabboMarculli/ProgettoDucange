package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomePageController {
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
        Application.changeScene("AllProducts");
    }

    @FXML
    protected void goToRecipes() throws IOException {
        Application.changeScene("AllRecipes");
    }
    /*
    @FXML
    protected void goToSavedRecipes() throws IOException {
        Application.changeScene("SavedRecipes");
    }
     */

}
