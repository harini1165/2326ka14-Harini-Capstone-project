package service;

import dao.SeatDAO;
import model.Seat;
import java.sql.SQLException;
import java.util.List;

public class SeatService {
    private SeatDAO seatDAO;

    public SeatService() {
        this.seatDAO = new SeatDAO();
    }

    public boolean addSeat(int venueId, String seatRow, String seatNumber, boolean isAvailable) {
        try {
            Seat newSeat = new Seat(0, venueId, seatRow, seatNumber, isAvailable);
            seatDAO.addSeat(newSeat);
            System.out.println("Seat added successfully: " + seatRow + seatNumber);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding seat: " + e.getMessage());
            return false;
        }
    }

    public Seat getSeatById(int seatId) {
        try {
            return seatDAO.getSeatById(seatId);
        } catch (SQLException e) {
            System.err.println("Error retrieving seat by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Seat> getSeatsByVenueId(int venueId) {
        try {
            return seatDAO.getSeatsByVenueId(venueId);
        } catch (SQLException e) {
            System.err.println("Error retrieving seats by venue ID: " + e.getMessage());
            return null;
        }
    }

    public void updateSeat(Seat seat) {
        try {
            seatDAO.updateSeat(seat);
            System.out.println("Seat updated successfully: " + seat.getSeatRow() + seat.getSeatNumber());
        } catch (SQLException e) {
            System.err.println("Error updating seat: " + e.getMessage());
        }
    }

    public void deleteSeat(int seatId) {
        try {
            seatDAO.deleteSeat(seatId);
            System.out.println("Seat deleted successfully: " + seatId);
        } catch (SQLException e) {
            System.err.println("Error deleting seat: " + e.getMessage());
        }
    }
}

