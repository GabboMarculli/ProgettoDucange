package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomePageController {
    @FXML
    protected void goToProfile() throws IOException {
        Application.changeScene("ProfilePage");
    }
}
