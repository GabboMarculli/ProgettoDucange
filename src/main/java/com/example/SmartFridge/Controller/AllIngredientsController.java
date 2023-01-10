package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.*;
import com.example.SmartFridge.DTO.*;
import com.example.SmartFridge.Utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllIngredientsController {
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
    @FXML
    public AnchorPane my_anchor_pane;


    private ObservableList<IngredientDTO> data = FXCollections.observableArrayList();

    public void initialize()
    {

        ProductNameColumn.setCellValueFactory(
                new PropertyValueFactory<IngredientDTO,String>("food")
        );
        QuantityInMyFridge.setCellValueFactory(
                new PropertyValueFactory<IngredientDTO,String>("calories")
        );
        AllProductsTable.setItems(data);

        AllProductsTable.setItems(data);

        AllProductsTable.setRowFactory( tv -> {
            TableRow<IngredientDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    IngredientDTO rowData = row.getItem();

                    if(Application.authenticatedUser.getUsername().equals("admin"))
                        AddIngredientToFridgeController.row = row.getItem();

                    viewProductDetail(rowData);
                }
            });
            return row ;
        });
        fillTable();

        if(Application.authenticatedUser.getUsername().equals("admin")) {
            final Button Add_product = new Button("Add product ");
            Add_product.setLayoutX(Double.parseDouble("240"));
            Add_product.setLayoutY(Double.parseDouble("560"));
            my_anchor_pane.getChildren().add(Add_product);

            Add_product.setOnAction(event -> {
                goToAddProduct();
            });
        }
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

/*
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

        if(label.equals("quantity")){
            Quantity = field;
        }
        if(label.equals("Expire_date")){
            Expire_date = field;
        }

    }
 */

    public void printAddToFridge(String label, String _id, Integer row_index) {

        if (_id.equals("Quantity")) {
            final Label lab = new Label(label);
            Quantity = new TextField();
            Quantity.setId(_id);
            Quantity.setPromptText("2");

            GridPane.setRowIndex(lab, row_index);
            GridPane.setRowIndex(Quantity, row_index);
            GridPane.setColumnIndex(Quantity, 2);

            Right.getChildren().add(lab);
            Right.getChildren().add(Quantity);
        }
        if (_id.equals("Expire_date")) {
            final Label lab = new Label(label);
            Expire_date = new TextField();
            Expire_date.setId(_id);
            Expire_date.setPromptText("02-13-2024");

            GridPane.setRowIndex(lab, row_index);
            GridPane.setRowIndex(Expire_date, row_index);
            GridPane.setColumnIndex(Expire_date, 2);

            Right.getChildren().add(lab);
            Right.getChildren().add(Expire_date);
        }
    }

    public void printProduct(String name, String text, Integer index)
    {
        final Label label = new Label(name);
        Label labelText = new Label(text);

        if(index!= 0){
            GridPane.setRowIndex(labelText, index);
            GridPane.setRowIndex(label, index);
        }

        GridPane.setColumnIndex(labelText, 2);

        Right.getChildren().add(label);

        Right.getChildren().add(labelText);
    }

    public void call_print_product(IngredientDTO rowData,String use)
    {
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
    public void viewProductDetail(IngredientDTO rowData)
    {
        Right.getChildren().clear();
        call_print_product(rowData, "view");

        if(Application.authenticatedUser.getUsername().equals("admin")){
            final Button Delete_product = new Button("Delete ");
            GridPane.setRowIndex(Delete_product, 10);
            Right.getChildren().add(Delete_product);

            Delete_product.setOnAction(event -> {
                if (AllProductsTable.getSelectionModel().getSelectedIndex() >= 0) {
                    IngredientDTO selectedItem = AllProductsTable.getSelectionModel().getSelectedItem();
                    AllProductsTable.getItems().remove(selectedItem);
                    IngredientInTheFridgeDAO.deleteProduct(selectedItem);
                }
            });

            final Button Modify_product = new Button("Modify ");
            GridPane.setRowIndex(Modify_product, 10);
            GridPane.setColumnIndex(Modify_product, 2);
            Right.getChildren().add(Modify_product);

            Modify_product.setOnAction(event -> {
                AddIngredientToFridgeController.modify = true;
                goToAddProduct();
            });

        } else {
            printAddToFridge("Quantity: ", "Quantity", 10);
            printAddToFridge("Expire_date: ", "Expire_date", 11);

            final Button Submit_in_fridge = new Button("Add ");
            GridPane.setRowIndex(Submit_in_fridge, 12);
            Right.getChildren().add(Submit_in_fridge);

            Submit_in_fridge.setOnAction(event -> {
                if (checkAddToFridge()) {

                    String date = Expire_date.getText();
                    DateTimeFormatter pattern =
                            DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate formattedDate = LocalDate.parse(date, pattern);

                    IngredientInTheFridgeDTO p = new IngredientInTheFridgeDTO(rowData.getFood(), Integer.parseInt(Quantity.getText()), formattedDate);
                    IngredientInTheFridgeDAO.add_product(p);
                }
            });
        }
    }

    public boolean checkAddToFridge()
    {
        return (!Quantity.getText().isBlank() && Utils.isNumeric(Quantity.getText()) && !Expire_date.getText().isBlank());
    }

    public void Search_for_ingredient()
    {
        String ingredientName = SearchIngredient.getText();
        if(!ingredientName.equals("")) {
            try {
                ArrayList<IngredientDTO> searched_ingredients = IngredientDAO.search_ingredient(ingredientName);
                if(searched_ingredients != null)
                {
                    data.clear();
                    data.addAll(searched_ingredients);
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
            if(Application.authenticatedUser.getUsername().equals("admin"))
                Application.changeScene("HomePageAdmin");
            else
                Application.changeScene("HomePage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToAddProduct()
    {
        try {
            Application.changeScene("AddIngredient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
