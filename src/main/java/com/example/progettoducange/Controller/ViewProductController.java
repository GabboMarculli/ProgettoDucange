package com.example.progettoducange.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewProductController {
    @FXML
    private Label ProductName;
    @FXML
    private Label ProductCalories;
    @FXML
    private Label ProductProtein;
    @FXML
    private Label ProductFat;
    @FXML
    private Label ProductSat;

    @FXML
    protected void addToFridge()
    {
        String product = ProductName.getText();

        // incrementa di uno la quantità di quel prodotto nel frigo.
    }

    @FXML
    protected void removeFromFridge()
    {
        String product = ProductName.getText();

        // decrementa di uno quel prodotto nel frigo. Se quantità prodotto nel frigo è già 0, non fa niente.
    }
}
