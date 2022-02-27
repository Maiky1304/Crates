package com.github.maiky1304.crates.database.consumer;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer<T> {

    void accept(T t) throws SQLException;

}
