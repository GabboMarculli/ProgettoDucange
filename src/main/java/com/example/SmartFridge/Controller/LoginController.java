package com.example.SmartFridge.Controller;


import com.example.SmartFridge.Application;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

// ######################################################################################################################
// DA FARE:
// - RISOLVERE BUG ESTETICI (SPESSO LE CASELLE DI INPUT SI COLORANO DI ROSSO A CAS0, E LA SCRITTA "LOGIN OK" OPPURE "LOGIN
//   ERRATO" VIENE DOV'E' IL SIGNUP IN BASSO
// - IMPEDIRE CHE UN UTENTE POSSA REGISTRARSI CON L'EMAIL DI UN UTENTE GIA' REGISTRATO
// - METTERE IL CHECK PASSWORD (?) ADESSO E' COMMENTATO, METTERLO IMPLICA DOVER MODIFICARE TUTTE LE PASSWORD NEL DB RENDENDOLE SICURE
// - METTERE UN LIMITE MINIMO E MASSIMO ALLA LUNGHEZZA DEI NOMI (DI NUOVO, FARLO IMPLICA DOVER MODIFICARE I NOMI ERRATI NEL DB)
// #######################################################################################################################

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
    @FXML
    private TextField signUpNameTextField;
    @FXML
    private TextField signUpSurnameTextField;
    @FXML
    private TextField signUpCountryTextField;

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
                if(UserDAO.checkPassword(loginUsernameTextField.getText(), loginPasswordField.getText())) {
                    invalidLoginCredentials.setText("Login Successful!");
                    invalidLoginCredentials.setStyle(successMessage);
                    loginUsernameTextField.setStyle(successStyle);
                    loginPasswordField.setStyle(successStyle);
                    invalidSignupCredentials.setText("");

                    try {
                        String[] credentials = UserDAO.getUser(loginUsernameTextField.getText());

                        String id = credentials[0];
                        String username = credentials[1];

                        if(username.equals("admin"))
                        {
                            Application.authenticatedUser = new RegisteredUser(id, username);
                            goToAdminPage();

                        } else {
                            String country = credentials[2];
                            String firstName = credentials[3];
                            String lastName = credentials[4];
                            String email = credentials[5];
                            String password = credentials[6];

                            Application.authenticatedUser = new RegisteredUser(id, username, firstName, lastName, country, email);
                            Application.authenticatedUser.setPassword(password);
                            goToHomePage();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    invalidSignupCredentials.setText("Password is wrong");
                    invalidSignupCredentials.setStyle(errorMessage);
                    loginPasswordField.setStyle(errorStyle);
                    invalidLoginCredentials.setText("");
                }
          }
    }

    protected void goToHomePage() throws IOException {
        Application.changeScene("HomePage");
    }

    protected void goToAdminPage() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

    @FXML
    protected void onSignUpButtonClick() throws IOException {
        clearField();
        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpUsernameTextField.getText().equals("admin") ||
                signUpPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16 ||

                signUpPasswordField.getText().length() > 16 || signUpRepeatPasswordField.getText().length()> 16 || signUpNameTextField.getText().isBlank() ||
                signUpSurnameTextField.getText().isBlank() || signUpCountryTextField.getText().isBlank() ) {
            invalidSignupCredentials.setText("Please fill in all fields! Max length is 16.");
            invalidSignupCredentials.setStyle(errorMessage);
            invalidLoginCredentials.setText("");

            if (signUpUsernameTextField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16) {
                signUpUsernameTextField.setStyle(errorStyle);
            } else if (signUpEmailTextField.getText().isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
            } else if (signUpPasswordField.getText().isBlank() || signUpPasswordField.getText().length() > 16) {
                signUpPasswordField.setStyle(errorStyle);
            } else if (signUpRepeatPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().length()> 16) {
                signUpRepeatPasswordField.setStyle(errorStyle);
            } else if (signUpNameTextField.getText().isBlank() || signUpNameTextField.getText().length()> 16) {
                signUpNameTextField.setStyle(errorStyle);
            } else if (signUpSurnameTextField.getText().isBlank() || signUpSurnameTextField.getText().length()> 16) {
                signUpSurnameTextField.setStyle(errorStyle);
            } else if (signUpCountryTextField.getText().isBlank() || signUpCountryTextField.getText().length()> 16) {
                signUpCountryTextField.setStyle(errorStyle);
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
        } else if (Objects.equals(UserDAO.findUser(signUpUsernameTextField.getText()), signUpUsernameTextField.getText())) {
            invalidSignupCredentials.setText("Username already exists!");
            invalidSignupCredentials.setStyle(errorMessage);
            signUpUsernameTextField.setStyle(errorStyle);
            invalidLoginCredentials.setText("");
        } else {
            ///////
            /////// MIRKO MODIFICARE QUI
            ///////
            RegisteredUser user = new RegisteredUser(
                    "",
                    signUpUsernameTextField.getText(),
                    signUpPasswordField.getText(),
                    signUpEmailTextField.getText(),
                    signUpNameTextField.getText(),
                    signUpSurnameTextField.getText(),
                    signUpCountryTextField.getText(),
                    LocalDate.now()
            );
            //registriamo lo user e otteniamo il suo id;
            String user_index = UserDAO.signup(user);

            if(user_index.equals("error")){
                System.out.println();
                invalidSignupCredentials.setText("Something went wrong! Retry");
                invalidSignupCredentials.setStyle(errorMessage);
                invalidLoginCredentials.setText("");

            }else{
                user.setId(user_index);
                Application.authenticatedUser = user;
                goToHomePage();
                /*
                invalidSignupCredentials.setText("You are set!");
                invalidSignupCredentials.setStyle(successMessage);
                signUpUsernameTextField.setStyle(successStyle);
                signUpEmailTextField.setStyle(successStyle);
                signUpPasswordField.setStyle(successStyle);
                signUpRepeatPasswordField.setStyle(successStyle);
                invalidLoginCredentials.setText("");
                */
            }
        }
    }

    private void clearField() {
        invalidLoginCredentials.setStyle(successMessage);
        loginUsernameTextField.setStyle(successStyle);
        loginPasswordField.setStyle(successStyle);
        signUpNameTextField.setStyle(successStyle);
        signUpPasswordField.setStyle(successStyle);
        signUpUsernameTextField.setStyle(successStyle);
        signUpEmailTextField.setStyle(successStyle);
        signUpSurnameTextField.setStyle(successStyle);
        signUpRepeatPasswordField.setStyle(successStyle);
        signUpCountryTextField.setStyle(successStyle);
        invalidSignupCredentials.setText("");
    }
}