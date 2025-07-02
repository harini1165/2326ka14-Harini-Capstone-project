package service;

import dao.EventDAO;
import model.Event;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class EventService {
    private EventDAO eventDAO;

    public EventService() {
        this.eventDAO = new EventDAO();
    }

    public boolean addEvent(String title, String description, LocalDateTime eventDate, int venueId, double ticketPrice, int availableTickets) {
        try {
            Event newEvent = new Event(0, title, description, eventDate, venueId, ticketPrice, availableTickets);
            eventDAO.addEvent(newEvent);
            System.out.println("Event added successfully: " + title);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding event: " + e.getMessage());
            return false;
        }
    }

    public Event getEventById(int eventId) {
        try {
            return eventDAO.getEventById(eventId);
        } catch (SQLException e) {
            System.err.println("Error retrieving event by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Event> getAllEvents() {
        try {
            return eventDAO.getAllEvents();
        } catch (SQLException e) {
            System.err.println("Error retrieving all events: " + e.getMessage());
            return null;
        }
    }

    public void updateEvent(Event event) {
        try {
            eventDAO.updateEvent(event);
            System.out.println("Event updated successfully: " + event.getTitle());
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
        }
    }

    public void deleteEvent(int eventId) {
        try {
            eventDAO.deleteEvent(eventId);
            System.out.println("Event deleted successfully: " + eventId);
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }
    }
}

