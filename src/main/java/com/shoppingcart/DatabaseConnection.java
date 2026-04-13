package com.shoppingcart;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        InputStream classpathInput = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config.properties");

        Path rootConfigPath = Path.of("config.properties");
        InputStream fileInput = null;
        if (classpathInput == null && Files.exists(rootConfigPath)) {
            try {
                fileInput = Files.newInputStream(rootConfigPath);
            } catch (IOException e) {
                throw new RuntimeException("Error opening root config.properties", e);
            }
        }

        try (InputStream input = classpathInput != null ? classpathInput : fileInput) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties (classpath or project root)");
            }
            props.load(input);
            URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
