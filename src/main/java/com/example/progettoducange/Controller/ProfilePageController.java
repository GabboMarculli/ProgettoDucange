package com.example.progettoducange.Controller;

import com.example.progettoducange.DAO.userDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfilePageController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";

    @FXML
    private Label Username;
    @FXML
    private Label FirstName;
    @FXML
    private Label LastName;
    @FXML
    private Label Email;
    @FXML
    private Label Country;
    @FXML
    private Label Password;
    @FXML
    private TextField NewPassword;
    @FXML
    private Button ChangePasswordButton;
    @FXML
    private Label invalidChangePassword;

    @FXML
    protected void onChangePasswordButtonClick()
    {
        if(NewPassword.getText().isBlank()){
            invalidChangePassword.setText("The Login fields are required!");
            invalidChangePassword.setStyle(errorMessage);
            invalidChangePassword.setText("");
            NewPassword.setStyle(errorStyle);
        } else {
            // DA DOVE LO PRENDO L'UTENTE ?????
            // userDAO.changePassword( , NewPassword.getText());

            invalidChangePassword.setText("Passoword changed.");
            invalidChangePassword.setStyle(successMessage);
            invalidChangePassword.setText("");
            NewPassword.setStyle(successStyle);
        }
    }
}
