package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ivar on 20.02.2015.
 */
public class Config {

    private static Config singleton;

    private Properties prop;

    private Config() throws IOException {
        prop = new Properties();
        String propFilePath = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFilePath);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Config file not found.");
        }
    }

    private static Config getSingleton() throws IOException {
        if(singleton == null) singleton = new Config();
        return singleton;
    }

    public static String getProperty(String propertyName) throws IOException {
        return getSingleton().prop.getProperty(propertyName);
    }

}
