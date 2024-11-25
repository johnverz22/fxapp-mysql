package com.johnverz.fxapp.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label userName;

    @FXML
    private Circle profilePicture;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private VBox sidebar;

    @FXML
    public void initialize() {
        // Set default profile image
        Image image =  new Image(getClass().getResource("/images/default-profile.png").toExternalForm());
        profilePicture.setFill(new ImagePattern(image));

        // Initialize node and default it to dashboard
        try {
            Node content = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
            mainContent.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username){
        userName.setText(username);
    }
    @FXML
    private void handelMenuClick(ActionEvent event){
        Button menu = (Button) event.getSource();
        removeActiveMenu();
        menu.getStyleClass().add("active");

        String fxmlFile = switch (menu.getId()) {
            case "home" -> "/fxml/dashboard.fxml";
            case "products" -> "/fxml/product.fxml";
            //case "account" -> "/fxml/account.fxml";
            default -> "";
        };

        try {
            Node content = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void removeActiveMenu(){
        sidebar.getChildrenUnmodifiable().forEach(control -> {
            control.getStyleClass().remove("active");
        });
    }




}