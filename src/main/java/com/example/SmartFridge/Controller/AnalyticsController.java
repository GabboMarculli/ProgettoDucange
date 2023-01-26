package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.IngredientDAO;
import com.example.SmartFridge.DAO.aggregationsMongo;
import com.example.SmartFridge.DTO.AggregationTransportDTO;
import com.example.SmartFridge.DTO.IngredientDTO;
import com.example.SmartFridge.Utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsController {
    @FXML
    public TableView<IngredientDTO> Table;
    @FXML
    public TableColumn<IngredientDTO, String> Name;

    private ObservableList<IngredientDTO> data = FXCollections.observableArrayList();

    @FXML
    private GridPane left;
    public void setClick(MouseEvent mouseEvent) {
        Utils.setClick(mouseEvent);
    }

    public void unsetClick(MouseEvent mouseEvent) {
        Utils.unsetClick(mouseEvent);
    }
    public void setOver(MouseEvent mouseEvent) {
        Utils.setOver(mouseEvent);
    }
    public void unsetOver(MouseEvent mouseEvent) {
        Utils.unsetOver(mouseEvent);
    }
    public void initialize(){

        for(int i =0 ; i<5;i++) {
            printAnalytics("campo1", "campo2", i);
        }
        aggregationsMongo.top10votedrecipe();
        aggregationsMongo.userMostCommented();
        aggregationsMongo.ingredientsByCountry();

    }

    public void show_most_10_ingredients() {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.top10votedrecipe();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left.getChildren().clear();
        printAnalytics("Ingredient","Time Used",0);
        int i=1;
        for(AggregationTransportDTO  r : result){
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }
    public void show_most_10_recipes(ActionEvent actionEvent) {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.top10Ingredients();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left.getChildren().clear();
        printAnalytics("recipeName","avarageRate",0);
        int i=1;
        for(AggregationTransportDTO  r : result){
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    public void show_userMostCommented(ActionEvent actionEvent) {

        ArrayList<AggregationTransportDTO> result = aggregationsMongo.userMostCommented();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left.getChildren().clear();
        printAnalytics("Username","Total Comments",0);
        int i=1;
        for(AggregationTransportDTO  r : result){
            printAnalytics(r.getField1(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    public void ingredientsByCountry(ActionEvent actionEvent) {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.userMostCommented();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        left.getChildren().clear();
        printAnalytics_2("ingredient","country","quantity",0);
        int i=1;
        for(AggregationTransportDTO  r : result){
            printAnalytics_2(r.getField1(),r.getField2(),String.valueOf(r.getField3()),i);
            i++;
        }
    }

    private void printAnalytics_2(String field1, String field2,String field3, int index) {
        final Label label = new Label(field1);
        Label labelText = new Label(field2);
        Label label2 = new Label(field3);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 16;");
        labelText.setStyle("-fx-font-size: 15;");

        GridPane.setRowIndex(label, index);

        GridPane.setRowIndex(labelText, index);
        GridPane.setColumnIndex(labelText, 1);

        GridPane.setRowIndex(label2, index);
        GridPane.setColumnIndex(label2, 2);


        left.getChildren().add(label);
        left.getChildren().add(labelText);
        left.getChildren().add(label2);
        left.setHalignment(label, HPos.LEFT);
        left.setHalignment(labelText, HPos.LEFT);
        left.setHalignment(label2, HPos.LEFT);
    }

    public void printAnalytics(String field1, String field2, int index){
            final Label label = new Label(field1);
            Label labelText = new Label(field2);
            label.setStyle("-fx-font-weight: bold;-fx-font-size: 16;");
            labelText.setStyle("-fx-font-size: 15;");

            GridPane.setRowIndex(label, index);

            GridPane.setRowIndex(labelText, index);
            GridPane.setColumnIndex(labelText, 1);


            left.getChildren().add(label);
            left.getChildren().add(labelText);
            left.setHalignment(label, HPos.LEFT);
            left.setHalignment(labelText, HPos.LEFT);
    }

    @FXML
    private void goBack() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

}
