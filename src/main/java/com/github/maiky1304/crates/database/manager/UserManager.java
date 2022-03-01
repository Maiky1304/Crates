package com.github.maiky1304.crates.database.manager;

import com.github.maiky1304.crates.CratesPlugin;
import com.github.maiky1304.crates.DatabaseExclusionStrategy;
import com.github.maiky1304.crates.database.DatabaseCredentials;
import com.github.maiky1304.crates.database.DatabaseManager;
import com.github.maiky1304.crates.database.QueryResult;
import com.github.maiky1304.crates.database.impl.MySQLImpl;
import com.github.maiky1304.crates.database.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public class UserManager implements DatabaseManager<User> {

    private final MySQLImpl database;
    private final Gson gson;

    private final HashMap<UUID, User> cache = new HashMap<>();

    public UserManager(CratesPlugin instance) throws InstantiationException {
        ConfigurationSection mysql = instance.getConfiguration().getConfigurationSection("mysql");

        if (mysql == null) {
            throw new InstantiationException("Cannot find the MySQL credentials in the config.yml");
        }

        DatabaseExclusionStrategy strategy = new DatabaseExclusionStrategy();
        this.gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .addDeserializationExclusionStrategy(strategy)
                .create();
        this.database = new MySQLImpl(DatabaseCredentials.fromConfig(mysql));
    }

    public CompletableFuture<User> createUser(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            User user = new User();
            user.setUuid(player.getUniqueId());
            user.setCratesToday(0);
            user.setLastCrateTimestamp(0L);

            insert(user.toDataObject());

            return user;
        });
    }

    public CompletableFuture<QueryResult> updateUser(User user) {
        return update(user.toDataObject(), "uuid", user.getUuid().toString());
    }

    public CompletableFuture<User> findUser(UUID uuid) {
        return find("uuid", uuid.toString());
    }

    @Override
    public CompletableFuture<User> find(String field, Object value) {
        String query = String.format("SELECT * FROM `users` WHERE %s=?", field);
        return CompletableFuture.supplyAsync(() -> database.query(query, ps -> {
            ps.setObject(1, value);
        })).thenApply(User::fromResultSet);
    }

    @Override
    public CompletableFuture<QueryResult> delete(String field, Object value) {
        String query = String.format("DELETE FROM `users` WHERE %s=?", field);
        return CompletableFuture.supplyAsync(() -> database.execute(query, ps -> {
            ps.setObject(1, value);
        })).thenApplyAsync(QueryResult::fromState);
    }

    @Override
    public CompletableFuture<QueryResult> insert(HashMap<String, Object> data) {
        String query = String.format("INSERT INTO `users`(%s) VALUES(%s)",
                StringUtils.join(data.keySet(), ','), StringUtils.chop(StringUtils.repeat("?,", data.size())));
        return CompletableFuture.supplyAsync(() -> database.execute(query, ps -> {
            int index = 1;
            for (Object obj : data.values()) {
                ps.setObject(index, obj);
                index++;
            }
        })).thenApplyAsync(QueryResult::fromState);
    }

    @Override
    public CompletableFuture<QueryResult> update(HashMap<String, Object> data, String field, Object value) {
        String query = String.format("UPDATE `users` SET %s WHERE %s=?",
                StringUtils.join(data.keySet().stream().map(key -> String.format("`%s`=?", key))
                        .collect(Collectors.toList()), ','), field);
        return CompletableFuture.supplyAsync(() -> database.execute(query, ps -> {
            int index = 1;
            for (Object obj : data.values()) {
                ps.setObject(index, obj);
                index++;
            }

            ps.setObject(index, value);
        })).thenApply(QueryResult::fromState);
    }

}
