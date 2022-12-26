package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DTO.productDTO;
import com.example.progettoducange.DTO.userDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.progettoducange.DAO.ProductDAO.getListOfProduct;
import static com.example.progettoducange.DAO.userDAO.getListOfUser;

public class AllProductsController {
    @FXML
    public TableView<productDTO> AllProductsTable;
    @FXML
    public TableColumn<productDTO, String> ProductNameColumn;
    @FXML
    public TableColumn<productDTO, Integer> QuantityInMyFridge;

    public boolean prova = true;

    private ObservableList<productDTO> data = FXCollections.observableArrayList();

    public void initialize()
    {
        fillTable();
    }

    public void fillTable()
    {
        if(prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<productDTO,String>("name")
            );
            QuantityInMyFridge.setCellValueFactory(
                    new PropertyValueFactory<productDTO,Integer>("quantity")
            );

            AllProductsTable.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in Product");
        }

        ArrayList<Document> products = getListOfProduct(20);

        // ########################################################################################################
        // Per la quantità nel frigo, bisogna ogni volta scorrere il db per vedere, di ogni prodotto, quanti ne ho nel frigo?
        // Oppure magari, quando un utente fa login, si salva in locale il frigo con le rispettive quantità? In questo secondo caso, è un KVDB?
        // ########################################################################################################

        for(Document us : products) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            productDTO newrow = new productDTO(us.get("_id").toString(),
                    us.get("food").toString(),
                    0);
            data.add(newrow);
        }
    }

    @FXML
    private void goToHome()
    {
        try {
            Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
