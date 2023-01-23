package com.example.SmartFridge.Controller;


import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.RecipeDao;
import com.example.SmartFridge.DTO.RecipeDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.example.SmartFridge.Utils.Utils;
import com.example.SmartFridge.DAO.UserDAO;
import com.example.SmartFridge.model.Recipe;
import com.example.SmartFridge.model.RegisteredUser;
import com.mongodb.client.MongoCollection;
import javafx.animation.FillTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
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
    @FXML
    private Text recipetext;
    @FXML
    private Text authortext;
    @FXML
    private TextArea ingredienttext;
    @FXML
    private TextArea preparationtext;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Text pagenumber;
    ObservableList<RecipeDTO> data = FXCollections.observableArrayList();
    private int page = 0;

    @FXML
    protected void onNextClick(){
        page++;
        previousButton.setDisable(false);
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        data.clear();
        for(RecipeDTO r : recipes)
            data.add(r);
        listview.getSelectionModel().select(0);
        pagenumber.setText(Integer.toString(page));
        showRecipe();
    }

    @FXML
    protected void onPreviousClick(){
        if(page <= 0){
            return;
        }
        page--;
        pagenumber.setText(Integer.toString(page));
        if(page == 0){
            previousButton.setDisable(true);
        }
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(20,page);
        data.clear();
        for(RecipeDTO r : recipes)
            data.add(r);
        listview.getSelectionModel().select(0);
        showRecipe();
    }


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
        Button b = ((Button) mouseEvent.getTarget());
        b.setStyle(onOver);
        b.setCursor(Cursor.HAND);
    }

    public void unsetOver(MouseEvent mouseEvent) {
        Button b = ((Button) mouseEvent.getTarget());
        b.setStyle(exitOver);
        b.setCursor(Cursor.DEFAULT);
        //Application.unSetMousePointer();
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
        Application.changeScene("MainTable");
    }

    protected void goToAdminPage() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

    public void initialize() throws IOException {
        ingredienttext.setWrapText(true);
        preparationtext.setWrapText(true);
        signUpCountryTextField.setItems(getCountryData());
        previousButton.setDisable(true);
/*
        FileWriter f = new FileWriter("country.txt");
        PrintWriter pw = new PrintWriter(f);
        pw.println("ObservableList<RecipeDTO> countries = FXCollections.observableArrayList();");
        for(String s : countries)
            pw.println("countries.add(\""+s+"\");");
        pw.close();
*/
        int limit_views_recipe = 20;
        List<RecipeDTO> recipes = RecipeDao.getRecipeLoginpage(limit_views_recipe,20);
        listview.setCellFactory(new Callback<ListView<RecipeDTO>, ListCell<RecipeDTO>>() {
            @Override
            public ListCell call(ListView<RecipeDTO> listView) {
                final ListCell<RecipeDTO> cell = new ListCell<RecipeDTO>(){

                    @Override
                    public void updateItem(RecipeDTO item, boolean empty){
                        super.updateItem(item,empty);
                        if(item != null) {
                            setText(item.getName());
                            }
                    }
                };
                return cell;
            }
        });

        for(RecipeDTO us : recipes) {
            System.out.println(us.getPreparationTime());
            data.add(us);
        }
        listview.setItems(data);
        listview.getSelectionModel().select(0);
        showRecipe();
    }

    @FXML
    protected void showRecipe(){
        int index = listview.getSelectionModel().getSelectedIndex();
        RecipeDTO recipe = (RecipeDTO) listview.getItems().get(index);

        authortext.setText(recipe.getAuthor());
        ingredienttext.setText(recipe.getIngrients());
        preparationtext.setText(recipe.getDirection());

        recipetext.setText(recipe.getName());
        System.out.println(recipe.getName()+" "+recipe.getTotalTime()+" time:"+recipe.getAuthor());
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
        //((Button) mouseEvent.getSource()).setStyle(onClick);
        //Application.setMousePointer();

        Button b = ((Button) mouseEvent.getSource());
        b.setStyle(onClick);
        b.setCursor(Cursor.HAND);
    }

    public void unsetClick(MouseEvent mouseEvent) {
        //((Button) mouseEvent.getSource()).setStyle(onReleased);
        //Application.setMousePointer();

        Button b = ((Button) mouseEvent.getSource());
        b.setStyle(onReleased);
    }
    private ObservableList getCountryData(){
        ObservableList<String> countries = FXCollections.observableArrayList();
        countries.add("Afghanistan");
        countries.add("Albania");
        countries.add("Algeria");
        countries.add("Andorra");
        countries.add("Angola");
        countries.add("Antigua & Deps");
        countries.add("Argentina");
        countries.add("Armenia");
        countries.add("Australia");
        countries.add("Austria");
        countries.add("Azerbaijan");
        countries.add("Bahamas");
        countries.add("Bahrain");
        countries.add("Bangladesh");
        countries.add("Barbados");
        countries.add("Belarus");
        countries.add("Belgium");
        countries.add("Belize");
        countries.add("Benin");
        countries.add("Bhutan");
        countries.add("Bolivia");
        countries.add("Bosnia Herzegovina");
        countries.add("Botswana");
        countries.add("Brazil");
        countries.add("Brunei");
        countries.add("Bulgaria");
        countries.add("Burkina");
        countries.add("Burundi");
        countries.add("Cambodia");
        countries.add("Cameroon");
        countries.add("Canada");
        countries.add("Cape Verde");
        countries.add("Central African Rep");
        countries.add("Chad");
        countries.add("Chile");
        countries.add("China");
        countries.add("Colombia");
        countries.add("Comoros");
        countries.add("Congo");
        countries.add("Congo {Democratic Rep}");
        countries.add("Costa Rica");
        countries.add("Croatia");
        countries.add("Cuba");
        countries.add("Cyprus");
        countries.add("Czech Republic");
        countries.add("Denmark");
        countries.add("Djibouti");
        countries.add("Dominica");
        countries.add("Dominican Republic");
        countries.add("East Timor");
        countries.add("Ecuador");
        countries.add("Egypt");
        countries.add("El Salvador");
        countries.add("Equatorial Guinea");
        countries.add("Eritrea");
        countries.add("Estonia");
        countries.add("Ethiopia");
        countries.add("Fiji");
        countries.add("Finland");
        countries.add("France");
        countries.add("Gabon");
        countries.add("Gambia");
        countries.add("Georgia");
        countries.add("Germany");
        countries.add("Ghana");
        countries.add("Greece");
        countries.add("Grenada");
        countries.add("Guatemala");
        countries.add("Guinea");
        countries.add("Guinea-Bissau");
        countries.add("Guyana");
        countries.add("Haiti");
        countries.add("Honduras");
        countries.add("Hungary");
        countries.add("Iceland");
        countries.add("India");
        countries.add("Indonesia");
        countries.add("Iran");
        countries.add("Iraq");
        countries.add("Ireland {Republic}");
        countries.add("Israel");
        countries.add("Italy");
        countries.add("Ivory Coast");
        countries.add("Jamaica");
        countries.add("Japan");
        countries.add("Jordan");
        countries.add("Kazakhstan");
        countries.add("Kenya");
        countries.add("Kiribati");
        countries.add("Korea North");
        countries.add("Korea South");
        countries.add("Kosovo");
        countries.add("Kuwait");
        countries.add("Kyrgyzstan");
        countries.add("Laos");
        countries.add("Latvia");
        countries.add("Lebanon");
        countries.add("Lesotho");
        countries.add("Liberia");
        countries.add("Libya");
        countries.add("Liechtenstein");
        countries.add("Lithuania");
        countries.add("Luxembourg");
        countries.add("Macedonia");
        countries.add("Madagascar");
        countries.add("Malawi");
        countries.add("Malaysia");
        countries.add("Maldives");
        countries.add("Mali");
        countries.add("Malta");
        countries.add("Marshall Islands");
        countries.add("Mauritania");
        countries.add("Mauritius");
        countries.add("Mexico");
        countries.add("Micronesia");
        countries.add("Moldova");
        countries.add("Monaco");
        countries.add("Mongolia");
        countries.add("Montenegro");
        countries.add("Morocco");
        countries.add("Mozambique");
        countries.add("Myanmar, {Burma}");
        countries.add("Namibia");
        countries.add("Nauru");
        countries.add("Nepal");
        countries.add("Netherlands");
        countries.add("New Zealand");
        countries.add("Nicaragua");
        countries.add("Niger");
        countries.add("Nigeria");
        countries.add("Norway");
        countries.add("Oman");
        countries.add("Pakistan");
        countries.add("Palau");
        countries.add("Panama");
        countries.add("Papua New Guinea");
        countries.add("Paraguay");
        countries.add("Peru");
        countries.add("Philippines");
        countries.add("Poland");
        countries.add("Portugal");
        countries.add("Qatar");
        countries.add("Romania");
        countries.add("Russian Federation");
        countries.add("Rwanda");
        countries.add("Saint Vincent & the Grenadines");
        countries.add("Samoa");
        countries.add("San Marino");
        countries.add("Sao Tome & Principe");
        countries.add("Saudi Arabia");
        countries.add("Senegal");
        countries.add("Serbia");
        countries.add("Seychelles");
        countries.add("Sierra Leone");
        countries.add("Singapore");
        countries.add("Slovakia");
        countries.add("Slovenia");
        countries.add("Solomon Islands");
        countries.add("Somalia");
        countries.add("South Africa");
        countries.add("South Sudan");
        countries.add("Spain");
        countries.add("Sri Lanka");
        countries.add("St Kitts & Nevis");
        countries.add("St Lucia");
        countries.add("Sudan");
        countries.add("Suriname");
        countries.add("Swaziland");
        countries.add("Sweden");
        countries.add("Switzerland");
        countries.add("Syria");
        countries.add("Taiwan");
        countries.add("Tajikistan");
        countries.add("Tanzania");
        countries.add("Thailand");
        countries.add("Togo");
        countries.add("Tonga");
        countries.add("Trinidad & Tobago");
        countries.add("Tunisia");
        countries.add("Turkey");
        countries.add("Turkmenistan");
        countries.add("Tuvalu");
        countries.add("Uganda");
        countries.add("Ukraine");
        countries.add("United Arab Emirates");
        countries.add("United Kingdom");
        countries.add("United States");
        countries.add("Uruguay");
        countries.add("Uzbekistan");
        countries.add("Vanuatu");
        countries.add("Vatican City");
        countries.add("Venezuela");
        countries.add("Vietnam");
        countries.add("Yemen");
        countries.add("Zambia");
        countries.add("Zimbabwe");
        countries.add("");
        return countries;
    }
}