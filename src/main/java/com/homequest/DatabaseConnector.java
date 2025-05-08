package com.homequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    // database connection details - replace user and password is yours is different - DOM
    private static final String DB_URL = "jdbc:mysql://localhost:3306/HomeQuest"; // for MySQL
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678abcd";

    private static Connection connection = null;
    private DatabaseConnector() {}

    // singleton database connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // I put the JDBC driver JAR inside the project classpath - DOM
                Class.forName("com.mysql.cj.jdbc.Driver"); // for MySQL Connector/J
                
                System.out.println("Connecting to database...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection established.");

            // error catchinig
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC Driver not found. Make sure the driver JAR is in the classpath.");
                e.printStackTrace();
                throw new SQLException("JDBC Driver not found", e);
            } catch (SQLException e) {
                System.err.println("Error connecting to the database!");
                System.err.println("SQLState: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }

    //close the singleton database connection if it is open
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing the database connection!");
                e.printStackTrace();
            } finally {
                connection = null; // connection is reset even if close fails
            }
        }
    }
    
    // main method for connection testing - not part of project
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                 System.out.println("Successfully connected to the database for testing.");
                 DatabaseConnector.closeConnection();
            } else {
                 System.out.println("Failed to get connection for testing.");
            }
        } catch (SQLException e) {
             System.err.println("Database connection test failed: " + e.getMessage());
        }
    }
}
