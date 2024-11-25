package com.johnverz.fxapp.controllers;

import com.johnverz.fxapp.models.Category;
import com.johnverz.fxapp.models.Order;
import com.johnverz.fxapp.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class OrderController {

    @FXML
    private TextField quantity;

    @FXML
    private ListView<Product> listView;

    @FXML
    void addToCart(ActionEvent event) {
        String qty = quantity.getText();
        Product product =  (Product) listView.getSelectionModel().getSelectedItem();
        System.out.println(qty);

        Order order = new Order(0,Integer.parseInt(qty), product);

        try {
            order.save();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize()  {
        Product product = new Product();
        try{
            List<Product> products = product.fetchAllWithJoin("LEFT JOIN categories ON products.category_id = categories.category_id");
            ObservableList<Product> productObservableList = FXCollections.observableArrayList(products);
            listView.setItems(productObservableList);

            listView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    if (empty || product == null) {
                        setText(null);
                    } else {
                        setText(product.getProductName() + "  | " + product.getPrice());
                    }
                }
            });


        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}
