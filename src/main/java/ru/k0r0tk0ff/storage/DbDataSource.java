package ru.k0r0tk0ff.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class DbDataSource {
    private static final Logger LOGGER  = LoggerFactory.getLogger(DbDataSource.class);

    private Connection connection = null;
    private Properties properties;

    public void initializeResources() throws StorageException {
        DbPropertiesLoader dbPropertiesLoader = new DbPropertiesLoader();
        properties = dbPropertiesLoader.getProperties();
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password"));
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Get connection success");
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws StorageException {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new StorageException("Cannot close connection!", e);
            }
        }
    }
}
