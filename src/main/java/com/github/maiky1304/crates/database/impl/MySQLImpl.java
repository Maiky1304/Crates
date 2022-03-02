package com.github.maiky1304.crates.database.impl;

import com.github.maiky1304.crates.database.Database;
import com.github.maiky1304.crates.database.DatabaseCredentials;
import com.github.maiky1304.crates.database.SQLConsumer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLImpl implements Database {

    private final HikariDataSource dataSource;

    public MySQLImpl(DatabaseCredentials credentials) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", credentials.getHost(),
                credentials.getPort(), credentials.getDatabase()));
        hikariConfig.setUsername(credentials.getUsername());
        hikariConfig.setPassword(credentials.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public ResultSet query(String query, SQLConsumer<PreparedStatement> psc) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement(query)) {
            psc.accept(statement);

            return statement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean execute(String query, SQLConsumer<PreparedStatement> psc) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement(query)) {
            psc.accept(statement);
            return statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
