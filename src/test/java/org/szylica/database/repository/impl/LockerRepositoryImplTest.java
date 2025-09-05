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
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
public class LockerRepositoryImplTest {

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

    LockerRepository lockerRepository;

    @BeforeEach
    void setUp() {
        createTables();
        lockerRepository = new LockerRepositoryImpl(jdbiExtension.getJdbi());
    }

    @Test
    @DisplayName("Insert and get all lockers")
    void test1() {
        var locker = Locker.builder()
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var expectedLocker = Locker.builder()
                .id(1L)
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        Assertions.assertThat(lockerRepository.insert(locker)).isEqualTo(expectedLocker);
        Assertions.assertThat(lockerRepository.findAll()).containsExactly(expectedLocker);
    }

    @Test
    @DisplayName("Insert and get all lockers by id")
    void test2() {
        var locker = Locker.builder()
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var expectedLocker = Locker.builder()
                .id(1L)
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        Assertions.assertThat(lockerRepository.insert(locker)).isEqualTo(expectedLocker);
        Assertions.assertThat(lockerRepository.findById(1L).get()).isEqualTo(expectedLocker);
    }

    @Test
    @DisplayName("Insert and update locker")
    void test3() {
        var locker = Locker.builder()
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var lockerToUpdate = Locker.builder()
                .size(LockerSize.LARGE)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var expectedLocker = Locker.builder()
                .id(1L)
                .size(LockerSize.LARGE)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        lockerRepository.insert(locker);
        lockerRepository.update(1L, lockerToUpdate);
        Assertions.assertThat(lockerRepository.findById(1L).get()).isEqualTo(expectedLocker);
    }

    @Test
    @DisplayName("Insert and delete locker")
    void test4() {

        var locker = Locker.builder()
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var expectedLocker = Locker.builder()
                .id(1L)
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        lockerRepository.insert(locker);
        lockerRepository.delete(1L);
        Assertions.assertThat(lockerRepository.findById(1L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Insert and find all locker with args")
    void test5() {
        var locker = Locker.builder()
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        var expectedLocker = Locker.builder()
                .id(1L)
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .parcelMachineId(1L)
                .build();

        lockerRepository.insert(locker);
        Assertions.assertThat(lockerRepository.findAllWhere(Map.of("id", "1"))).containsExactly(expectedLocker);
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

            handle.execute(parcelMachinesTableSql);
            handle.execute(lockersTableSql);
            handle.execute("""
                    insert into parcel_machines (name, latitude, longitude, address)
                    values('Paczkomat WAW001', 52.22977, 21.01178, 'pies')
                    """);
        });
    }

    void dropTables() {
        jdbiExtension.getJdbi().useHandle(handle -> {
            handle.execute("DROP TABLE lockers");
            handle.execute("DROP TABLE parcel_machines");

        });
    }


}
