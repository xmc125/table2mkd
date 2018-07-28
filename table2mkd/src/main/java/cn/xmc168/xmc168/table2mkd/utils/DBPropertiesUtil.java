package cn.xmc168.xmc168.table2mkd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertiesUtil {
    static Properties properties = new Properties();
    static InputStream in = DBPropertiesUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
    
    static {
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getDbDriver() {
        return properties.getProperty("jdbc.driver");
    }
    
    public static String getUrl() {
        return properties.getProperty("jdbc.url");
    }
    
    public static String getUser() {
        return properties.getProperty("jdbc.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("jdbc.password");
    }

}
