package com.github.maiky1304.crates.database;

import com.github.maiky1304.crates.database.consumer.SqlConsumer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public interface MySQL {

    void query(String query, SqlConsumer<PreparedStatement> psc, SqlConsumer<ResultSet> rsc);
    void update(String update, SqlConsumer<PreparedStatement> psc);
    void execute(String sql, SqlConsumer<Statement> sc);

}
