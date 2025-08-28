CREATE TABLE IF NOT EXISTS parcel_machines (
    id integer primary key auto_increment,
    name varchar(100) not null,
    latitude decimal(11, 11) not null,
    longitude decimal(11, 11) not null,
    address varchar(200) not null
);

INSERT INTO parcel_machines (name, latitude, longitude, address) VALUES
                                                                     ('Paczkomat WAW001', 52.2297700, 21.0117800, 'Warszawa, ul. Marszałkowska 1'),
                                                                     ('Paczkomat KRK002', 50.0614300, 19.9365800, 'Kraków, ul. Floriańska 15'),
                                                                     ('Paczkomat GDA003', 54.3520500, 18.6463700, 'Gdańsk, ul. Długa 20');

CREATE TABLE IF NOT EXISTS lockers (
    id integer primary key auto_increment,
    parcel_machine_id integer,
    size varchar(20) not null,
    status varchar(30) not null,
    width decimal(4, 2) not null,
    height decimal(4, 2) not null,
    depth decimal(4, 2) not null,
    foreign key(parcel_machine_id) references parcel_machines(id) on delete cascade on update cascade
    );

INSERT INTO lockers (parcel_machine_id, size, status, width, height, depth) VALUES
                                                                                (1, 'Small', 'free', 30.00, 20.00, 40.00),
                                                                                (1, 'Medium', 'occupied', 40.00, 30.00, 50.00),
                                                                                (1, 'Large', 'free', 60.00, 40.00, 60.00),
                                                                                (2, 'Small', 'free', 30.00, 20.00, 40.00),
                                                                                (2, 'Medium', 'free', 40.00, 30.00, 50.00),
                                                                                (3, 'Large', 'occupied', 60.00, 40.00, 60.00);

CREATE TABLE IF NOT EXISTS parcels (
    id integer primary key auto_increment,
    locker_id integer,
    status varchar(30) not null,
    width decimal(4, 2) not null,
    height decimal(4, 2) not null,
    depth decimal(4, 2) not null,
    foreign key(locker_id) references lockers(id) on delete cascade on update cascade
    );

INSERT INTO parcels (locker_id, status, width, height, depth) VALUES
                                                                  (2, 'in_transit', 38.00, 28.00, 45.00),
                                                                  (6, 'delivered', 55.00, 38.00, 58.00);

CREATE TABLE IF NOT EXISTS users (
    id integer primary key auto_increment,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(100) not null,
    phone varchar(15) not null,
    latitude decimal(10, 10) not null,
    longitude decimal(10, 10) not null
    );

INSERT INTO users (first_name, last_name, email, phone, latitude, longitude) VALUES
                                                                                 ('Jan', 'Kowalski', 'jan.kowalski@example.com', '+48123456789', 52.2300000, 21.0100000),
                                                                                 ('Anna', 'Nowak', 'anna.nowak@example.com', '+48111222333', 50.0600000, 19.9400000),
                                                                                 ('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com', '+48555111222', 54.3500000, 18.6500000);

CREATE TABLE IF NOT EXISTS orders (
    id integer primary key auto_increment,
    user_id integer,
    parcel_id integer,
    created_at varchar(100),
    delivered_at varchar(100),
    foreign key(user_id) references users(id) on delete cascade on update cascade,
    foreign key(parcel_id) references parcels(id) on delete cascade on update cascade
    );

INSERT INTO orders (user_id, parcel_id, created_at, delivered_at) VALUES
                                                                      (1, 1, '2025-08-20 10:15:00', NULL),
                                                                      (2, 2, '2025-08-18 15:30:00', '2025-08-25 12:00:00');