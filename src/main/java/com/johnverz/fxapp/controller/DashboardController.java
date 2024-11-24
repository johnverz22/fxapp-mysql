package com.johnverz.fxapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {
    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private TableView<Item> dataTable;

    @FXML
    public void initialize() {
        // Populate the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024 Revenue");
        series.getData().add(new XYChart.Data<>("Jan", 20000));
        series.getData().add(new XYChart.Data<>("Feb", 25000));
        series.getData().add(new XYChart.Data<>("Mar", 30000));
        series.getData().add(new XYChart.Data<>("Apr", 40000));
        series.getData().add(new XYChart.Data<>("May", 50000));
        lineChart.getData().add(series);

        // Populate the table
        ObservableList<Item> data = FXCollections.observableArrayList(
                new Item(1, "John Doe", "Active", "2024-01-01"),
                new Item(2, "Jane Smith", "Inactive", "2024-02-15"),
                new Item(3, "Mike Brown", "Active", "2024-03-10")
        );

        TableColumn<Item, Integer> idColumn = (TableColumn<Item, Integer>) dataTable.getColumns().get(0);
        TableColumn<Item, String> nameColumn = (TableColumn<Item, String>) dataTable.getColumns().get(1);
        TableColumn<Item, String> statusColumn = (TableColumn<Item, String>) dataTable.getColumns().get(2);
        TableColumn<Item, String> dateColumn = (TableColumn<Item, String>) dataTable.getColumns().get(3);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        dataTable.setItems(data);
    }

    public static class Item {
        private final Integer id;
        private final String name;
        private final String status;
        private final String date;

        public Item(Integer id, String name, String status, String date) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.date = date;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public String getDate() {
            return date;
        }
    }
}
