package com.johnverz.fxapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class FoodCardController {

    @FXML
    private VBox foodCard;

    @FXML
    private ImageView foodImage;

    @FXML
    private Label foodLabel;

    @FXML
    private Label foodPrice;

    boolean isSelected;

    public void setData(String name, double price, String image){
        foodLabel.setText(name);
        foodPrice.setText(Double.toString(price));
        foodImage.setImage(new Image( "file:storage/images/" + image));
    }

    public void initialize() {
        foodCard.setOnMouseClicked(event -> toggleSelection());
    }

    private void toggleSelection() {
        isSelected = !isSelected;
        foodCard.getStyleClass().remove("selected");
        if (isSelected) {
            foodCard.getStyleClass().add("selected");
        }
    }

}
