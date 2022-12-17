package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.model.ProductInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class FridgePageController {
    @FXML
    private TableView<ProductInFridge> FridgeTable;
    @FXML
    private TableColumn<ProductInFridge, String> ProductName;
    @FXML
    private TableColumn<ProductInFridge, Integer> ProductQuantity;
    @FXML
    private TableColumn<ProductInFridge, Date> ProductExpireDate;

    @FXML
    private void goToHome()
    {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // #############################################################################################################
    // Non ho idea se funzioni
    // ###########################################################################################################

    public void fillTable() {
        /*
        // serve una lista del genere
        final ObservableList<ProductInFridge> data = FXCollections.observableArrayList(
                new ProductInFridge("Jacob", 2, LocalDate.now()),
                new ProductInFridge("Isabella", 4,LocalDate.now()),
                new ProductInFridge("Ethan", 3,LocalDate.now())
        ); */
        ProductName.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,String>("name")
        );
        ProductQuantity.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Integer>("quantity")
        );
        ProductExpireDate.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Date>("date")
        );

        // FridgeTable.setItems(data);
    }
}
