package com.example.progettoducange.Controller;


import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.userDTO;
import com.example.progettoducange.Utils.Utils;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
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
    private DatePicker signUpDateDatePicker;

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

                    try {
                        String[] credentials = userDAO.getUser(loginUsernameTextField.getText());

                        int id = Integer.parseInt(credentials[0]);
                        String username = credentials[1];

                        if(username.equals("admin"))
                        {
                            goToAdminPage();

                        } else {
                            String country = credentials[2];
                            String firstName = credentials[3];
                            String lastName = credentials[4];
                            String email = credentials[5];

                            Application.authenticatedUser = new RegisteredUser(id, username, firstName, lastName, country, email);
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
    protected void onSignUpButtonClick() {
        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpUsernameTextField.getText().equals("admin") ||
                signUpPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16 ||
                signUpDateDatePicker.getEditor().getText().isBlank() || signUpPasswordField.getText().equals(signUpUsernameTextField.getText()) ||
                signUpPasswordField.getText().length() > 16 || signUpRepeatPasswordField.getText().length()> 16) {
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
            }else if(signUpDateDatePicker.getEditor().getText().isBlank()){
                invalidSignupCredentials.setText("set date of birth");
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
            RegisteredUser user = new RegisteredUser(
                    0,
                    signUpUsernameTextField.getText(),
                    signUpPasswordField.getText(),
                    signUpEmailTextField.getText(),
                    LocalDate.now()
            );
            //registriamo lo user e otteniamo il suo id;
            int user_index = userDAO.signup(user);

            if(user_index == 0){
                System.out.println();
                invalidSignupCredentials.setText("Something went wrong! Retry");
                invalidSignupCredentials.setStyle(errorMessage);
                invalidLoginCredentials.setText("");

            }else{
                user.setId(user_index);
                invalidSignupCredentials.setText("You are set!");
                invalidSignupCredentials.setStyle(successMessage);
                signUpUsernameTextField.setStyle(successStyle);
                signUpEmailTextField.setStyle(successStyle);
                signUpPasswordField.setStyle(successStyle);
                signUpRepeatPasswordField.setStyle(successStyle);
                invalidLoginCredentials.setText("");
            }
        }
    }
}