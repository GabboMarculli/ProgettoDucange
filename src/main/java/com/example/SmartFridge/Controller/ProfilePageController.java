package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.DTO.userDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ProfilePageController {
    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #green; -fx-border-width: 2; -fx-border-radius: 5;";

    @FXML
    private Label Username;
    @FXML
    private TextField FirstName;
    @FXML
    private TextField LastName;
    @FXML
    private Label Email;
    @FXML
    private TextField Country;
    @FXML
    private Label Password;
    @FXML
    private TextField NewPassword;
    @FXML
    private Button ChangePasswordButton;
    @FXML
    private Button changeFieldButton;
    @FXML
    private Label invalidChangePassword;

    public void initialize()
    {
        Username.setText(Application.authenticatedUser.getUsername());
        FirstName.setText(Application.authenticatedUser.getFirstName());
        LastName.setText(Application.authenticatedUser.getLastName());
        Email.setText(Application.authenticatedUser.getEmail());
        Country.setText(Application.authenticatedUser.getCountry());
    }

    @FXML
    protected void onChangePasswordButtonClick()
    {

        String password;
        if(NewPassword.getText().isBlank()) password = Application.authenticatedUser.getPassword();
        else password =  NewPassword.getText();

            userDTO user_to_modify = new userDTO();
            user_to_modify.setPassword(password);
            user_to_modify.setName(FirstName.getText());
            user_to_modify.setCountry(Country.getText());
            user_to_modify.setSurname(LastName.getText());

            Application.authenticatedUser.setPassword(password);
            Application.authenticatedUser.setFirstName(FirstName.getText());
            Application.authenticatedUser.setLastName(Country.getText());
            Application.authenticatedUser.setCountry(LastName.getText());

            //UserDAO.changePassword(Application.authenticatedUser, NewPassword.getText());
            UserDAO.changeField(Application.authenticatedUser, user_to_modify);

            invalidChangePassword.setText("Fields changed");
            invalidChangePassword.setStyle(successMessage);
    }

    @FXML
    protected void onDeleteUserClick()
    {
        try{
            UserDAO.deleteUser(Application.authenticatedUser);
            GoToLoginPage();
        } catch (Exception error){
            System.out.println(error);
        }
    }

    @FXML
    protected void onGoToHomeClick() throws IOException {
        Application.changeScene("HomePage");
    }
    @FXML
    protected void GoToLoginPage() throws IOException {
        Application.changeScene("LoginPage");
    }

    @FXML
    protected void showMyRecipe() throws IOException {
        Application.changeScene("showMyRecipe");
    }
}
