package com.example.hospital.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InFileRepository<T, ID> implements InterfaceRepository<T, ID> {
    private final Map<ID, T> storage = new ConcurrentHashMap<>();
    private final File dataFile;
    private final ObjectMapper objectMapper;
    private final Class<T> entityType;

    public InFileRepository(String fileName, Class<T> entityType) {
        this.entityType = entityType;
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String dataDir = "src/main/resources/data";
        new File(dataDir).mkdirs();

        this.dataFile = Paths.get(dataDir, fileName).toFile();
        loadData();
    }

    private void loadData() {
        try {
            if (dataFile.exists() && dataFile.length() > 0) {
                List<T> entities = objectMapper.readValue(dataFile,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, entityType));
                storage.clear();
                for (T entity : entities) {
                    storage.put(getEntityId(entity), entity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from " + dataFile.getPath(), e);
        }
    }

    private void saveData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, new ArrayList<>(storage.values()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to " + dataFile.getPath(), e);
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
        saveData();
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
        saveData();
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }


    protected ID generateId() {
        return parseId("ID_" + System.currentTimeMillis());
    }

    protected abstract ID getEntityId(T entity);
    protected abstract void setEntityId(T entity, ID id);
    protected abstract ID parseId(String id);
}