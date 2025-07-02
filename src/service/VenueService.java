package service;

import dao.VenueDAO;
import model.Venue;
import java.sql.SQLException;
import java.util.List;

public class VenueService {
    private VenueDAO venueDAO;

    public VenueService() {
        this.venueDAO = new VenueDAO();
    }

    public boolean addVenue(String name, String address, int capacity) {
        try {
            Venue newVenue = new Venue(0, name, address, capacity);
            venueDAO.addVenue(newVenue);
            System.out.println("Venue added successfully: " + name);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding venue: " + e.getMessage());
            return false;
        }
    }

    public Venue getVenueById(int venueId) {
        try {
            return venueDAO.getVenueById(venueId);
        } catch (SQLException e) {
            System.err.println("Error retrieving venue by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Venue> getAllVenues() {
        try {
            return venueDAO.getAllVenues();
        } catch (SQLException e) {
            System.err.println("Error retrieving all venues: " + e.getMessage());
            return null;
        }
    }

    public void updateVenue(Venue venue) {
        try {
            venueDAO.updateVenue(venue);
            System.out.println("Venue updated successfully: " + venue.getName());
        } catch (SQLException e) {
            System.err.println("Error updating venue: " + e.getMessage());
        }
    }

    public void deleteVenue(int venueId) {
        try {
            venueDAO.deleteVenue(venueId);
            System.out.println("Venue deleted successfully: " + venueId);
        } catch (SQLException e) {
            System.err.println("Error deleting venue: " + e.getMessage());
        }
    }
}

