package com.example.SmartFridge.Controller;


import com.example.SmartFridge.Application;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.model.RegisteredUser;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    String onOver = "-fx-background-color: #34cfeb;";
    String exitOver = "-fx-background-color: #24a0ed; -fx-text-fill: WHITE;";
    String onClick = "-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5";
    String onReleased = "-fx-background-color: #24a0ed; -fx-text-fill: WHITE;";

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
    private ComboBox signUpCountryTextField;
    @FXML
    private ListView listview;

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        loginUsernameTextField.clear();
        loginUsernameTextField.setStyle(successStyle);
        loginPasswordField.clear();
        loginPasswordField.setStyle(successStyle);
        invalidLoginCredentials.setText("");
    }
    @FXML
    protected void onLeaveButtonClick() {
        MongoDbDriver.close();
        Neo4jDriver.close();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onCancelButtonClick2() {
        signUpCountryTextField.setValue("");
        signUpCountryTextField.setStyle(successStyle);
        signUpEmailTextField.clear();
        signUpEmailTextField.setStyle(successStyle);
        signUpNameTextField.clear();
        signUpNameTextField.setStyle(successStyle);
        signUpUsernameTextField.clear();
        signUpUsernameTextField.setStyle(successStyle);
        signUpPasswordField.clear();
        signUpPasswordField.setStyle(successStyle);
        signUpRepeatPasswordField.clear();
        signUpRepeatPasswordField.setStyle(successStyle);
        signUpSurnameTextField.setStyle(successStyle);
        signUpSurnameTextField.clear();
        invalidSignupCredentials.setText("");
    }
    public void setOver(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getTarget()).setStyle(onOver);
        Application.setMousePointer();
    }

    public void unsetOver(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getTarget()).setStyle(exitOver);
        Application.unSetMousePointer();
    }

    @FXML
    protected void onLoginButtonClick() {
        clearloginField();
        if (loginUsernameTextField.getText().isBlank() || loginPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("All Login fields are required!");
            invalidLoginCredentials.setStyle(errorMessage);
            invalidSignupCredentials.setText("");

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
                loginUsernameTextField.requestFocus();
            } else if (loginPasswordField.getText().isBlank()) {
                loginPasswordField.setStyle(errorStyle);
                loginPasswordField.requestFocus();
            }
        } else {

            if (UserDAO.checkPassword(loginUsernameTextField.getText(), loginPasswordField.getText())) {
                invalidLoginCredentials.setText("Login Successful!");
                invalidLoginCredentials.setStyle(successMessage);
                loginUsernameTextField.setStyle(successStyle);
                loginPasswordField.setStyle(successStyle);
                invalidSignupCredentials.setText("");

                try {
                    String[] credentials = UserDAO.getUser(loginUsernameTextField.getText());

                    String id = credentials[0];
                    String username = credentials[1];

                    if (username.equals("admin")) {
                        Application.authenticatedUser = new RegisteredUser(id, username);
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
                invalidLoginCredentials.setText("Password is wrong or user is not existing.");
                loginPasswordField.setStyle(errorStyle);
                loginUsernameTextField.setStyle(errorStyle);
                invalidLoginCredentials.setStyle(errorMessage);
                invalidSignupCredentials.setText("");
            }
        }
    }

    protected void goToHomePage() throws IOException {
        Application.changeScene("HomePage");
    }

    protected void goToAdminPage() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

    public void initialize(){
        ObservableList<String> countries = MongoDbDriver.getCountry();
        countries.add("");
        signUpCountryTextField.setItems(countries);
        ObservableList<>
    }

    @FXML
    protected void onSignUpButtonClick() throws IOException {
        clearsignupField();
        if (signUpUsernameTextField.getText().isBlank() || signUpEmailTextField.getText().isBlank() || signUpUsernameTextField.getText().equals("admin") ||
                signUpPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16 ||
                signUpCountryTextField.getValue() == null || signUpCountryTextField.getValue().toString().equals("") ||
                signUpPasswordField.getText().length() > 16 || signUpRepeatPasswordField.getText().length() > 16 || signUpNameTextField.getText().isBlank() ||
                signUpSurnameTextField.getText().isBlank() || signUpCountryTextField.getSelectionModel().isEmpty() ||
                signUpPasswordField.getText().equals(signUpUsernameTextField.getText())) {
            invalidSignupCredentials.setText("Please fill in all fields! Max length is 16.");
            invalidSignupCredentials.setStyle(errorMessage);
            invalidLoginCredentials.setText("");

            if (signUpUsernameTextField.getText().isBlank() || signUpUsernameTextField.getText().length() > 16) {
                signUpUsernameTextField.setStyle(errorStyle);
                signUpUsernameTextField.requestFocus();
            } else if (signUpNameTextField.getText().isBlank() || signUpNameTextField.getText().length() > 16) {
                signUpNameTextField.setStyle(errorStyle);
                signUpNameTextField.requestFocus();
            } else if (signUpSurnameTextField.getText().isBlank() || signUpSurnameTextField.getText().length() > 16) {
                signUpSurnameTextField.setStyle(errorStyle);
                signUpSurnameTextField.requestFocus();
            } else if (signUpEmailTextField.getText().isBlank()) {
                signUpEmailTextField.setStyle(errorStyle);
                signUpEmailTextField.requestFocus();
            } else if (signUpPasswordField.getText().isBlank() || signUpPasswordField.getText().length() > 16) {
                signUpPasswordField.setStyle(errorStyle);
                signUpPasswordField.requestFocus();
            } else if (signUpRepeatPasswordField.getText().isBlank() || signUpRepeatPasswordField.getText().length() > 16) {
                signUpRepeatPasswordField.setStyle(errorStyle);
                signUpRepeatPasswordField.requestFocus();
            } else if (signUpCountryTextField.getValue() == null || signUpCountryTextField.getValue().toString().equals("") ) {
                signUpCountryTextField.setStyle(errorStyle);
                invalidSignupCredentials.setText("Please, select a country from the list.");
                signUpCountryTextField.requestFocus();
            } else if (signUpPasswordField.getText().equals(signUpUsernameTextField.getText())) {
                signUpPasswordField.setStyle(errorStyle);
                signUpPasswordField.requestFocus();
                signUpRepeatPasswordField.setStyle(errorStyle);
                invalidSignupCredentials.setText("Password and Username should not match!");
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
                    signUpCountryTextField.getValue().toString(),
                    LocalDate.now()
            );
            //registriamo lo user e otteniamo il suo id;
            String user_index = UserDAO.signup(user);


            if(user_index.equals("error")){

                System.out.println();
                invalidSignupCredentials.setText("Something went wrong! Retry");
                invalidSignupCredentials.setStyle(errorMessage);
                invalidLoginCredentials.setText("");

            } else {
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

    private void clearsignupField() {
        signUpUsernameTextField.setStyle(successStyle);
        signUpSurnameTextField.setStyle(successStyle);
        signUpPasswordField.setStyle(successStyle);
        signUpRepeatPasswordField.setStyle(successStyle);
        signUpNameTextField.setStyle(successStyle);
        signUpEmailTextField.setStyle(successStyle);
        signUpCountryTextField.setStyle(successStyle);
    }

    private void clearloginField(){
        loginPasswordField.setStyle(successStyle);
        loginUsernameTextField.setStyle(successStyle);
    }
    public void setClick(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle(onClick);
        Application.setMousePointer();
    }

    public void unsetClick(MouseEvent mouseEvent) {
        ((Button) mouseEvent.getSource()).setStyle(onReleased);
        Application.setMousePointer();
    }

}