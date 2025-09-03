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
import org.szylica.database.repository.LockerRepository;
import org.szylica.database.repository.ParcelRepository;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.szylica.model.parcel.Parcel;
import org.szylica.model.parcel.enums.ParcelStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
public class ParcelRepositoryImplTest {

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

    ParcelRepository parcelRepository;

    @BeforeEach
    void setUp() {
        createTables();
        parcelRepository = new ParcelRepositoryImpl(jdbiExtension.getJdbi());
    }

    @Test
    @DisplayName("Insert and get all lockers")
    void test1(){
        var parcel = Parcel.builder()
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        var expectedParcel = Parcel.builder()
                .id(1L)
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        Assertions.assertThat(parcelRepository.insert(parcel)).isEqualTo(expectedParcel);
        Assertions.assertThat(parcelRepository.findAll()).containsExactly(expectedParcel);
    }

    @Test
    @DisplayName("Insert and get all lockers by id")
    void test2(){
        var parcel = Parcel.builder()
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        var expectedParcel = Parcel.builder()
                .id(1L)
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        Assertions.assertThat(parcelRepository.insert(parcel)).isEqualTo(expectedParcel);
        Assertions.assertThat(parcelRepository.findById(1L).get()).isEqualTo(expectedParcel);
    }

    @Test
    @DisplayName("Insert and update locker")
    void test3(){
        var parcel = Parcel.builder()
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        var expectedParcel = Parcel.builder()
                .id(1L)
                .width(20)
                .height(20)
                .depth(20)
                .lockerId(1L)
                .status(ParcelStatus.IN_TRANSIT)
                .build();

        var parcelToUpdate = Parcel.builder()
                .width(20)
                .height(20)
                .depth(20)
                .lockerId(1L)
                .status(ParcelStatus.IN_TRANSIT)
                .build();

        parcelRepository.insert(parcel);
        parcelRepository.update(1L, parcelToUpdate);
        Assertions.assertThat(parcelRepository.findById(1L).get()).isEqualTo(expectedParcel);
    }

    @Test
    @DisplayName("Insert and delete locker")
    void test4(){

        var parcel = Parcel.builder()
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        parcelRepository.insert(parcel);
        parcelRepository.delete(1L);
        Assertions.assertThat(parcelRepository.findById(1L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Insert and find all locker with args")
    void test5(){
        var parcel = Parcel.builder()
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        var expectedParcel = Parcel.builder()
                .id(1L)
                .width(10)
                .height(10)
                .depth(10)
                .lockerId(1L)
                .status(ParcelStatus.PENDING)
                .build();

        parcelRepository.insert(parcel);
        Assertions.assertThat(parcelRepository.findAllWhere(Map.of("id", "1"))).containsExactly(expectedParcel);
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
//
//            var ordersTableSql = new TableBuilder("orders")
//                    .addColumn("id", "bigint", "primary key", "auto_increment")
//                    .addColumn("created_at", "timestamp", "not null")
//                    .addColumn("delivered_at", "timestamp")
//                    .addColumn("user_id", "bigint", "not null")
//                    .addColumn("parcel_id", "bigint")
//                    .addForeignKeyConstraint(
//                            "parcel_id",
//                            "parcels",
//                            "id",
//                            "cascade",
//                            "cascade")
//                    .buildSql();
            handle.execute(parcelMachinesTableSql);
            handle.execute(lockersTableSql);
            handle.execute(parcelsTableSql);
//            handle.execute(ordersTableSql);

            handle.execute("insert into parcel_machines (name, latitude, longitude, address) values ('Paczkomat WAW001', 52.22977, 21.01178, 'pies')");

            handle.execute("insert into lockers (size, status, parcel_machine_id) values ('SMALL', 'FREE', 1), ('MEDIUM', 'FREE', 1);");
        });
    }

    void dropTables() {
        jdbiExtension.getJdbi().useHandle(handle -> {
//            handle.execute("DROP TABLE orders");
            handle.execute("DROP TABLE parcels");
            handle.execute("DROP TABLE lockers");
            handle.execute("DROP TABLE parcel_machines");

        });
    }


}
