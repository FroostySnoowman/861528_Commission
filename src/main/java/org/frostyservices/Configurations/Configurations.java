package org.frostyservices.Configurations;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.frostyservices.Main;
import org.frostyservices.Utils.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Configurations {
    public static FileUtils fileUtils = new FileUtils();

    public static void init() {
        if (!fileUtils.checkFileExist("config.yml")) {

            Main.logger.info("Creating default configuration file...");
            createConfigFile();
            Main.logger.info("Created!");
        } else {
            Main.logger.info("Configuration file has already been created!");
        }
    }

    public static void createConfigFile() {
        if (!fileUtils.checkFileExist("config.yml")) {

            fileUtils.createFile("config.yml");

            InputStream is = Configurations.class.getClassLoader().getResourceAsStream("config.yml");

            try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try(PrintWriter output = new PrintWriter(new FileWriter("config.yml",true))) {
                        output.printf("%s\r\n", line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fileUtils.checkFileExist("config.yml")) {
                Main.logger.severe("Default config file created, please check config and run app again!");
                System.exit(0);
            } else {
                Main.logger.severe("Error creating default config, try reinstalling the application or check the config!");
                System.exit(0);
            }
        }
    }

    public static String getConfigValue(String value) {

        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader("config.yml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = reader.read();
        } catch (YamlException e) {
            e.printStackTrace();
        }
        Map map = (Map)object;

        return (String) map.get(value);
    }
}