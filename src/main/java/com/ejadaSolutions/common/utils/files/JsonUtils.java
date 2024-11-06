package com.ejadaSolutions.common.utils.files;

import com.ejadaSolutions.common.utils.logs.MyLogger;
import com.ejadaSolutions.common.utils.properties.PropertiesManager;
import org.apache.logging.log4j.core.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;

/**
 * This class extracts a given JSON file name to the script directory, then
 * calls the extracted file to collect data.
 *
 * @author Mahmoud Osama
 */
public class JsonUtils {
    private static final Logger log = new MyLogger().getLogger();
    private static final PropertiesManager propertiesManager = new PropertiesManager();

    public static String ReadJson(String key) {
        JSONParser parser = new JSONParser();
        String value = null;
        try {
            String path = System.getProperty("user.dir") + propertiesManager.getProp("test.data.path");
            log.info("Json Path is set to: {}", path);
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            value = (String) jsonObject.get(key);
            log.info("Successfully retrieved the value for key '{}': {}", key, value);
        } catch (FileNotFoundException e) {
            log.error("File not found at the specified path: {}", e.getMessage());
        } catch (IOException e) {
            log.error("IO Exception occurred: {}", e.getMessage());
        } catch (ParseException e) {
            log.error("Error parsing JSON file: {}", e.getMessage());
        }
        return value;
    }
}
