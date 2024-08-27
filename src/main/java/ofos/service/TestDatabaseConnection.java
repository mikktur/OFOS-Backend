package ofos.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://10.120.32.94:3306/mikt";
        String user = "xxx";
        String password = "xxx";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connection successful!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB Driver class not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while connecting to the database.");
            e.printStackTrace();
        }
    }
}