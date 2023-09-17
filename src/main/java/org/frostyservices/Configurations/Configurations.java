package org.frostyservices.Configurations;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configurations {
    private static final String CONFIG_FILE_NAME = "config.yml";

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

    public void loadConfig() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE_NAME);

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
    }


    public void saveConfig() throws IOException {
        File configFile = new File(CONFIG_FILE_NAME);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter(configFile)) {
            Map<String, Object> data = new HashMap<>();
            data.put("Token", this.token);
            data.put("MySQL.Hostname", this.hostname);
            data.put("MySQL.Port", this.port);
            data.put("MySQL.Database", this.database);
            data.put("MySQL.Username", this.username);
            data.put("MySQL.Password", this.password);

            yaml.dump(data, writer);
        }
    }

}

