package com.github.maiky1304.crates.database;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum QueryResult {

    SUCCESS(true), FAILED(false);

    private final boolean state;

    public static @NonNull QueryResult fromState(boolean state) {
        return Objects.requireNonNull(Arrays.stream(values())
                .filter(qr -> qr.state == state).findFirst().orElse(null));
    }

}
