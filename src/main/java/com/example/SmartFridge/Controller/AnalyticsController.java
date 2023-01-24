package com.example.SmartFridge.Controller;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DAO.IngredientDAO;
import com.example.SmartFridge.DAO.aggregationsMongo;
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
        List<Document> result = aggregationsMongo.top10Ingredients();
        //System.out.println(result);
        ArrayList<IngredientDTO> ingredientList = new ArrayList<>();

        JSONObject obj;
        while(result.iterator().hasNext()){
            String text = result.iterator().next().toJson(); //i get a json
            obj = new JSONObject(text);
            ingredientList.add(
                    new IngredientDTO(
                            obj.getString("food"),
                            obj.getString("measure"),
                            obj.getString("grams"),
                            obj.getString("calories"),
                            obj.getString("protein"),
                            obj.getString("fat"),
                            obj.getString("fiber"),
                            obj.getString("carbs"),
                            obj.getString("category")
                    )
            );
        }

        data.addAll(ingredientList);
    }

    @FXML
    private void goBack() throws IOException {
        Application.changeScene("HomePageAdmin");
    }

}
