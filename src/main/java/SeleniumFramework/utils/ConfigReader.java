package SeleniumFramework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties prop;

    public static void loadConfig() {
        if (prop == null) {
            prop = new Properties();
            try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
                prop.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String get(String key) {
        if (prop == null) loadConfig();
        return prop.getProperty(key);
    }
}
