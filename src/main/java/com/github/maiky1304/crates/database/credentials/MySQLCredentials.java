package com.github.maiky1304.crates.database.credentials;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@Getter
@RequiredArgsConstructor
public class MySQLCredentials {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    public static MySQLCredentials fromConfig(ConfigurationSection config) {
        String host = config.getString("host", "localhost");
        String port = config.getString("port", "3306");
        String username = config.getString("username", "root");
        String password = config.getString("password", "p@ssword");
        String database = config.getString("database");
        return new MySQLCredentials(host, port, username, password, database);
    }

}
