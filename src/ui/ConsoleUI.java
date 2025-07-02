package ui;

import service.UserService;
import service.EventService;
import service.VenueService;
import service.SeatService;
import service.BookingService;
import util.BookingException;
import model.User;
import model.Event;
import model.Venue;
import model.Seat;
import model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private UserService userService;
    private EventService eventService;
    private VenueService venueService;
    private SeatService seatService;
    private BookingService bookingService;
    private Scanner scanner;
    private User currentUser;

    public ConsoleUI() {
        userService = new UserService();
        eventService = new EventService();
        venueService = new VenueService();
        seatService = new SeatService();
        bookingService = new BookingService();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Ticket Booking System!");
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Browse Events");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    browseEvents();
                    break;
                case 4:
                    adminLogin();
                    break;
                case 5:
                    System.out.println("Thank you for using the Ticket Booking System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
            System.out.print("Enter your choice: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return choice;
    }

    private void registerUser() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter phone number (optional): ");
        String phoneNumber = scanner.nextLine();

        if (userService.registerUser(username, password, email, fullName, phoneNumber)) {
            System.out.println("Registration successful. You can now log in.");
        } else {
            System.out.println("Registration failed. Username or email might already be in use.");
        }
    }

    private void loginUser() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userService.loginUser(username, password);
        if (currentUser != null) {
            System.out.println("Login successful. Welcome, " + currentUser.getFullName() + "!");
            if (currentUser.isAdmin()) {
                adminMenu();
            } else {
                userMenu();
            }
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private void adminLogin() {
        System.out.println("\n--- Admin Login ---");
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        User adminUser = userService.loginUser(username, password);
        if (adminUser != null && adminUser.isAdmin()) {
            currentUser = adminUser;
            System.out.println("Admin login successful. Welcome, " + currentUser.getFullName() + "!");
            adminMenu();
        } else {
            System.out.println("Admin login failed. Invalid credentials or not an admin user.");
        }
    }

    private void userMenu() {
        while (currentUser != null && !currentUser.isAdmin()) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Browse Events");
            System.out.println("2. View My Bookings");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    browseEvents();
                    break;
                case 2:
                    viewMyBookings();
                    break;
                case 3:
                    currentUser = null;
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void adminMenu() {
        while (currentUser != null && currentUser.isAdmin()) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Manage Events");
            System.out.println("2. Manage Venues");
            System.out.println("3. View All Bookings");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    manageEvents();
                    break;
                case 2:
                    manageVenues();
                    break;
                case 3:
                    viewAllBookings();
                    break;
                case 4:
                    currentUser = null;
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void browseEvents() {
        System.out.println("\n--- Available Events ---");
        List<Event> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available at the moment.");
            return;
        }

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            Venue venue = venueService.getVenueById(event.getVenueId());
            System.out.println((i + 1) + ". " + event.getTitle() + " on " + event.getEventDate() + " at " + (venue != null ? venue.getName() : "Unknown Venue") + " (Price: $" + event.getTicketPrice() + ", Available: " + event.getAvailableTickets() + ")");
        }

        System.out.print("Enter event number to view details and book (0 to go back): ");
        int eventChoice = getUserChoice();
        if (eventChoice > 0 && eventChoice <= events.size()) {
            viewEventDetailsAndBook(events.get(eventChoice - 1));
        }
    }

    private void viewEventDetailsAndBook(Event event) {
        System.out.println("\n--- Event Details ---");
        System.out.println("Title: " + event.getTitle());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Date: " + event.getEventDate());
        Venue venue = venueService.getVenueById(event.getVenueId());
        System.out.println("Venue: " + (venue != null ? venue.getName() : "Unknown Venue"));
        System.out.println("Ticket Price: $" + event.getTicketPrice());
        System.out.println("Available Tickets: " + event.getAvailableTickets());

        if (currentUser == null) {
            System.out.println("Please login to book tickets.");
            return;
        }

        System.out.println("\n--- Seat Selection ---");
        List<Seat> seats = seatService.getSeatsByVenueId(event.getVenueId());
        if (seats.isEmpty()) {
            System.out.println("No seats configured for this venue.");
            return;
        }

        System.out.println("Available Seats (Row-Number):");
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seat.isAvailable()) {
                System.out.print((i + 1) + ". " + seat.getSeatRow() + "-" + seat.getSeatNumber() + "  ");
            }
        }
        System.out.println();

        System.out.print("Enter seat number to book (0 to go back): ");
        int seatChoice = getUserChoice();
        if (seatChoice > 0 && seatChoice <= seats.size()) {
            Seat selectedSeat = seats.get(seatChoice - 1);
            if (selectedSeat.isAvailable()) {
                try {
                    bookingService.createBooking(currentUser.getUserId(), event.getEventId(), selectedSeat.getSeatId(), event.getTicketPrice());
                    System.out.println("Booking successful!");
                    // Refresh event details after booking
                    Event updatedEvent = eventService.getEventById(event.getEventId());
                    if (updatedEvent != null) {
                        event.setAvailableTickets(updatedEvent.getAvailableTickets());
                    }
                } catch (BookingException e) {
                    System.err.println("Booking failed: " + e.getMessage());
                }
            } else {
                System.out.println("Selected seat is not available. Please choose another.");
            }
        }
    }

    private void viewMyBookings() {
        System.out.println("\n--- My Bookings ---");
        List<Booking> allBookings = bookingService.getAllBookings();
        boolean foundBookings = false;
        for (Booking booking : allBookings) {
            if (booking.getUserId() == currentUser.getUserId()) {
                foundBookings = true;
                Event event = eventService.getEventById(booking.getEventId());
                Seat seat = seatService.getSeatById(booking.getSeatId());
                System.out.println("Booking ID: " + booking.getBookingId() + ", Event: " + (event != null ? event.getTitle() : "Unknown Event") + ", Seat: " + (seat != null ? seat.getSeatRow() + "-" + seat.getSeatNumber() : "Unknown Seat") + ", Price: $" + booking.getTotalPrice() + ", Status: " + booking.getPaymentStatus() + ", Confirmation: " + booking.getConfirmationCode());
            }
        }
        if (!foundBookings) {
            System.out.println("You have no bookings yet.");
        }
    }

    private void manageEvents() {
        System.out.println("\n--- Manage Events ---");
        while (true) {
            System.out.println("1. Add New Event");
            System.out.println("2. View All Events");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addNewEvent();
                    break;
                case 2:
                    viewAllEventsAdmin();
                    break;
                case 3:
                    updateExistingEvent();
                    break;
                case 4:
                    deleteExistingEvent();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addNewEvent() {
        System.out.println("\n--- Add New Event ---");
        System.out.print("Enter event title: ");
        String title = scanner.nextLine();
        System.out.print("Enter event description: ");
        String description = scanner.nextLine();
        System.out.print("Enter event date and time (YYYY-MM-DD HH:MM:SS): ");
        String dateTimeStr = scanner.nextLine();
        LocalDateTime eventDate = LocalDateTime.parse(dateTimeStr.replace(" ", "T")); // ISO_LOCAL_DATE_TIME format

        List<Venue> venues = venueService.getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues available. Please add a venue first.");
            return;
        }
        System.out.println("Available Venues:");
        for (int i = 0; i < venues.size(); i++) {
            System.out.println((i + 1) + ". " + venues.get(i).getName());
        }
        System.out.print("Select venue number: ");
        int venueChoice = getUserChoice();
        int venueId = venues.get(venueChoice - 1).getVenueId();

        System.out.print("Enter ticket price: ");
        double ticketPrice = scanner.nextDouble();
        System.out.print("Enter available tickets: ");
        int availableTickets = scanner.nextInt();
        scanner.nextLine(); // consume newline

        eventService.addEvent(title, description, eventDate, venueId, ticketPrice, availableTickets);
    }

    private void viewAllEventsAdmin() {
        System.out.println("\n--- All Events ---");
        List<Event> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events to display.");
            return;
        }
        for (Event event : events) {
            Venue venue = venueService.getVenueById(event.getVenueId());
            System.out.println("ID: " + event.getEventId() + ", Title: " + event.getTitle() + ", Date: " + event.getEventDate() + ", Venue: " + (venue != null ? venue.getName() : "Unknown") + ", Price: $" + event.getTicketPrice() + ", Available: " + event.getAvailableTickets());
        }
    }

    private void updateExistingEvent() {
        System.out.println("\n--- Update Event ---");
        viewAllEventsAdmin();
        System.out.print("Enter Event ID to update: ");
        int eventId = getUserChoice();
        Event eventToUpdate = eventService.getEventById(eventId);

        if (eventToUpdate == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.print("Enter new title (current: " + eventToUpdate.getTitle() + "): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) eventToUpdate.setTitle(title);

        System.out.print("Enter new description (current: " + eventToUpdate.getDescription() + "): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) eventToUpdate.setDescription(description);

        System.out.print("Enter new date and time (YYYY-MM-DD HH:MM:SS, current: " + eventToUpdate.getEventDate() + "): ");
        String dateTimeStr = scanner.nextLine();
        if (!dateTimeStr.isEmpty()) eventToUpdate.setEventDate(LocalDateTime.parse(dateTimeStr.replace(" ", "T")));

        List<Venue> venues = venueService.getAllVenues();
        if (!venues.isEmpty()) {
            System.out.println("Available Venues:");
            for (int i = 0; i < venues.size(); i++) {
                System.out.println((i + 1) + ". " + venues.get(i).getName());
            }
            System.out.print("Select new venue number (current venue ID: " + eventToUpdate.getVenueId() + "): ");
            String venueChoiceStr = scanner.nextLine();
            if (!venueChoiceStr.isEmpty()) {
                int venueChoice = Integer.parseInt(venueChoiceStr);
                eventToUpdate.setVenueId(venues.get(venueChoice - 1).getVenueId());
            }
        }

        System.out.print("Enter new ticket price (current: " + eventToUpdate.getTicketPrice() + "): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) eventToUpdate.setTicketPrice(Double.parseDouble(priceStr));

        System.out.print("Enter new available tickets (current: " + eventToUpdate.getAvailableTickets() + "): ");
        String ticketsStr = scanner.nextLine();
        if (!ticketsStr.isEmpty()) eventToUpdate.setAvailableTickets(Integer.parseInt(ticketsStr));

        eventService.updateEvent(eventToUpdate);
    }

    private void deleteExistingEvent() {
        System.out.println("\n--- Delete Event ---");
        viewAllEventsAdmin();
        System.out.print("Enter Event ID to delete: ");
        int eventId = getUserChoice();
        eventService.deleteEvent(eventId);
    }

    private void manageVenues() {
        System.out.println("\n--- Manage Venues ---");
        while (true) {
            System.out.println("1. Add New Venue");
            System.out.println("2. View All Venues");
            System.out.println("3. Update Venue");
            System.out.println("4. Delete Venue");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addNewVenue();
                    break;
                case 2:
                    viewAllVenuesAdmin();
                    break;
                case 3:
                    updateExistingVenue();
                    break;
                case 4:
                    deleteExistingVenue();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addNewVenue() {
        System.out.println("\n--- Add New Venue ---");
        System.out.print("Enter venue name: ");
        String name = scanner.nextLine();
        System.out.print("Enter venue address: ");
        String address = scanner.nextLine();
        System.out.print("Enter venue capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        venueService.addVenue(name, address, capacity);
    }

    private void viewAllVenuesAdmin() {
        System.out.println("\n--- All Venues ---");
        List<Venue> venues = venueService.getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues to display.");
            return;
        }
        for (Venue venue : venues) {
            System.out.println("ID: " + venue.getVenueId() + ", Name: " + venue.getName() + ", Address: " + venue.getAddress() + ", Capacity: " + venue.getCapacity());
        }
    }

    private void updateExistingVenue() {
        System.out.println("\n--- Update Venue ---");
        viewAllVenuesAdmin();
        System.out.print("Enter Venue ID to update: ");
        int venueId = getUserChoice();
        Venue venueToUpdate = venueService.getVenueById(venueId);

        if (venueToUpdate == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.print("Enter new name (current: " + venueToUpdate.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) venueToUpdate.setName(name);

        System.out.print("Enter new address (current: " + venueToUpdate.getAddress() + "): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) venueToUpdate.setAddress(address);

        System.out.print("Enter new capacity (current: " + venueToUpdate.getCapacity() + "): ");
        String capacityStr = scanner.nextLine();
        if (!capacityStr.isEmpty()) venueToUpdate.setCapacity(Integer.parseInt(capacityStr));

        venueService.updateVenue(venueToUpdate);
    }

    private void deleteExistingVenue() {
        System.out.println("\n--- Delete Venue ---");
        viewAllVenuesAdmin();
        System.out.print("Enter Venue ID to delete: ");
        int venueId = getUserChoice();
        venueService.deleteVenue(venueId);
    }

    private void viewAllBookings() {
        System.out.println("\n--- All Bookings ---");
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings to display.");
            return;
        }
        for (Booking booking : bookings) {
            User user = userService.getUserByUsername(String.valueOf(booking.getUserId())); // Assuming user ID can be used to get username
            Event event = eventService.getEventById(booking.getEventId());
            Seat seat = seatService.getSeatById(booking.getSeatId());
            System.out.println("Booking ID: " + booking.getBookingId() + ", User: " + (user != null ? user.getUsername() : "Unknown") + ", Event: " + (event != null ? event.getTitle() : "Unknown") + ", Seat: " + (seat != null ? seat.getSeatRow() + "-" + seat.getSeatNumber() : "Unknown") + ", Price: $" + booking.getTotalPrice() + ", Status: " + booking.getPaymentStatus() + ", Confirmation: " + booking.getConfirmationCode());
        }
    }
}

