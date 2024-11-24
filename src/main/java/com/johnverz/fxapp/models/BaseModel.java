package com.johnverz.fxapp.models;

import com.johnverz.fxapp.utility.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel<T> {
    private static final Database database = new Database();

    // Abstract method to be implemented by subclasses to specify the table name
    protected abstract String getTableName();

    // Abstract method to convert a ResultSet row into a model object
    protected abstract T fromResultSet(ResultSet rs) throws SQLException;

    /**
     * Executes a SELECT query.
     */
    protected ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection connection = database.connection();
        PreparedStatement stmt = connection.prepareStatement(query);
        setParameters(stmt, params);
        return stmt.executeQuery();
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     */
    protected int executeUpdate(String query, Object... params) throws SQLException {
        Connection connection = database.connection();
        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        setParameters(stmt, params);
        int affectedRows = stmt.executeUpdate();
        return affectedRows;
    }



    /**
     * Fetches all rows from the table.
     */
    public List<T> fetchAll() throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName();
        ResultSet rs = executeQuery(query);

        while (rs.next()) {
            results.add(fromResultSet(rs));
        }
        return results;
    }

    /**
     * Executes a SELECT query with a JOIN.
     */
    public List<T> fetchAllWithJoin(String joinQuery, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName() + " " + joinQuery;
        ResultSet rs = executeQuery(query, params);

        while (rs.next()) {
            results.add(fromResultSet(rs));
        }
        return results;
    }

    /**
     * Sets parameters for a PreparedStatement.
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
}
