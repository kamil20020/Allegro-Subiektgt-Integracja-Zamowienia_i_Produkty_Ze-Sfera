package pl.kamil_dywan.service;

import pl.kamil_dywan.api.allegro.ProductApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private AppProperties(){


    }

    private static final Properties properties = new Properties();

    public static void loadProperties(){

        InputStream inputStream = ProductApi.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            properties.load(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getProperty(String key, Class<T> type){

        return (T) getProperty(key);
    }

    public static String getProperty(String key){

        String gotRawValue = properties.getProperty(key);

        return gotRawValue;
    }
}
