package com.johnverz.fxapp.utility;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static Connection connection = null;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME, Config.DB_USER, Config.DB_PASS);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateUser(String username, String password){
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            preparedStatement.executeUpdate();
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}