package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.IngredientDAO;
import com.example.SmartFridge.DAO.aggregationsMongo;
import com.example.SmartFridge.DTO.AggregationTransportDTO;
import com.example.SmartFridge.DTO.IngredientDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void initialize(){
        Name.setCellValueFactory(
                new PropertyValueFactory<IngredientDTO, String>("food")
        );
        Table.setItems(data);
    }

    public void show_most_10_ingredients() throws JSONException {
        ArrayList<AggregationTransportDTO> result = aggregationsMongo.top10Ingredients();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        for(AggregationTransportDTO  r : result){
           //completare
        }

        data.addAll(ingredientList);
    }

    @FXML
    private void goBack() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

}
