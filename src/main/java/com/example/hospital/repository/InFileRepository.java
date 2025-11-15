package com.example.hospital.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InFileRepository<T, ID> implements InterfaceRepository<T, ID> {

    private final File dataFile;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<ID, T> storage;
    private final AtomicLong sequence;

    public InFileRepository(String filename) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);


        File dataDir = new File("src/main/resources/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        this.dataFile = new File(dataDir, filename);
        this.storage = new ConcurrentHashMap<>();
        this.sequence = new AtomicLong(1);

        loadDataFromFile();
        initializeSequence();
    }

    private void loadDataFromFile() {
        try {
            if (dataFile.exists() && dataFile.length() > 0) {

                List<Object> rawList = objectMapper.readValue(dataFile, new TypeReference<List<Object>>() {});
                for (Object rawObj : rawList) {

                    T entity = convertToEntity(rawObj);
                    ID id = getEntityId(entity);
                    storage.put(id, entity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file: " + dataFile.getPath(), e);
        }
    }


    protected abstract T convertToEntity(Object rawObject);

    private void initializeSequence() {
        long maxId = storage.keySet().stream()
                .map(this::parseIdToLong)
                .max(Long::compareTo)
                .orElse(0L);
        sequence.set(maxId + 1);
    }

    private void saveDataToFile() {
        try {
            List<T> entities = new ArrayList<>(storage.values());
            objectMapper.writeValue(dataFile, entities);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + dataFile.getPath(), e);
        }
    }

    @Override
    public T save(T entity) {
        ID id = getEntityId(entity);


        if (id == null || id.toString().isEmpty()) {
            id = generateId();
            setEntityId(entity, id);
        }

        storage.put(id, entity);
        saveDataToFile();
        return entity;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(ID id) {
        storage.remove(id);
        saveDataToFile();
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

    protected ID generateId() {
        return parseId("DEPT_" + sequence.getAndIncrement());
    }

    protected abstract ID getEntityId(T entity);
    protected abstract void setEntityId(T entity, ID id);
    protected abstract ID parseId(String id);
    protected abstract Long parseIdToLong(ID id);
}