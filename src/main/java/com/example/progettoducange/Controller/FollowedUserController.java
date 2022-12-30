package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import javafx.fxml.FXML;

import java.io.IOException;

public class FollowedUserController {

    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }
}
