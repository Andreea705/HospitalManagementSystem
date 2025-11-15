package com.example.hospital.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InFileRepository<T, ID> implements InterfaceRepository<T, ID> {
    private final File dataFile;
    private final ObjectMapper objectMapper;

        this.objectMapper = new ObjectMapper();


    }

        try{
            if(dataFile.exists() && dataFile.length() > 0){

                }
            }
        }catch(IOException e) {
        }
    }


        try{
        } catch(IOException e) {
        }
    }

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
    public void deleteById(ID id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }

}
