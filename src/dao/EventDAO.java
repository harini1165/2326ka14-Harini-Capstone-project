package dao;

import model.Event;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO Events (title, description, event_date, venue_id, ticket_price, available_tickets) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getEventDate()));
            stmt.setInt(4, event.getVenueId());
            stmt.setDouble(5, event.getTicketPrice());
            stmt.setInt(6, event.getAvailableTickets());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setEventId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM Events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("event_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("event_date").toLocalDateTime(),
                            rs.getInt("venue_id"),
                            rs.getDouble("ticket_price"),
                            rs.getInt("available_tickets")
                    );
                }
            }
        }
        return null;
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE Events SET title = ?, description = ?, event_date = ?, venue_id = ?, ticket_price = ?, available_tickets = ? WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(event.getEventDate()));
            stmt.setInt(4, event.getVenueId());
            stmt.setDouble(5, event.getTicketPrice());
            stmt.setInt(6, event.getAvailableTickets());
            stmt.setInt(7, event.getEventId());
            stmt.executeUpdate();
        }
    }

    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM Events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("event_date").toLocalDateTime(),
                        rs.getInt("venue_id"),
                        rs.getDouble("ticket_price"),
                        rs.getInt("available_tickets")
                ));
            }
        }
        return events;
    }
}

