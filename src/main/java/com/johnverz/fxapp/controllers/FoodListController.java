package com.johnverz.fxapp.controllers;

import com.johnverz.fxapp.models.Food;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodListController
{
    @FXML
    private TilePane foodTiles;

    private List<VBox> foodCards;
    public void initialize(){
        foodCards = new ArrayList<>();

        try {
            List<Food> foods = (new Food()).fetchAll();

            for(Food food : foods){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food_card.fxml"));
                VBox foodCard = loader.load();
                FoodCardController controller = loader.getController();
                controller.setData(food.getName(), food.getPrice(), food.getImage());
                foodCard.setOnMouseClicked(e -> {
                    System.out.print("Hello! Food is selected");
                    // Handle when the card is clicked or selected like adding it to a list
                });
                foodCards.add(foodCard);
            }

            foodTiles.getChildren().addAll(foodCards);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
