package dao;

import model.Booking;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO Bookings (user_id, event_id, seat_id, booking_time, total_price, payment_status, confirmation_code) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getEventId());
            stmt.setInt(3, booking.getSeatId());
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getBookingTime()));
            stmt.setDouble(5, booking.getTotalPrice());
            stmt.setString(6, booking.getPaymentStatus());
            stmt.setString(7, booking.getConfirmationCode());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("user_id"),
                            rs.getInt("event_id"),
                            rs.getInt("seat_id"),
                            rs.getTimestamp("booking_time").toLocalDateTime(),
                            rs.getDouble("total_price"),
                            rs.getString("payment_status"),
                            rs.getString("confirmation_code")
                    );
                }
            }
        }
        return null;
    }

    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE Bookings SET user_id = ?, event_id = ?, seat_id = ?, booking_time = ?, total_price = ?, payment_status = ?, confirmation_code = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getEventId());
            stmt.setInt(3, booking.getSeatId());
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getBookingTime()));
            stmt.setDouble(5, booking.getTotalPrice());
            stmt.setString(6, booking.getPaymentStatus());
            stmt.setString(7, booking.getConfirmationCode());
            stmt.setInt(8, booking.getBookingId());
            stmt.executeUpdate();
        }
    }

    public void deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM Bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getInt("event_id"),
                        rs.getInt("seat_id"),
                        rs.getTimestamp("booking_time").toLocalDateTime(),
                        rs.getDouble("total_price"),
                        rs.getString("payment_status"),
                        rs.getString("confirmation_code")
                ));
            }
        }
        return bookings;
    }
}

