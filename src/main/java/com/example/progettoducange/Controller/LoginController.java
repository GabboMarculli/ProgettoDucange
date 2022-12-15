package com.example.progettoducange.Controller;


import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginController {

    // Strings which hold css elements to easily re-use in the application
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordField;
    @FXML
    private TextField signUpUsernameTextField;
    @FXML
    private TextField signUpEmailTextField;
    @FXML
    private TextField signUpPasswordField;
    @FXML
    private TextField signUpRepeatPasswordField;

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onLoginButtonClick() {
        if (loginUsernameTextField.getText().isBlank() || loginPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("The Login fields are required!");
            invalidLoginCredentials.setStyle(errorMessage);
            invalidSignupCredentials.setText("");

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
            } else if (loginPasswordField.getText().isBlank()) {
                loginPasswordField.setStyle(errorStyle);
            }
        } else {
                if(userDAO.checkPassword(loginUsernameTextField.getText(), loginPasswordField.getText())) {
                    invalidLoginCredentials.setText("Login Successful!");
                    invalidLoginCredentials.setStyle(successMessage);
                    loginUsernameTextField.setStyle(successStyle);
                    loginPasswordField.setStyle(successStyle);
                    invalidSignupCredentials.setText("");
                } else {
                    invalidSignupCredentials.setText("Password is wrong");
                    invalidSignupCredentials.setStyle(errorMessage);
                    signUpPasswordField.setStyle(errorStyle);
                    signUpRepeatPasswordField.setStyle(errorStyle);
                    invalidLoginCredentials.setText("");
                }
        }
    }

    @FXML
    protected void onSignUpButtonClick() {

        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpUsernameTextField.getText().equals("adimn") ||
                signUpPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().isBlank()) {
            invalidSignupCredentials.setText("Please fill in all fields!");
            invalidSignupCredentials.setStyle(errorMessage);
            invalidLoginCredentials.setText("");

            if (signUpUsernameTextField.getText().isBlank()) {
                signUpUsernameTextField.setStyle(errorStyle);
            } else if (signUpEmailTextField.getText().isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
            } else if (signUpPasswordField.getText().isBlank()) {
                signUpPasswordField.setStyle(errorStyle);
            } else if (signUpRepeatPasswordField.getText().isBlank()) {
                signUpRepeatPasswordField.setStyle(errorStyle);
            }
        } else if (!Utils.CheckEmail(signUpEmailTextField.getText())) {
            invalidSignupCredentials.setText("The email is in incorrect format!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpEmailTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        }/* else if (!Utils.CheckEmail(signUpPasswordField.getText())) {
            invalidSignupCredentials.setText("Password is too simple");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        }*/ else if (!signUpRepeatPasswordField.getText().equals(signUpPasswordField.getText())) {
            invalidSignupCredentials.setText("The Passwords don't match!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpPasswordField.setStyle(errorStyle);
            signUpRepeatPasswordField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        } else if (Objects.equals(userDAO.findUser(signUpUsernameTextField.getText()), signUpUsernameTextField.getText())) {
            invalidSignupCredentials.setText("Username already exists!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpUsernameTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        } else {
            invalidSignupCredentials.setText("You are set!");
            invalidSignupCredentials.setStyle(successMessage);
            signUpUsernameTextField.setStyle(successStyle);
            signUpEmailTextField.setStyle(successStyle);
            signUpPasswordField.setStyle(successStyle);
            signUpRepeatPasswordField.setStyle(successStyle);
            invalidLoginCredentials.setText("");

            RegisteredUser user = new RegisteredUser(0, signUpUsernameTextField.getText(),signUpPasswordField.getText(),signUpEmailTextField.getText());
            userDAO.signup(user);
        }
    }
}