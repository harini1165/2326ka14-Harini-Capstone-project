package service;

import dao.UserDAO;
import model.User;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerUser(String username, String password, String email, String fullName, String phoneNumber) {
        try {
            // In a real application, hash the password before storing
            String passwordHash = password; // Placeholder for actual hashing
            User newUser = new User(0, username, passwordHash, email, fullName, phoneNumber, false);
            userDAO.addUser(newUser);
            System.out.println("User registered successfully: " + username);
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String username, String password) {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null && user.getPasswordHash().equals(password)) { // Placeholder for password verification
                System.out.println("User logged in successfully: " + username);
                return user;
            } else {
                System.out.println("Invalid username or password for: " + username);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error logging in user: " + e.getMessage());
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            return userDAO.getUserByUsername(username);
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
            return null;
        }
    }

    public void updateUserProfile(User user) {
        try {
            userDAO.updateUser(user);
            System.out.println("User profile updated successfully for: " + user.getUsername());
        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
        }
    }
}

