package dao;

import model.Venue;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    public void addVenue(Venue venue) throws SQLException {
        String sql = "INSERT INTO Venues (name, address, capacity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getAddress());
            stmt.setInt(3, venue.getCapacity());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venue.setVenueId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Venue getVenueById(int venueId) throws SQLException {
        String sql = "SELECT * FROM Venues WHERE venue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venueId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Venue(
                            rs.getInt("venue_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity")
                    );
                }
            }
        }
        return null;
    }

    public void updateVenue(Venue venue) throws SQLException {
        String sql = "UPDATE Venues SET name = ?, address = ?, capacity = ? WHERE venue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, venue.getName());
            stmt.setString(2, venue.getAddress());
            stmt.setInt(3, venue.getCapacity());
            stmt.setInt(4, venue.getVenueId());
            stmt.executeUpdate();
        }
    }

    public void deleteVenue(int venueId) throws SQLException {
        String sql = "DELETE FROM Venues WHERE venue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venueId);
            stmt.executeUpdate();
        }
    }

    public List<Venue> getAllVenues() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM Venues";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                venues.add(new Venue(
                        rs.getInt("venue_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity")
                ));
            }
        }
        return venues;
    }
}

