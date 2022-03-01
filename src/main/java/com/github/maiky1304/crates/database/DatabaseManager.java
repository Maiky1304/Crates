package com.github.maiky1304.crates.database;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public interface DatabaseManager<M> {

    CompletableFuture<M> find(String field, Object value);
    CompletableFuture<QueryResult> delete(String field, Object value);
    CompletableFuture<QueryResult> insert(HashMap<String, Object> data);
    CompletableFuture<QueryResult> update(HashMap<String, Object> data, String field, Object value);

}
