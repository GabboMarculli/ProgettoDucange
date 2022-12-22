package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import javafx.fxml.FXML;

import java.io.IOException;

public class AllProductsController {
    @FXML
    private void goToHome()
    {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
