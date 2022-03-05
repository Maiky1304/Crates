package com.github.maiky1304.crates.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Function;

public interface Database {

    <T> T query(String query, SQLConsumer<PreparedStatement> psc, Class<T> clazz, Function<ResultSet, T> function);
    boolean execute(String query, SQLConsumer<PreparedStatement> psc);
    boolean run(String query);

}
