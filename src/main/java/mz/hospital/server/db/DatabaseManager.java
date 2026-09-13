package mz.hospital.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:src/main/resources/database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {

        String sql = """
                CREATE TABLE IF NOT EXISTS patients (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    birth_date TEXT NOT NULL,
                    gender TEXT NOT NULL,
                    phone TEXT NOT NULL
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

            System.out.println("Base de dados inicializada.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar a base de dados.", e);
        }
    }
}