package org.frostyservices.Configurations;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configurations {
    private static final String CONFIG_FILE_NAME = "config.yml";

    private String token_placeholder = "TOKEN_HERE";
    private String hostname_placeholder = "HOSTNAME_HERE";
    private String password_placeholder = "PASSWORD_HERE";
    private String database_placeholder = "DATABASE_HERE";
    private String username_placeholder = "USERNAME_HERE";
    private Integer port_placeholder = 3306;

    private String token;
    private String hostname;
    private Integer port;
    private String database;
    private String username;
    private String password;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void loadConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE_NAME);

        try {
            if (inputStream != null) {
                try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> data = yaml.load(reader);

                    if (data != null) {
                        this.token = (String) data.get("Token");
                        this.hostname = (String) data.get("MySQL.Hostname");
                        this.port = (Integer) data.get("MySQL.Port");
                        this.database = (String) data.get("MySQL.Database");
                        this.username = (String) data.get("MySQL.Username");
                        this.password = (String) data.get("MySQL.Password");
                    } else {
                        throw new IOException("Invalid configuration file.");
                    }
                }
            } else {
                throw new IOException("Configuration file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.token = (this.token != null) ? this.token : token_placeholder;
        this.hostname = (this.hostname != null) ? this.hostname : hostname_placeholder;
        this.port = (this.port != null) ? this.port : port_placeholder;
        this.database = (this.database != null) ? this.database : database_placeholder;
        this.username = (this.username != null) ? this.username : username_placeholder;
        this.password = (this.password != null) ? this.password : password_placeholder;
    }

    public void saveConfig() {
        File configFile = new File(CONFIG_FILE_NAME);

        // Check if the file exists and has content
        if (configFile.exists() && configFile.length() > 0) {
            System.out.println("Configuration file already contains data. Skipping save.");
            return;
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            String yamlContent = "Token: \"" + token_placeholder + "\"\n" +
                    "\n" +
                    "MySQL:\n" +
                    "   Hostname: \"" + hostname_placeholder + "\"\n" +
                    "   Password: \"" + password_placeholder + "\"\n" +
                    "   Database: \"" + database_placeholder + "\"\n" +
                    "   Username: \"" + username_placeholder + "\"\n" +
                    "   Port: " + port_placeholder;

            writer.write(yamlContent);
            System.out.println("Configuration file saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

