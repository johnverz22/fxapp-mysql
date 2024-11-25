package com.johnverz.fxapp.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order extends BaseModel<Order> {
    private int orderId;
    private int quantity;
    private Product product;

    public Order(){}
    public Order(int orderId, int quantity, Product product){
        this.orderId = orderId;
        this.quantity = quantity;
        this. product = product;
    }


    @Override
    protected String getTableName() {
        return "orders";
    }

    @Override
    protected Order fromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));

        Product product = new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                category,
                rs.getDouble("price"),
                rs.getString("picture_file_name")
        );

        return new Order(
                rs.getInt("order_id"),
                rs.getInt("quantity"),
                product
        );
    }

    public boolean save() throws SQLException {

        //Implement save here similar to Product.
        return true;
    }
}
