package carsharing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
