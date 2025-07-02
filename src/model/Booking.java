package model;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int userId;
    private int eventId;
    private int seatId;
    private LocalDateTime bookingTime;
    private double totalPrice;
    private String paymentStatus;
    private String confirmationCode;

    public Booking(int bookingId, int userId, int eventId, int seatId, LocalDateTime bookingTime, double totalPrice, String paymentStatus, String confirmationCode) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.seatId = seatId;
        this.bookingTime = bookingTime;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.confirmationCode = confirmationCode;
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}

