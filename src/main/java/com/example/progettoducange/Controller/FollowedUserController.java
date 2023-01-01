package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.userDAO;
import com.example.progettoducange.DTO.userDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.progettoducange.DAO.userDAO.getListOfFollowedUser;
import static com.example.progettoducange.DAO.userDAO.getListOfUser;

public class FollowedUserController {
    @FXML
    private TableView<userDTO> UserTable;
    @FXML
    public TableColumn<userDTO, String> UsernameColumn;
    @FXML
    public TableColumn<userDTO, String> CountryColumn;

    @FXML
    public TableColumn UnFollowButtonColumn;
    @FXML
    public TextField SearchUser;

    private ObservableList<userDTO> data = FXCollections.observableArrayList();

    public void initialize() {
        UsernameColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("username")
        );
        CountryColumn.setCellValueFactory(
                new PropertyValueFactory<userDTO, String>("country")
        );
        UnFollowButtonColumn.setCellValueFactory(
                new PropertyValueFactory<>(""));

        Callback<TableColumn<userDTO, String>, TableCell<userDTO, String>> cellFactory
                = new Callback<>() {
            @Override
            public TableCell call(final TableColumn<userDTO, String> param) {
                final TableCell<userDTO, String> cell = new TableCell<userDTO, String>() {

                    final Button btn = new Button("Unfollow");

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
                                userDAO.unfollowUser(Application.authenticatedUser.id, user.getId());
                                btn.setDisable(true);
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

        UnFollowButtonColumn.setCellFactory(cellFactory);
        UserTable.setItems(data);
        fillTable();
    }

    int called_times = 0;

    //fill the table with 20 followed user. By pressing the button the user will visualize
    // 20 user in plus appended at the end of the previous list of user
    public void fillTable() {
        int limit_views_user = 20;
        List<userDTO> users = getListOfFollowedUser(limit_views_user, called_times);
        if(users != null) {
            for (userDTO us : users) {
                data.add(us);
            }
            called_times++;
        }
    }

    /* carina la roba del formatter, cancellateliamola alla fine sennò è uno sbattimento costante con queste LocalDate
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            userDTO newrow = new userDTO(Integer.parseInt(
                    us.get("id").toString()),
                    us.get("username").toString(),
                    us.get("password").toString(),
                    us.get("name").toString(),
                    us.get("surname").toString(),
                    LocalDate.parse(us.get("registrationdate").toString(), formatter),
                    us.get("country").toString());
            data.add(newrow);
    **/


    public void Search_for_followed_user() {
        String username = SearchUser.getText();
        if(!username.equals("")) {
            try {
                List<userDTO> searched_user = userDAO.Search_for_followed_user(username);

                if(searched_user.size()!=0)
                {
                    data.clear();
                    data.add(searched_user.get(0));
                    UserTable.setItems(data);
                }
            } catch (Error e){
                System.out.println(e);
            }
        }
        else fillTable();
    }

    @FXML
    protected void goToHome() throws IOException {
        Application.changeScene("HomePage");
    }
}

