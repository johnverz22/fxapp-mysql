package com.johnverz.fxapp.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category extends BaseModel<Category> {
    private int categoryId;
    private String categoryName;

    // Constructors
    public Category() {}

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    @Override
    protected String getTableName() {
        return "categories";
    }

    @Override
    protected Category fromResultSet(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("category_id"), rs.getString("category_name"));
    }

    // Save or update a category
    public boolean save() throws SQLException {
        String query = categoryId == 0 ?
                "INSERT INTO categories (category_name) VALUES (?)" :
                "UPDATE categories SET category_name = ? WHERE category_id = ?";
        Object[] params = categoryId == 0 ? new Object[]{categoryName} : new Object[]{categoryName, categoryId};
        return executeUpdate(query, params) > 0;
    }

    // Getters and Setters...
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
