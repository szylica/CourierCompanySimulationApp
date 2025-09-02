package org.szylica.repository.db.generic;

import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T, ID> implements CrudRepository<T, ID> {
    protected final Jdbi jdbi;
    private final Class<T> entityClass;
    private final String tableName;
    private final Map<String, String> customColumnNames;

    @SuppressWarnings("unchecked")
    protected AbstractRepository(Jdbi jdbi) {
        this.jdbi  = jdbi;
        this.entityClass = (Class<T>) getGenericTypeClass();
        this.tableName = English.plural(entityClass.getSimpleName().toLowerCase());
        this.customColumnNames = getFieldColumnMap();
        registerMappers();
    }

    protected abstract void registerMappers();

    // Custom names for mapping classes names to column names
    protected abstract Map<String, String> getFieldColumnMap();


    @Override
    public T insert(T entity) {

        var nonNullFields = getNonNullFields(entity);
        if (nonNullFields.isEmpty()) {
            throw new IllegalArgumentException("No fields to insert");
        }

        var sql = createSqlInsert(nonNullFields);

        return jdbi.withHandle(handle -> {
            var update = handle.createUpdate(sql);
            bindFieldsToUpdate(entity, update, nonNullFields);
            update.execute();

            var selectSql = "select * from %s order by id desc limit 1".formatted(tableName);
            return handle
                    .createQuery(selectSql)
                    .mapTo(entityClass)
                    .one();

        });


    }

    @Override
    public void update(ID id, T entity) {

        var nonNullFields = getNonNullFields(entity);
        if (nonNullFields.isEmpty()) {
            throw new IllegalArgumentException("No fields to insert");
        }

        var sql = createSqlUpdate(nonNullFields, id);

        jdbi.useHandle(handle -> {
            var update = handle.createUpdate(sql);
            update.bind("id", id);
            bindFieldsToUpdate(entity, update, nonNullFields);
            update.execute();
        });
    }

    @Override
    public void delete(ID id) {
        // DELETE FROM table_name WHERE condition;

        var sql = "DELETE from %s where id = :id".formatted(tableName);

        jdbi.useHandle(handle -> {
            var update = handle.createUpdate(sql);
            update.bind("id", id);
            update.execute();
        });
    }

    @Override
    public Optional<T> findById(ID id) {

        var sql = "SELECT * FROM %s WHERE id = :id".formatted(tableName);

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                    .bind("id", id)
                    .mapTo(entityClass)
                    .findFirst()
                );

    }

    @Override
    public List<T> findAll() {
        var sql = "SELECT * FROM %s".formatted(tableName);
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(entityClass)
                        .list());

    }

    @Override
    public List<T> findAllWhere(Map<String, String> filters, String... separators) {
        if(filters == null ) {
            throw new IllegalArgumentException("filters cannot be null");
        }

        if(filters.size()-1 != separators.length) {
            throw new IllegalArgumentException("wrong number of separators");
        }

        StringBuilder where = new StringBuilder();

        var i = 0;
        for(Map.Entry<String, String> entry : filters.entrySet()) {
            where.append("%s = %s ".formatted(getColumnName(entry.getKey()), entry.getValue()));
            if(i < separators.length) {
                where.append(separators[i]).append(" ");
            }
            i++;
        }

        var sql = "SELECT * FROM %s WHERE %s".formatted(tableName,  where.toString());
        System.out.println(sql);
        return null;
    }

    private Class<?> getGenericTypeClass() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new IllegalArgumentException("Class does not have a generic superclass");
    }

    private List<String> getFieldsWithoutId(){
        return Arrays.stream(entityClass.getDeclaredFields())
                .map(Field::getName)
                .filter(field -> !field.equalsIgnoreCase("id"))
                .toList();
    }

    private List<String> getNonNullFields(T entity){
        return getFieldsWithoutId().stream()
                .filter(field -> {
                    try{
                        var column = entityClass.getDeclaredField(field);
                        column.setAccessible(true);
                        return column.get(entity) != null;
                    }catch (Exception e){
                        throw new IllegalStateException(e);
                    }
                })
                .toList();
    }

    private String getPlaceholders(List<String> fields){
        return fields.stream()
                .map(field -> ":" + field)
                .collect(Collectors.joining(", "));
    }

    private String getColumnNames(List<String> fields){
        return fields.stream()
                .map(this::getColumnName)
                .collect(Collectors.joining(", "));
    }

    private String getColumnName(String field){
        return customColumnNames.getOrDefault(field, field);
    }


    private String createSqlInsert(List<String> nonNullFields) {


        return "INSERT INTO %s (%s) VALUES (%s);".formatted(
                tableName,
                getColumnNames(nonNullFields),
                getPlaceholders(nonNullFields));
    }

    private String createSqlUpdate(List<String> nonNullFields, ID id) {

        var set = nonNullFields.stream()
                .map(field -> "%s = %s".formatted(
                        getColumnName(field),
                        ":" + field
                        ))
                .collect(Collectors.joining(", "));

        var idStr = "id = " + id;

        return "UPDATE %s SET %s WHERE %s".formatted(tableName, set, idStr);
    }

    private void bindFieldsToUpdate(T entity, Update update, List<String> fields){
        for(String fieldName : fields){
            try{
                var field = entityClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                var value = field.get(entity);
                if (value != null){
                    update.bind(fieldName, value);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }





}
