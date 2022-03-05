package com.github.maiky1304.crates.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DatabaseCredentials {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    public static DatabaseCredentials fromConfig(ConfigurationSection config) {
        String host = config.getString("host");
        String port = config.getString("port");
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");
        return new DatabaseCredentials(host, port, username, password, database);
    }

}
