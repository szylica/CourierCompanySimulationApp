package org.szylica.model.builder;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TableBuilder {
    private final String tableName;
    private final List<Column> columns;
    private final List<String> foreignKeys;


    public TableBuilder(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("tableName is null or blank");
        }

        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
    }

    public TableBuilder addColumn(String name, String type, String... constraints) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is null or blank");
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("type is null or blank");
        }

        columns.add(new Column(name, type, String.join(" ", constraints).trim()));
        return this;
    }

    public TableBuilder addForeignKeyConstraint(
            String columnName,
            String referencedTableName,
            String referencedColumnName,
            String onDelete,
            String onUpdate) {
        foreignKeys.add("foreign key (%s) references %s (%s) on delete %s on update %s".formatted(
                columnName,
                referencedTableName,
                referencedColumnName,
                onDelete,
                onUpdate));
        return this;
    }

    public String buildSql() {
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("No columns added to table");
        }
        String columnDefinitions = columns
                .stream()
                .map(Column::toString)
                .collect(Collectors.joining(", "));

        var createSqlStatement = "create table if not exists %s ( %s %s );".formatted(
                tableName,
                columnDefinitions,
                foreignKeys.isEmpty() ? "" : ", " + String.join(", ", foreignKeys));

        log.debug("Generated SQL statement for creating new table: {}", createSqlStatement);

        return createSqlStatement;
    }

    private record Column(String name, String type, String constraints) {
        public String toString() {
            return name + " " + type + ((!constraints.isBlank()) ? " " + constraints : "");
        }
    }
}
