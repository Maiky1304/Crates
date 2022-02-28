package com.github.maiky1304.crates.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface Database {

    ResultSet query(String query, SQLConsumer<PreparedStatement> psc);
    boolean execute(String query, SQLConsumer<PreparedStatement> psc);

}
