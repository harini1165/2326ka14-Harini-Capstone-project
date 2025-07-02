package service;

import dao.BookingDAO;
import dao.SeatDAO;
import model.Booking;
import model.Seat;
import util.BookingException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private BookingDAO bookingDAO;
    private SeatDAO seatDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.seatDAO = new SeatDAO();
    }

    public Booking createBooking(int userId, int eventId, int seatId, double totalPrice) throws BookingException {
        try {
            Seat seat = seatDAO.getSeatById(seatId);
            if (seat == null || !seat.isAvailable()) {
                throw new BookingException("Seat is not available or does not exist.");
            }

            // Simulate payment
            boolean paymentSuccessful = simulatePayment(totalPrice);
            String paymentStatus = paymentSuccessful ? "Paid" : "Failed";

            if (!paymentSuccessful) {
                throw new BookingException("Payment failed for booking.");
            }

            String confirmationCode = generateConfirmationCode();
            Booking newBooking = new Booking(0, userId, eventId, seatId, LocalDateTime.now(), totalPrice, paymentStatus, confirmationCode);
            bookingDAO.addBooking(newBooking);

            // Mark seat as unavailable
            seat.setAvailable(false);
            seatDAO.updateSeat(seat);

            System.out.println("Booking created successfully with confirmation code: " + confirmationCode);
            return newBooking;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            throw new BookingException("Database error during booking creation: " + e.getMessage());
        }
    }

    public Booking getBookingById(int bookingId) {
        try {
            return bookingDAO.getBookingById(bookingId);
        } catch (SQLException e) {
            System.err.println("Error retrieving booking by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Booking> getAllBookings() {
        try {
            return bookingDAO.getAllBookings();
        } catch (SQLException e) {
            System.err.println("Error retrieving all bookings: " + e.getMessage());
            return null;
        }
    }

    public void cancelBooking(int bookingId) throws BookingException {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                throw new BookingException("Booking not found.");
            }

            // Mark seat as available again
            Seat seat = seatDAO.getSeatById(booking.getSeatId());
            if (seat != null) {
                seat.setAvailable(true);
                seatDAO.updateSeat(seat);
            }

            bookingDAO.deleteBooking(bookingId);
            System.out.println("Booking cancelled successfully: " + bookingId);
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            throw new BookingException("Database error during booking cancellation: " + e.getMessage());
        }
    }

    private boolean simulatePayment(double amount) {
        // In a real system, this would integrate with a payment gateway
        System.out.println("Simulating payment for amount: " + amount);
        return true; // Always successful for simulation
    }

    private String generateConfirmationCode() {
        return UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}

