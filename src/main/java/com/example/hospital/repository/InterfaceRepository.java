package com.example.hospital.repository;
import java.util.List;
import java.util.Optional;


public interface InterfaceRepository<T,ID> {

    T save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
    boolean existsById(ID id);

}
