package com.course.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    protected Connection connection;

    public DBContext() {
        try {
            String user = "root";
            String pass = "12345";
            String url = "jdbc:mysql://localhost:3306/practice_rest";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connected successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error during connection test: " + ex.getMessage());
        }
    }

    // Main method to test the connection
    public static void main(String[] args) {
        DBContext dbContext = new DBContext();
        dbContext.testConnection();
    }
}

