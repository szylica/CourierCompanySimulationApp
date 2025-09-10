# Parcel Delivery System

## Overview
This project implements an application for sending parcels via parcel machines.  
The main goal of the system is to provide a complete workflow for registering a parcel, assigning it to a suitable locker in the nearest parcel machine, and managing the order until delivery.

---

## Main Features

1. **Order Placement**  
   - A user creates an order by selecting a parcel with defined dimensions.  
   - After successful payment, the system begins processing the order.

2. **Order Creation via `OrderService`**  
   - The `createOrder` method receives:  
     - `userId`  
     - `parcelId`  
     - `maxDistance` (maximum distance to search for a parcel machine)  
     - `days` (delivery time in days).  
   - The system retrieves the parcel from `ParcelRepository` and throws an exception if not found.

3. **Locker Size Determination**  
   - The system calculates the required locker size (`LockerSize`) based on parcel dimensions.

4. **Finding a Suitable Parcel Machine**  
   - The system locates the user and searches for the nearest available parcel machine with a free locker of the required size.  
   - If no machine is found within the defined radius, the process is stopped.

5. **Locker Reservation**  
   - The locker is assigned to the parcel and updated in the database.  
   - The parcel record is updated with the `lockerId`.

6. **Order Creation**  
   - A new order (`Order`) is created with:  
     - `userId`  
     - `parcelId`  
     - `createdAt` (timestamp of order creation)  
     - `deliveredAt` (expected delivery date).  
   - The order is saved in `OrderRepository`.

7. **Order Confirmation**  
   - The user receives confirmation with:  
     - Parcel machine details  
     - Assigned locker information  
     - Estimated delivery time.

---

## Database Schema

### Tables

- **`parcel_machines`** – stores parcel machine details (location, coordinates, name).  
- **`lockers`** – stores lockers inside parcel machines (size, status).  
- **`parcels`** – stores parcel details (dimensions, status, locker assignment).  
- **`users`** – stores sender information (name, email, phone, location).  
- **`orders`** – stores parcel orders linked to users and parcels.  

### Relationships

- One parcel machine → many lockers  
- One locker → one parcel  
- One user → many orders  
- One order → one parcel  

---

## Parcel Lifecycle

1. **Parcel Registration**  
   - A new order is created in the `orders` table.  
   - At this point, `parcel_id` may be empty.

2. **Adding Parcel Data**  
   - Parcel dimensions and status (`pending`) are saved in `parcels`.

3. **Assigning Locker**  
   - The parcel is assigned to a free locker.  
   - Locker status changes to `occupied`.  
   - `locker_id` is updated in the `parcels` table.

4. **Updating Order**  
   - The `parcel_id` is linked in the `orders` table.  
   - Parcel status changes during delivery: `pending` → `in_transit` → `delivered`.

---

## Example SQL Schema

```sql
CREATE TABLE parcel_machines (
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    latitude  FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    address   VARCHAR(255) NOT NULL
);

CREATE TABLE lockers (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    size              VARCHAR(50) NOT NULL,
    status            VARCHAR(50) NOT NULL,
    parcel_machine_id BIGINT NOT NULL,
    FOREIGN KEY (parcel_machine_id) REFERENCES parcel_machines (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE parcels (
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    width     INT NOT NULL,
    height    INT NOT NULL,
    depth     INT NOT NULL,
    status    VARCHAR(50) NOT NULL,
    locker_id BIGINT,
    FOREIGN KEY (locker_id) REFERENCES lockers (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orders (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_at   TIMESTAMP NOT NULL,
    delivered_at TIMESTAMP NOT NULL,
    user_id      BIGINT NOT NULL,
    parcel_id    BIGINT NOT NULL,
    FOREIGN KEY (parcel_id) REFERENCES parcels (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
