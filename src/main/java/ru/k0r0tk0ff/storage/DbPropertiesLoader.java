package ru.k0r0tk0ff.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbPropertiesLoader {
    private Properties properties;
    private final String DB_PROPERTY_PATH = "/dbConnect.properties";
    public Properties getProperties() throws StorageException{
        properties = new Properties();
        try(InputStream input = getClass().getResourceAsStream(DB_PROPERTY_PATH )) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new StorageException("Cannot load properties!", e);
        }
    }
}
