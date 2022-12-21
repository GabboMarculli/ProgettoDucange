package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.model.ProductInFridge;
import com.example.progettoducange.model.RegisteredUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
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

    private ObservableList<RegisteredUser> data = FXCollections.observableArrayList();

    public void fillTable()
    {
        UsernameColumn.setCellValueFactory(
                new PropertyValueFactory<RegisteredUser,String>("Username")
        );
        CountryColumn.setCellValueFactory(
                new PropertyValueFactory<RegisteredUser,String>("Country")
        );
        NumberOfRecipe.setCellValueFactory(
                new PropertyValueFactory<RegisteredUser,Integer>("NumberOfRecipe")
        );
        Users.setItems(data);

        // ********************************************************************************************
        // COME SI FA??????
        // ********************************************************************************************

        List<String> users = getListOfUser(20);
        System.out.println(users);
        for(Integer i = 0; i < 20; i++)
        {
            data.add((RegisteredUser) users);
        }
    }

    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }
}

