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
import org.szylica.database.repository.OrderRepository;
import org.szylica.model.Order;
import org.szylica.model.parcel.Parcel;
import org.szylica.model.parcel.enums.ParcelStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
public class OrderRepositoryImplTest {

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

    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        createTables();
        orderRepository = new OrderRepositoryImpl(jdbiExtension.getJdbi());
    }

    @Test
    @DisplayName("Insert and get all lockers")
    void test1(){

        var time = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var order = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        var expectedOrder = Order.builder()
                .id(1L)
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        Assertions.assertThat(orderRepository.insert(order)).isEqualTo(expectedOrder);
        Assertions.assertThat(orderRepository.findAll()).containsExactly(expectedOrder);
    }

    @Test
    @DisplayName("Insert and get all lockers by id")
    void test2(){
        var time = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var order = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        var expectedOrder = Order.builder()
                .id(1L)
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        Assertions.assertThat(orderRepository.insert(order)).isEqualTo(expectedOrder);
        Assertions.assertThat(orderRepository.findById(1L).get()).isEqualTo(expectedOrder);
    }

    @Test
    @DisplayName("Insert and update locker")
    void test3(){
        var time = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var order = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        var expectedOrder = Order.builder()
                .id(1L)
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(2L)
                .build();


        var orderToUpdate = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(2L)
                .build();

        orderRepository.insert(order);
        orderRepository.update(1L, orderToUpdate);
        Assertions.assertThat(orderRepository.findById(1L).get()).isEqualTo(expectedOrder);
    }

    @Test
    @DisplayName("Insert and delete locker")
    void test4(){

        var time = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var order = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        orderRepository.insert(order);
        orderRepository.delete(1L);
        Assertions.assertThat(orderRepository.findById(1L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Insert and find all locker with args")
    void test5(){

        var time = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        var order = Order.builder()
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        var expectedOrder = Order.builder()
                .id(1L)
                .createdAt(time)
                .deliveredAt(time.plusDays(1))
                .userId(1L)
                .parcelId(1L)
                .build();

        orderRepository.insert(order);
        Assertions.assertThat(orderRepository.findAllWhere(Map.of("id", "1"))).containsExactly(expectedOrder);
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

            handle.execute("insert into parcel_machines (name, latitude, longitude, address) values ('Paczkomat WAW001', 52.22977, 21.01178, 'pies')");

            handle.execute("insert into lockers (size, status, parcel_machine_id) values ('SMALL', 'FREE', 1), ('MEDIUM', 'FREE', 1);");

            handle.execute("""
                    insert into parcels (width, height, depth, status, locker_id)
                    values 
                        (10, 10, 10, 'IN_TRANSIT', 1),
                        (20, 30, 40, 'PENDING', 2),
                        (20, 30, 40, 'DELIVERED', 1);
                    """);
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
