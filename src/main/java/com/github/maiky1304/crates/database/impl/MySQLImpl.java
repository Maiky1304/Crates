package com.github.maiky1304.crates.database.impl;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.database.MySQL;
import com.github.maiky1304.crates.database.consumer.SqlConsumer;
import com.github.maiky1304.crates.database.credentials.MySQLCredentials;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class MySQLImpl implements MySQL {

    private final CratesPlugin instance;
    private final HikariDataSource dataSource;

    public MySQLImpl(CratesPlugin instance, MySQLCredentials credentials) {
        this.instance = instance;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                credentials.getHost(), credentials.getPort(), credentials.getDatabase()));
        config.setUsername(credentials.getUsername());
        config.setPassword(credentials.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void query(String query, SqlConsumer<PreparedStatement> psc, SqlConsumer<ResultSet> rsc) {
        runAsync(() -> {
            try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                psc.accept(statement);

                ResultSet rs = statement.executeQuery();
                rsc.accept(rs);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void update(String update, SqlConsumer<PreparedStatement> psc) {
        runAsync(() -> {
            try(Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(update)) {
                psc.accept(statement);
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void execute(String sql, SqlConsumer<Statement> sc) {
        runAsync(() -> {
            try(Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
                sc.accept(statement);
                statement.execute(sql);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Executes a task asynchronously
     * @param runnable
     */
    public void runAsync(Runnable runnable) {
        instance.getServer().getScheduler()
                .runTaskAsynchronously(instance, runnable);
    }

}
