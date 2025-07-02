CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE Venues (
    venue_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    capacity INT NOT NULL
);

CREATE TABLE Events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATETIME NOT NULL,
    venue_id INT NOT NULL,
    ticket_price DECIMAL(10, 2) NOT NULL,
    available_tickets INT NOT NULL,
    FOREIGN KEY (venue_id) REFERENCES Venues(venue_id)
);

CREATE TABLE Seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    venue_id INT NOT NULL,
    seat_row VARCHAR(10) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    UNIQUE (venue_id, seat_row, seat_number),
    FOREIGN KEY (venue_id) REFERENCES Venues(venue_id)
);

CREATE TABLE Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    seat_id INT NOT NULL,
    booking_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    confirmation_code VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id),
    FOREIGN KEY (seat_id) REFERENCES Seats(seat_id),
    UNIQUE (event_id, seat_id) -- A seat can only be booked once per event
);

-- Sample Data

INSERT INTO Users (username, password_hash, email, full_name, is_admin) VALUES
('admin', 'admin_hash', 'admin@example.com', 'Admin User', TRUE),
('john_doe', 'john_hash', 'john.doe@example.com', 'John Doe', FALSE);

INSERT INTO Venues (name, address, capacity) VALUES
('Grand Concert Hall', '123 Music Ave, City', 2000),
('Community Theater', '456 Drama St, Town', 500);

INSERT INTO Events (title, description, event_date, venue_id, ticket_price, available_tickets) VALUES
('Symphony Night', 'A classical music concert', '2025-07-20 19:00:00', 1, 50.00, 1500),
('Local Play', 'An exciting drama performance', '2025-08-10 18:30:00', 2, 25.00, 400);

-- Sample Seats for Grand Concert Hall (Venue ID 1)
INSERT INTO Seats (venue_id, seat_row, seat_number, is_available) VALUES
(1, 'A', '1', TRUE), (1, 'A', '2', TRUE), (1, 'A', '3', TRUE),
(1, 'B', '1', TRUE), (1, 'B', '2', TRUE), (1, 'B', '3', TRUE);

-- Sample Seats for Community Theater (Venue ID 2)
INSERT INTO Seats (venue_id, seat_row, seat_number, is_available) VALUES
(2, 'AA', '1', TRUE), (2, 'AA', '2', TRUE), (2, 'AA', '3', TRUE);

-- Sample Bookings
INSERT INTO Bookings (user_id, event_id, seat_id, total_price, payment_status, confirmation_code) VALUES
(2, 1, 1, 50.00, 'Paid', 'CONF1234567890');

