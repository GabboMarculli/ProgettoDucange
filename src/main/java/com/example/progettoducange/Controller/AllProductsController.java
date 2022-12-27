package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.io.IOException;
import java.util.ArrayList;
public class AllProductsController {
    @FXML
    public TableView<IngredientDTO> AllProductsTable;
    @FXML
    public TableColumn<IngredientDTO, String> ProductNameColumn;
    @FXML
    public TableColumn<IngredientDTO, String> QuantityInMyFridge;
    @FXML
    public TableColumn AddToFridge;

    public boolean prova = true;

    private ObservableList<IngredientDTO> data = FXCollections.observableArrayList();

    public void initialize()
    {

        Callback<TableColumn<IngredientDTO, String>, TableCell<IngredientDTO, String>> cellFactory
                =   new Callback<>() {
            @Override
            public TableCell call(final TableColumn<IngredientDTO, String> param) {
                final TableCell<IngredientDTO, String> cell = new TableCell<IngredientDTO, String>() {

                    final Button btn = new Button("View");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                IngredientDTO ingredientDTO = getTableView().getItems().get(getIndex());

                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        AddToFridge.setCellFactory(cellFactory);
        AllProductsTable.setItems(data);
        fillTable();
    }

    public void fillTable()
    {
        if(prova) {
            ProductNameColumn.setCellValueFactory(
                    new PropertyValueFactory<IngredientDTO,String>("food")
            );
            QuantityInMyFridge.setCellValueFactory(
                    new PropertyValueFactory<IngredientDTO,String>("measure")
            );

            AllProductsTable.setItems(data);

            prova = false;
            System.out.println("Inizializzazione dati in Product");
        }

        ArrayList<IngredientDTO> ingredientList = IngredientDAO.getListOfIngredient(20);

        // ########################################################################################################
        // Per la quantità nel frigo, bisogna ogni volta scorrere il db per vedere, di ogni prodotto, quanti ne ho nel frigo?
        // Oppure magari, quando un utente fa login, si salva in locale il frigo con le rispettive quantità? In questo secondo caso, è un KVDB?
        // ########################################################################################################

        for(IngredientDTO us : ingredientList) {
            data.add(us);
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
