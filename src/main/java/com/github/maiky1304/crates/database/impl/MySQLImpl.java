package com.github.maiky1304.crates.database.impl;

import com.github.maiky1304.crates.database.Database;
import com.github.maiky1304.crates.database.DatabaseCredentials;
import com.github.maiky1304.crates.database.SQLConsumer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.function.Function;

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
    public <T> T query(String query, SQLConsumer<PreparedStatement> psc, Class<T> clazz, Function<ResultSet, T> function) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement
                = connection.prepareStatement(query)) {
            psc.accept(statement);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return function.apply(rs);
            }
            return null;
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

    @Override
    public boolean run(String query) {
        try (Connection connection = dataSource.getConnection(); Statement statement
                = connection.createStatement()) {
            return statement.execute(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

}
