package com.gmail.nihilist.analytics.storage;

import com.gmail.nihilist.analytics.storage.model.JoinRecord;

import java.util.UUID;

public interface DataStorage {

    void createTable();

    JoinRecord getRecord(String hostname);

    void insert(UUID uuid, String hostname);
}
