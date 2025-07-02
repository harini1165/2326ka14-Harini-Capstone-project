# 🎟️ Ticket Booking System

A console-based Java application to manage users, events, venues, and ticket bookings.

---

## ✅ Requirements

- Java 8+
- Maven
- MySQL (or compatible database)
- MySQL JDBC Driver (included via Maven)

---

## 🚀 How to Run

### 1. 🔧 Set Up the Database

1. **Install MySQL** and create a database:

   ```sql
   CREATE DATABASE ticket_booking_db;
   ```

2. **Execute the schema script**:

   ```bash
   mysql -u YOUR_USERNAME -p ticket_booking_db < db/ticket_schema.sql
   ```

3. **Configure database credentials** in:

   ```
   src/util/DBConnection.java
   ```

   Update:
   ```java
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```

---

### 2. 🧱 Build the Project

From the root of the project:

```bash
mvn clean package
```

Run the app:

```bash
java -jar target/TicketBookingSystem-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 👤 Admin Credentials

- **Username**: `admin`
- **Password**: `admin_hash`

> ⚠️ Update the password in production (`admin_hash` is a placeholder).

---

## ✨ Key Features

### For Users:
- Register and login
- Browse events and venues
- Book tickets with seat selection
- View your bookings

### For Admins:
- Login to admin panel
- Add / update / delete events and venues
- View all bookings

---

## 📁 Project Structure

```
TicketBookingSystem/
├── src/
│   ├── model/       → User, Event, Venue, etc.
│   ├── dao/         → JDBC-based database access
│   ├── service/     → Business logic
│   ├── util/        → DB connection, helpers
│   ├── ui/          → Console UI
│   └── main/        → Main.java (entry point)
├── db/
│   └── ticket_schema.sql  → SQL schema & sample data
├── lib/
│   └── mysql-connector.jar (if manually used)
├── pom.xml
└── README.md
```

---

## 📌 Notes

- Console-based UI — no JavaFX/GUI required
- Must configure `DBConnection.java` before running
- Clean, modular code using Model → DAO → Service → UI pattern

---
# 2326ka14-Harini-Capstone-project
