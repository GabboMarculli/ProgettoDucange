package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.model.ProductInFridge;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.progettoducange.DAO.userDAO.getListOfUser;
import static java.time.LocalDate.now;

public class AllUsersController {
    @FXML
    private TableView<RegisteredUser> Users;
    @FXML
    public TableColumn<RegisteredUser, String> UsernameColumn;
    @FXML
    public TableColumn<RegisteredUser, String> CountryColumn;
    @FXML
    public TableColumn<RegisteredUser, Integer> NumberOfRecipe;
    public boolean prova = true;
    private ObservableList<RegisteredUser> data = FXCollections.observableArrayList();

    public void fillTable()
    {
        if(prova) {
            UsernameColumn.setCellValueFactory(
                    new PropertyValueFactory<RegisteredUser,String>("Name")
            );
            CountryColumn.setCellValueFactory(
                    new PropertyValueFactory<RegisteredUser,String>("Country")
            );
            NumberOfRecipe.setCellValueFactory(
                    new PropertyValueFactory<RegisteredUser,Integer>("Number of Recipe")
            );
            Users.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in User");
        }

        System.out.println("Inserimento dati in frigo");
        ArrayList<Document> users = getListOfUser(20);

        RegisteredUser newrow = new RegisteredUser();
        data.add(newrow);
        System.out.println(newrow.getUsername());


    }

    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }
}

