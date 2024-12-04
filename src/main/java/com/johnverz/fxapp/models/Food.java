package com.johnverz.fxapp.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Food extends BaseModel<Food>{

    private String name;
    private double price;
    private String image;
    @Override
    protected String getTableName() {
        return "products";
    }

    public Food(){}

    public Food(String name, double price, String image){
        this.name = name;
        this.price = price;
        this.image = image;
    }

    @Override
    protected Food fromResultSet(ResultSet rs) throws SQLException {
        return new Food(
                rs.getString("product_name"),
                rs.getDouble("price"),
                rs.getString("picture_file_name")
        );
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
