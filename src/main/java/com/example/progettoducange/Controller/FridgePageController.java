package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.model.ProductInFridge;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.time.LocalDate.*;

public class FridgePageController {
    @FXML
    private TableView<ProductInFridge> FridgeTable;
    @FXML
    public TableColumn<ProductInFridge, String> ProductNameColumn;
    @FXML
    public TableColumn<ProductInFridge, Integer> ProductQuantityColumn;
    @FXML
    public TableColumn<ProductInFridge, Date> ProductExpireDateColumn;
    @FXML
    private Button IncrementButton;
    private ObservableList<ProductInFridge> data = FXCollections.observableArrayList();
    private boolean prova = true;
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
        );
        ProductName.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,String>("name1")
        );
        ProductQuantity.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Integer>("quantity1")
        );
        ProductExpireDate.setCellValueFactory(
                new PropertyValueFactory<ProductInFridge,Date>("date1")
        );

         */
        if(prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,String>("name")
            );
            ProductQuantityColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,Integer>("quantity")
            );
            ProductExpireDateColumn.setCellValueFactory(
                    new PropertyValueFactory<ProductInFridge,Date>("expireDate")
            );
            FridgeTable.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in frigo");
        }
        System.out.println("Inserimento dati in frigo");
        ProductInFridge newrow = new ProductInFridge("toninomerda",69, now());
        data.add(newrow);
        System.out.println(newrow.getName());
    }
}
