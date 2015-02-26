package util;

import exceptions.MissingPropertyException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by ivar on 20.02.2015.
 */
public class Config {

    private Properties prop;

    public void load(String propFilePath) throws IOException {
        prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFilePath);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Config file not found.");
        }
    }

    public String getProperty(String propertyName) throws MissingPropertyException {
        String property = prop.getProperty(propertyName);
        if(property == null) throw new MissingPropertyException("Missing property '" + propertyName + "' in config file");
        return property;
    }

    public HashMap<String, String> getProperties(String[] propertyNames) throws MissingPropertyException {
        HashMap<String, String> properties = new HashMap<String, String>();
        StringBuilder msg = new StringBuilder();
        for(String propertyName : propertyNames) {
            try {
                properties.put(propertyName, getProperty(propertyName));
            } catch (MissingPropertyException e) {
                if (msg.length() != 0) msg.append(", ");
                msg.append(e.getMessage());
            }
        }
        if (msg.length() != 0) throw new MissingPropertyException(msg.toString());
        return properties;
    }

}
