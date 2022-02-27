package com.github.maiky1304.crates.database;

import com.github.maiky1304.crates.CratesPlugin;
import com.google.common.base.Suppliers;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.Getter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class MongoDatabase <M> {

    private final MongoClient client;
    private final Morphia morphia;
    private final Datastore datastore;
    private final BasicDAO<M, String> data;

    public MongoDatabase(String mongoUri, Class<M> modelClass) {
        this.client = new MongoClient(new MongoClientURI(mongoUri));

        this.morphia = new Morphia();
        this.morphia.map(modelClass);

        this.datastore = this.morphia.createDatastore(this.client, "seniorteam_crates");
        this.datastore.ensureIndexes();

        this.data = new BasicDAO<>(modelClass, datastore);
    }

    /**
     * Attempts to store the type of M
     * @param model
     * @return a serializable key object that contains the data like the ID of
     * the document provided by the MongoDB server.
     */
    public CompletableFuture<Key<M>> store(M model) {
        return CompletableFuture.supplyAsync(() -> data.save(model));
    }

    /**
     * Attempts to find all types of M that matches the predicate.
     * @param predicate
     * @return future containing a list with all types of M (can also be an
     * empty list)
     */
    public CompletableFuture<List<M>> find(Predicate<M> predicate) {
        return CompletableFuture.supplyAsync(() -> data.find().asList()
                .stream().filter(predicate).collect(Collectors.toList()));
    }

    /**
     * Attempts to find the first type of M that matches the predicate and
     * can be found in the database
     * @param predicate
     * @return future containing either the type of M or null if not found
     */
    public CompletableFuture<M> findOne(Predicate<M> predicate) {
        return CompletableFuture.supplyAsync(() -> data.find().asList()
                .stream().filter(predicate).findFirst().orElse(null));
    }

}
