package com.example.hospital.repository;

import java.util.*;

public abstract class GenericRepo<T, ID> implements InterfaceRepo<T, ID> {
    protected final Map<ID, T> storage = new HashMap<>();

    @Override
    public T save(T entity) {
        ID id = getEntityId(entity);
        storage.put(id, entity);
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
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

    protected abstract ID getEntityId(T entity);

    protected abstract ID parseId(String id);
}