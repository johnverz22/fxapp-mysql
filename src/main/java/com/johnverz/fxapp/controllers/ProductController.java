package com.johnverz.fxapp.controllers;

import com.johnverz.fxapp.models.Category;
import com.johnverz.fxapp.models.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    private final Product product = new Product();
    private final Category categoryModel = new Category();
    @FXML
    private TextField productNameField, priceField, pictureField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn, categoryColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;

    public void initialize() {
        // Set up table columns
        //productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        // Use a custom cell factory to display row numbers
        productIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue() == null ? null :
                    new ReadOnlyObjectWrapper<>(productTable.getItems().indexOf(cellData.getValue()) + 1);
        });

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(cellData ->
                cellData.getValue().getCategory() != null
                        ? new SimpleStringProperty(cellData.getValue().getCategory().getCategoryName())
                        : new SimpleStringProperty("")
        );
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set initial width for each column
        productIdColumn.setPrefWidth(50);
        productNameColumn.setPrefWidth(300);
        categoryColumn.setPrefWidth(150);
        priceColumn.setPrefWidth(100);

        // Load initial data
        loadProducts();
        loadCategories();
    }

    private void loadProducts() {
        try {
            List<Product> products = product.fetchAllWithJoin("LEFT JOIN categories ON products.category_id = categories.category_id");
            ObservableList<Product> productObservableList = FXCollections.observableArrayList(products);
            productTable.setItems(productObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading products: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryModel.fetchAll();
            ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categories);
            categoryComboBox.setItems(categoryObservableList);

            // Use a StringConverter to display category_name
            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getCategoryName() : "";
                }

                @Override
                public Category fromString(String string) {
                    return null; // No need for reverse mapping in this case
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading categories: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveButton() {
        try {
            // Validate inputs
            String productName = productNameField.getText().trim();
            String priceText = priceField.getText().trim();
            String pictureFileName = pictureField.getText().trim();
            Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

            if (productName.isEmpty() || priceText.isEmpty() || selectedCategory == null) {
                showError("Please fill in all required fields.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                showError("Invalid price. Please enter a valid number.");
                return;
            }

            // Generate a random unique file name for the image
            String uniqueFileName = java.util.UUID.randomUUID() + pictureFileName.substring(pictureFileName.lastIndexOf("."));
            File sourceFile = new File(pictureFileName);

            File storageDir = new File("storage/images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            File destFile = new File("storage/images/" + uniqueFileName);

            // Move the image to the resources folder
            try {
                java.nio.file.Files.copy(sourceFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to move image file: " + e.getMessage());
                return;
            }

            // Create a new product
            Product product = new Product();
            product.setProductName(productName);
            product.setPrice(price);
            product.setPictureFileName(uniqueFileName);
            product.setCategory(selectedCategory);

            // Save the product
            if (product.save()) {
                showInfo("Product saved successfully!");
                loadProducts();
                clearForm();
            } else {
                showError("Failed to save product.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void clearForm() {
        productNameField.clear();
        priceField.clear();
        pictureField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        categoryComboBox.setPromptText("Select a category");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void pickImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            File tempFile = new File(System.getProperty("java.io.tmpdir"), selectedFile.getName());
            try {
                java.nio.file.Files.copy(selectedFile.toPath(), tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                pictureField.setText(tempFile.getAbsolutePath());
            } catch (IOException e) {
                showError("Failed to copy image file to temporary directory: " + e.getMessage());
            }
        }
    }

    @FXML
    private void addCategory() {
        Stage stage = new Stage();

        HBox fields = new HBox();
        fields.setSpacing(10);

        TextField categoryNameField = new TextField();
        categoryNameField.setStyle("-fx-background-color: #f0f0f0;\n" +
                "    -fx-border-color: #dcdcdc;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px;\n" +
                "    -fx-font-size: 14px;");
        categoryNameField.setPromptText("Enter category name");

        Button addButton = new Button("Add Category");
        addButton.setStyle("-fx-background-color: #4a90e2;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px 20px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-cursor: hand;");
        addButton.setOnMouseEntered(event -> addButton.setStyle("-fx-background-color: #357abd;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px 20px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-cursor: hand;"));
        addButton.setOnMouseExited(event -> addButton.setStyle("-fx-background-color: #4a90e2;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px 20px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-cursor: hand;"));
        addButton.setOnMousePressed(event -> addButton.setStyle("-fx-background-color: #2c5a8a;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px 20px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-cursor: hand;"));
        addButton.setOnMouseReleased(event -> addButton.setStyle("-fx-background-color: #4a90e2;\n" +
                "    -fx-text-fill: #ffffff;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-padding: 10px 20px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-cursor: hand;"));


        VBox parent = new VBox(10);
        parent.setPadding(new Insets(10));

        ListView<Category> categoryListView = getCategoryListView();

        addButton.setOnAction(event -> {
            String categoryName = categoryNameField.getText().trim();
            if (!categoryName.isEmpty()) {
                Category newCategory = new Category();
                newCategory.setCategoryName(categoryName);
                try {
                    if (newCategory.save()) {
                        loadCategories();
                        categoryNameField.clear();
                    } else {
                        showError("Failed to add category.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error adding category: " + e.getMessage());
                }
            } else {
                showError("Category name cannot be empty.");
            }
        });

        fields.getChildren().addAll(categoryNameField, addButton);
        HBox.setHgrow(categoryNameField, Priority.ALWAYS);

        parent.getChildren().addAll(fields, categoryListView);
        Scene scene = new Scene(parent, 768, 350);
        stage.setScene(scene);
        stage.setTitle("Add Category");
        stage.show();

    }

    private ListView<Category> getCategoryListView() {
        ListView<Category> categoryListView = new ListView<>(categoryComboBox.getItems());
        categoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                } else {
                    setText(category.getCategoryName());
                }
            }
        });
        return categoryListView;
    }


    @FXML
    private void handleTableDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Handle the double-click event, show image of products
                showImage(selectedProduct.getPictureFileName());
            }
        }
    }

    // Show Product image
    private void showImage(String pictureFileName) {
        Stage stage = new Stage();
        StackPane parent = new StackPane();
        ImageView imageView = new ImageView(new Image("file:storage/images/" + pictureFileName));
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        parent.getChildren().add(imageView);
        Scene scene = new Scene(parent, 768, 480);
        stage.setScene(scene);
        stage.setTitle("Product Image");
        stage.show();
    }
}

