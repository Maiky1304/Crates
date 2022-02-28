package com.github.maiky1304.crates.database;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {

    void accept(T t) throws SQLException;

}
