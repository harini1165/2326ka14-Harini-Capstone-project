package dao;

import model.Seat;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public void addSeat(Seat seat) throws SQLException {
        String sql = "INSERT INTO Seats (venue_id, seat_row, seat_number, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, seat.getVenueId());
            stmt.setString(2, seat.getSeatRow());
            stmt.setString(3, seat.getSeatNumber());
            stmt.setBoolean(4, seat.isAvailable());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    seat.setSeatId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Seat getSeatById(int seatId) throws SQLException {
        String sql = "SELECT * FROM Seats WHERE seat_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("venue_id"),
                            rs.getString("seat_row"),
                            rs.getString("seat_number"),
                            rs.getBoolean("is_available")
                    );
                }
            }
        }
        return null;
    }

    public List<Seat> getSeatsByVenueId(int venueId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM Seats WHERE venue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venueId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("venue_id"),
                            rs.getString("seat_row"),
                            rs.getString("seat_number"),
                            rs.getBoolean("is_available")
                    ));
                }
            }
        }
        return seats;
    }

    public void updateSeat(Seat seat) throws SQLException {
        String sql = "UPDATE Seats SET venue_id = ?, seat_row = ?, seat_number = ?, is_available = ? WHERE seat_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seat.getVenueId());
            stmt.setString(2, seat.getSeatRow());
            stmt.setString(3, seat.getSeatNumber());
            stmt.setBoolean(4, seat.isAvailable());
            stmt.setInt(5, seat.getSeatId());
            stmt.executeUpdate();
        }
    }

    public void deleteSeat(int seatId) throws SQLException {
        String sql = "DELETE FROM Seats WHERE seat_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatId);
            stmt.executeUpdate();
        }
    }

    public List<Seat> getAllSeats() throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM Seats";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seats.add(new Seat(
                        rs.getInt("seat_id"),
                        rs.getInt("venue_id"),
                        rs.getString("seat_row"),
                        rs.getString("seat_number"),
                        rs.getBoolean("is_available")
                ));
            }
        }
        return seats;
    }
}

