package org.szylica.repository.db.generic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    T insert(T entity);
    void update(ID id, T entity);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAllWhere(Map<String, String> filters, String... separators);

}
