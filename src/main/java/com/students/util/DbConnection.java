package com.students.util;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private static final Properties properties = new Properties();
    private static HikariDataSource dataSource;

    static {
        try (InputStream input = DbConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IOException("db.properties file not found");
            }

            properties.load(input);

            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);

            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // Get a connection from the HikariCP pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Allow DAO classes to receive the connection pool
    public static DataSource getDataSource() {
        return dataSource;
    }

    // Close the pool when the application exits
    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}