package com.johnverz.fxapp.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Product extends BaseModel<Product> {
    private int productId;
    private String productName;
    private Category category; // Association with Category
    private double price;
    private String pictureFileName;

    // Constructors
    public Product() {}

    public Product(int productId, String productName, Category category, double price, String pictureFileName) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.pictureFileName = pictureFileName;
    }

    @Override
    protected String getTableName() {
        return "products";
    }

    @Override
    protected Product fromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));

        return new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                category,
                rs.getDouble("price"),
                rs.getString("picture_file_name")
        );
    }

    // Save or update a product
    public boolean save() throws SQLException {
        String query = productId == 0 ?
                "INSERT INTO products (product_name, category_id, price, picture_file_name) VALUES (?, ?, ?, ?)" :
                "UPDATE products SET product_name = ?, category_id = ?, price = ?, picture_file_name = ? WHERE product_id = ?";
        Object[] params = productId == 0 ?
                new Object[]{productName, category.getCategoryId(), price, pictureFileName} :
                new Object[]{productName, category.getCategoryId(), price, pictureFileName, productId};
        return executeUpdate(query, params) > 0;
    }

    // Getters and Setters...
    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }
}
