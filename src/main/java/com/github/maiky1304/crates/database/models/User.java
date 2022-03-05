package com.github.maiky1304.crates.database.models;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@Getter @Setter
public class User {

    private UUID uuid;
    private long lastCrateTimestamp;
    private int cratesToday;

    public static User fromResultSet(ResultSet resultSet) {
        try {
            User user = new User();
            user.uuid = UUID.fromString(resultSet.getString("uuid"));
            user.lastCrateTimestamp = resultSet.getLong("last_crate");
            user.cratesToday = resultSet.getInt("crates_today");
            return user;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public HashMap<String, Object> toDataObject() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("uuid", this.uuid.toString());
        data.put("last_crate", this.lastCrateTimestamp);
        data.put("crates_today", this.cratesToday);
        return data;
    }

}
