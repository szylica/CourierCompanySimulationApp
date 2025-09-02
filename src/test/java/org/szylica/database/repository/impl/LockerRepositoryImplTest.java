package org.szylica.database.repository.impl;

import org.assertj.core.api.Assertions;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.jdbi.v3.testing.junit5.tc.TestcontainersDatabaseInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.szylica.database.builder.TableBuilder;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.model.ParcelMachine;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
public class ParcelMachineRepositoryImplTest {

    @Container
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.3")
            .withUsername("user")
            .withPassword("user1234")
            .withDatabaseName("test_db");

    static final TestcontainersDatabaseInformation mysqlContainerInformation =
            TestcontainersDatabaseInformation.of(
                    "user",
                    "test_db",
                    null,
                    (catalogName, schemanName) -> "create database if not exists %s".formatted(catalogName)
            );

    @RegisterExtension
    static JdbiExtension jdbiExtension = JdbiTestcontainersExtension.instance(mysqlContainerInformation, mySQLContainer);

    ParcelMachineRepository parcelMachineRepository;

    @BeforeEach
    void setUp() {
        createTables();
        parcelMachineRepository = new ParcelMachineRepositoryImpl(jdbiExtension.getJdbi());
    }

    @Test
    @DisplayName("Insert and get all parcel machines")
    void test1(){
        var parcelMachine = ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        var expectedParcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        Assertions.assertThat(parcelMachineRepository.insert(parcelMachine)).isEqualTo(expectedParcelMachine);
        Assertions.assertThat(parcelMachineRepository.findAll()).containsExactly(expectedParcelMachine);
    }

    @Test
    @DisplayName("Insert and get all parcel machine by id")
    void test2(){
        var parcelMachine = ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        var expectedParcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        Assertions.assertThat(parcelMachineRepository.insert(parcelMachine)).isEqualTo(expectedParcelMachine);
        Assertions.assertThat(parcelMachineRepository.findById(1L).get()).isEqualTo(expectedParcelMachine);
    }

    @Test
    @DisplayName("Insert and update parcel machine")
    void test3(){
        var parcelMachine = ParcelMachine.builder()
                .name("Paczkomat AAW001")
                .address("kot")
                .latitude(53.22977)
                .longitude(21.01178)
                .build();

        var parcelMachineToUpdate = ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        var expectedParcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        parcelMachineRepository.insert(parcelMachine);
        parcelMachineRepository.update(1L, parcelMachineToUpdate);
        Assertions.assertThat(parcelMachineRepository.findById(1L).get()).isEqualTo(expectedParcelMachine);
    }

    @Test
    @DisplayName("Insert and delete parcel machine")
    void test4(){


        var parcelMachine = ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        var expectedParcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        parcelMachineRepository.insert(parcelMachine);
        parcelMachineRepository.delete(1L);
        Assertions.assertThat(parcelMachineRepository.findById(1L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Insert and find all parcel machine with args")
    void test5(){


        var parcelMachine = ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        var expectedParcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build();

        parcelMachineRepository.insert(parcelMachine);
        Assertions.assertThat(parcelMachineRepository.findAllWhere(Map.of("id", "1"))).containsExactly(expectedParcelMachine);
    }


    @AfterEach
    void tearDown() {
        dropTables();
    }

    void createTables() {

        jdbiExtension.getJdbi().useHandle(handle -> {

            var parcelMachinesTableSql = new TableBuilder("parcel_machines")
                    .addColumn("id", "bigint", "primary key", "auto_increment")
                    .addColumn("name", "varchar(255)", "not null")
                    .addColumn("latitude", "double", "not null")
                    .addColumn("longitude", "double", "not null")
                    .addColumn("address", "varchar(255)", "not null")
                    .buildSql();

            var lockersTableSql = new TableBuilder("lockers")
                    .addColumn("id", "bigint", "primary key", "auto_increment")
                    .addColumn("size", "varchar(50)", "not null")
                    .addColumn("status", "varchar(50)", "not null")
                    .addColumn("parcel_machine_id", "bigint")
                    .addForeignKeyConstraint(
                            "parcel_machine_id",
                            "parcel_machines",
                            "id",
                            "cascade",
                            "cascade")
                    .buildSql();

            var parcelsTableSql = new TableBuilder("parcels")
                    .addColumn("id", "bigint", "primary key", "auto_increment")
                    .addColumn("width", "int", "not null")
                    .addColumn("height", "int", "not null")
                    .addColumn("depth", "int", "not null")
                    .addColumn("status", "varchar(50)", "not null")
                    .addColumn("locker_id", "bigint")
                    .addForeignKeyConstraint(
                            "locker_id",
                            "lockers",
                            "id",
                            "cascade",
                            "cascade")
                    .buildSql();

            var ordersTableSql = new TableBuilder("orders")
                    .addColumn("id", "bigint", "primary key", "auto_increment")
                    .addColumn("created_at", "timestamp", "not null")
                    .addColumn("delivered_at", "timestamp")
                    .addColumn("user_id", "bigint", "not null")
                    .addColumn("parcel_id", "bigint")
                    .addForeignKeyConstraint(
                            "parcel_id",
                            "parcels",
                            "id",
                            "cascade",
                            "cascade")
                    .buildSql();
            handle.execute(parcelMachinesTableSql);
            handle.execute(lockersTableSql);
            handle.execute(parcelsTableSql);
            handle.execute(ordersTableSql);
        });
    }

    void dropTables() {
        jdbiExtension.getJdbi().useHandle(handle -> {
            handle.execute("DROP TABLE orders");
            handle.execute("DROP TABLE parcels");
            handle.execute("DROP TABLE lockers");
            handle.execute("DROP TABLE parcel_machines");

        });
    }


}
