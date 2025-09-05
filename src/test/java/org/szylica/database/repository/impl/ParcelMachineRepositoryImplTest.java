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
import org.szylica.database.mapper.LockerMapper;
import org.szylica.database.mapper.ParcelMachineMapper;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.concurrent.locks.Lock;

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

    @Test
    @DisplayName("Find first parcel machine within distance with available locker by size")
    void test6(){

        double targetLatitude = 21.0122;
        double targetLongitude = 52.2297;
        double maxDistance = 10.0;
        LockerSize lockerSize = LockerSize.SMALL;
        LockerStatus lockerStatus = LockerStatus.FREE;

        createLockersTableAndInsertData(targetLatitude, targetLongitude, lockerSize, lockerStatus);

        var result = parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                targetLatitude, targetLongitude, maxDistance, lockerSize);

        System.out.println(result);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    @DisplayName("No parcel machine found when it is out of range")
    void test7(){

        double targetLatitude = 21.0122;
        double targetLongitude = 52.2297;
        double targetLatitude2 = 22.0122;
        double targetLongitude2 = 53.2297;
        double maxDistance = 5.0;
        LockerSize lockerSize = LockerSize.SMALL;
        LockerStatus lockerStatus = LockerStatus.FREE;

        createLockersTableAndInsertData(targetLatitude, targetLongitude, lockerSize, lockerStatus);


        var result = parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                targetLatitude2, targetLongitude2, maxDistance, lockerSize);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("No parcel machine found when all lockers are occupied")
    void test8(){

        double targetLatitude = 21.0122;
        double targetLongitude = 52.2297;
        double maxDistance = 100.0;
        LockerSize lockerSize = LockerSize.SMALL;
        LockerStatus lockerStatus = LockerStatus.OCCUPIED;

        createLockersTableAndInsertData(targetLatitude, targetLongitude, lockerSize, lockerStatus);


        var result = parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                targetLatitude, targetLongitude, maxDistance, lockerSize);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("No parcel machine found when there are no lockers of the required size")
    void test9(){

        double targetLatitude = 21.0122;
        double targetLongitude = 52.2297;
        double maxDistance = 10.0;
        LockerSize lockerSize = LockerSize.SMALL;
        LockerStatus lockerStatus = LockerStatus.FREE;

        createLockersTableAndInsertData(targetLatitude, targetLongitude, LockerSize.LARGE, lockerStatus);

        var result = parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                targetLatitude, targetLongitude, maxDistance, lockerSize);

        Assertions.assertThat(result).isEmpty();
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

            handle.execute(parcelMachinesTableSql);
        });
    }

    void createLockersTableAndInsertData(
            double latitude,
            double longitude,
            LockerSize lockerSize,
            LockerStatus lockerStatus) {
        jdbiExtension.getJdbi().useHandle(handle -> {
            var lockersTableSql = new TableBuilder("lockers")
                    .addColumn("id", "bigint", "primary key", "auto_increment")
                    .addColumn("size", "varchar(50)", "not null")
                    .addColumn("status", "varchar(50)", "not null")
                    .addColumn("parcel_machine_id", "bigint", "not null")
                    .addForeignKeyConstraint(
                            "parcel_machine_id",
                            "parcel_machines",
                            "id",
                            "cascade",
                            "cascade")
                    .buildSql();
            handle.execute(lockersTableSql);

            handle.execute(
                    "INSERT INTO parcel_machines (name, latitude, longitude, address) VALUES ('PM1', ?, ?, 'Address 1')",
                    latitude,
                    longitude
            );
            handle.execute(
                    "INSERT INTO lockers (parcel_machine_id, status, size) VALUES (1, ?, ?)",
                    lockerStatus.name(),
                    lockerSize.name()
            );
        });
    }


    void dropTables() {
        jdbiExtension.getJdbi().useHandle(handle -> {
            handle.execute("DROP TABLE IF EXISTS lockers");
            handle.execute("DROP TABLE IF EXISTS parcel_machines");

        });
    }


}
