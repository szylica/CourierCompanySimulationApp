package org.szylica.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.szylica.database.builder.TableBuilder;

@Configuration
@ComponentScan("org.szylica")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class AppBeansConfig {

    private final Environment environment;


    @Bean
    public Jdbi jdbi() {

        var jdbi = Jdbi.create(
                environment.getRequiredProperty("db.url"),
                environment.getRequiredProperty("db.username"),
                environment.getRequiredProperty("db.password")
        );

        var parcelMachinesTableSql = new TableBuilder("parcel_machines")
                .addColumn("id", "integer", "primary key", "auto_increment")
                .addColumn("name", "varchar(255)", "not null")
                .addColumn("latitude", "float", "not null")
                .addColumn("longitude", "float", "not null")
                .addColumn("address", "varchar(255)", "not null")
                .buildSql();

        var lockersTableSql = new TableBuilder("lockers")
                .addColumn("id", "integer", "primary key", "auto_increment")
                .addColumn("size", "varchar(50)", "not null")
                .addColumn("status", "varchar(50)", "not null")
                .addColumn("parcel_machine_id", "integer")
                .addForeignKeyConstraint(
                        "parcel_machine_id",
                        "parcel_machines",
                        "id",
                        "cascade",
                        "cascade")
                .buildSql();

        var parcelsTableSql = new TableBuilder("parcels")
                .addColumn("id", "integer", "primary key", "auto_increment")
                .addColumn("width", "int", "not null")
                .addColumn("height", "int", "not null")
                .addColumn("depth", "int", "not null")
                .addColumn("status", "varchar(50)", "not null")
                .addColumn("locker_id", "integer")
                .addForeignKeyConstraint(
                        "locker_id",
                        "lockers",
                        "id",
                        "cascade",
                        "cascade")
                .buildSql();

        var ordersTableSql = new TableBuilder("orders")
                .addColumn("id", "integer", "primary key", "auto_increment")
                .addColumn("created_at", "datetime", "not null")
                .addColumn("delivered_at", "datetime")
                .addColumn("user_id", "int", "not null")
                .addColumn("parcel_id", "int")
                .addForeignKeyConstraint(
                        "parcel_id",
                        "parcels",
                        "id",
                        "cascade",
                        "cascade")
                .buildSql();

        jdbi.useHandle(handle -> {
            handle.execute("drop database if exists db_1;");
            handle.execute("create database db_1;");
            handle.execute("use db_1;");
            handle.execute(parcelMachinesTableSql);
            handle.execute(lockersTableSql);
            handle.execute(parcelsTableSql);
            handle.execute(ordersTableSql);
        });

        return jdbi;

    }

    @Bean
    public Gson gson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }
}