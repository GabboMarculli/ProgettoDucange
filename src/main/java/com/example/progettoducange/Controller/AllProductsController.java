package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.*;
import com.example.progettoducange.model.ProductInFridge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    @FXML
    public GridPane Right;

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

        AllProductsTable.setRowFactory( tv -> {
            TableRow<IngredientDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    IngredientDTO rowData = row.getItem();
                    viewProductDetail(rowData);
                }
            });
            return row ;
        });

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

    public void printProduct(String name, String text, Integer index)
    {
        final Label label = new Label(name);

        final Label labelText = new Label(text);

        if(index!= 0){
            GridPane.setRowIndex(labelText, index);
            GridPane.setRowIndex(label, index);
        }

        GridPane.setColumnIndex(labelText, 2);

        Right.getChildren().add(label);
        Right.getChildren().add(labelText);
    }

    @FXML
    public void viewProductDetail(IngredientDTO rowData)
    {
        Right.getChildren().clear();
        printProduct("Name: ",rowData.getFood(),0);
        printProduct("Measure: ", rowData.getMeasure(), 1);
        printProduct("Grams: ", rowData.getGrams(), 2);
        printProduct("Calories: ", rowData.getCalories(), 3);
        printProduct("Protein: ", rowData.getProtein(), 4);
        printProduct("Fat: ", rowData.getFat(), 5);
        printProduct("Fiber: ", rowData.getFiber(), 6);
        printProduct("Carbs", rowData.getCarbs(), 7);
        printProduct("Category: ", rowData.getCategory(), 8);
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
