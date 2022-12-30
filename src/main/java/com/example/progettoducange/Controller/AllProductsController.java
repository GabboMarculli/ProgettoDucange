package com.example.progettoducange.Controller;

import com.example.progettoducange.Application;
import com.example.progettoducange.DAO.*;
import com.example.progettoducange.DTO.*;
import com.example.progettoducange.Utils.Utils;
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
import java.time.LocalDate;
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
    @FXML
    public TextField SearchIngredient;
    @FXML
    public TextField Quantity;
    @FXML
    public TextField Expire_date;

    public boolean prova = true;

    private ObservableList<IngredientDTO> data = FXCollections.observableArrayList();

    public void initialize()
    {

        ProductNameColumn.setCellValueFactory(
                new PropertyValueFactory<IngredientDTO,String>("food")
        );
        QuantityInMyFridge.setCellValueFactory(
                new PropertyValueFactory<IngredientDTO,String>("measure")
        );
        AllProductsTable.setItems(data);

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
                                IngredientDAO.addToFridge(ingredientDTO);
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

    int called_times_products = 0;
    public void fillTable()
    {
        int limit_views_product = 20;
        ArrayList<IngredientDTO> ingredientList = IngredientDAO.getListOfIngredient(limit_views_product, called_times_products);

        if(ingredientList!= null)
        {
            data.addAll(ingredientList);
            called_times_products++;
        }
    }

    public boolean checkAddToFridge()
    {
        return true;
        // dà errore dio bestia
        //return (!Quantity.getText().isBlank() && Utils.isNumeric(Quantity.getText()) && !Expire_date.getText().isBlank());
    }

    public void printAddToFridge(String label, String _id, Integer row_index)
    {
        final Label lab = new Label(label);
        final TextField field = new TextField();
        field.setId(_id);

        GridPane.setRowIndex(lab, row_index);
        GridPane.setRowIndex(field, row_index);
        GridPane.setColumnIndex(field, 2);

        Right.getChildren().add(lab);
        Right.getChildren().add(field);
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

        printAddToFridge("Quantity: ", "Quantity", 10);
        printAddToFridge("Expire_date: ", "Expire_date", 11);

        final Button Submit_in_fridge = new Button("Add ");
        GridPane.setRowIndex(Submit_in_fridge,12);
        Right.getChildren().add(Submit_in_fridge);

        Submit_in_fridge.setOnAction(event -> {
            if(checkAddToFridge()){
                ProductInFridge p = new ProductInFridge(rowData.getFood(), Integer.parseInt(Quantity.getText()),
                            LocalDate.parse(Expire_date.getText()));
                ObservableList<ProductInFridge> p_list = null;
                p_list.add(p);
                FridgeDAO.updateFridge(p_list);
            }
        });
    }

    public void Search_for_ingredient()
    {
        String ingredientName = SearchIngredient.getText();
        if(!ingredientName.equals("")) {
            try {
                IngredientDTO searched_ingredients = IngredientDAO.getIngredient(ingredientName);
                if(searched_ingredients != null)
                {
                    data.clear();
                    data.add(searched_ingredients);
                    AllProductsTable.setItems(data);
                }
            } catch (Error e){
                System.out.println(e);
            }
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
