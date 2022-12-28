package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.DTO.userDTO;
import com.example.progettoducange.model.ProductInFridge;
import com.example.progettoducange.model.RegisteredUser;
import com.example.progettoducange.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.progettoducange.DAO.userDAO.getListOfUser;
import static java.time.LocalDate.now;

public class AllUsersController {
    @FXML
    private TableView<userDTO> UserTable;
    @FXML
    public TableColumn<userDTO, String> UsernameColumn;
    @FXML
    public TableColumn<userDTO, String> CountryColumn;
    @FXML
    public TableColumn<userDTO, LocalDate> NumberOfRecipe;
    @FXML
    public TableColumn FollowButtonColumn;


    private ObservableList<userDTO> data = FXCollections.observableArrayList();

    public void initialize(){
        UsernameColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO,String>("name")
        );
        CountryColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO,String>("country")
        );
        NumberOfRecipe.setCellValueFactory(
                new PropertyValueFactory<userDTO, LocalDate>("id")
        );
        FollowButtonColumn.setCellValueFactory(
                new PropertyValueFactory<>(""));

        Callback<TableColumn<userDTO, String>, TableCell<userDTO, String>> cellFactory
                =   new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<userDTO, String> param) {
                        final TableCell<userDTO, String> cell = new TableCell<userDTO, String>() {

                            final Button btn = new Button("Follow");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        userDTO user = getTableView().getItems().get(getIndex());
                                        //userDAO.follow_a_user(Integer.parseInt(Application.authenticatedUser.id),user.getId());
                                        userDAO.follow_a_user(Application.authenticatedUser.id,user.getId());
                                        //System.out.println(Application.authenticatedUser.getUsername()+ " FOLLOWS " + user.getUsername());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        FollowButtonColumn.setCellFactory(cellFactory);
        UserTable.setItems(data);
        fillTable();
    }

    int called_times = 0;
    //fill the table with 20 user. By pressing the button the user will visualize
    // 20 user in plus appended at the end of the previous list of user
    public void fillTable(){
        int limit_views_user = 20;
        ArrayList<Document> users = getListOfUser(limit_views_user,called_times);
        for(Document us : users) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            userDTO newrow = new userDTO(Integer.parseInt(
                    us.get("id").toString()),
                    us.get("username").toString(),
                    us.get("password").toString(),
                    us.get("name").toString(),
                    us.get("surname").toString(),
                    LocalDate.parse(us.get("registrationdate").toString(),formatter),
                    us.get("country").toString());
            data.add(newrow);
        }
        called_times++;
    }

    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }
}

