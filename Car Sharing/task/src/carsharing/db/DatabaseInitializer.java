package carsharing.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            createCompanyTable(statement);
            createCarTable(statement);
            createCustomerTable(statement);
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }

    private static void createCompanyTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " +
                "COMPANY (ID INT NOT NULL AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "PRIMARY KEY (ID))";
        statement.execute(query);
        System.out.println("COMPANY table created or already exists.");
    }
    private static void createCarTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " +
                "CAR (ID INT NOT NULL AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL, " +
                "PRIMARY KEY (ID), " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
        statement.execute(query);
        System.out.println("CAR table created or already exists.");
    }

    private static void createCustomerTable(Statement statement) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " +
                "CUSTOMER (ID INT NOT NULL AUTO_INCREMENT, " +
                "NAME VARCHAR UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INT, " +
                "PRIMARY KEY (ID), " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
        statement.execute(query);
        System.out.println("CUSTOMER table created or already exists.");
    }
}
