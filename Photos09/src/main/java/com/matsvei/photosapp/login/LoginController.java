package com.matsvei.photosapp.login;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.home.User;
import com.matsvei.photosapp.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.io.IOException;

/**
 * Controller for the login view.
 * Handles user authentication and registration.
 * 
 * @author matsvei
 */
public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @FXML
    public Button loginButton;

    @FXML
    public Button signupButton;

    private void navigateToHome(User user) throws IOException {
        UserSession.set(user);
        NavigationService.navigate("/com/matsvei/photosapp/home.fxml");
    }

    public void onSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate username
        if (username.isEmpty()) {
            showError("Username cannot be empty.");
            return;
        }

        // Check if username is "admin"
        if ("admin".equalsIgnoreCase(username)) {
            showError("Cannot create a user with username 'admin'.");
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            showError("Password cannot be empty.");
            return;
        }

        // Check if user already exists
        if (DataStore.getAllUsers().containsKey(username.toLowerCase())) {
            showError("User '" + username + "' already exists. Please login instead.");
            return;
        }

        // Create new user
        User user = new User(username, password);
        DataStore.addUser(user);

        try {
            navigateToHome(user);
        } catch (Exception e) {
            showError("Failed to navigate: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check for admin login
        if ("admin".equalsIgnoreCase(username.trim())) {
            // Admin login (password is optional but if provided should be "admin")
            if (password.isEmpty() || "admin".equals(password)) {
                try {
                    UserSession.set(new User("admin", "admin"));
                    NavigationService.navigate("/com/matsvei/photosapp/admin.fxml");
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to navigate: " + e.getMessage());
                    alert.showAndWait();
                }
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid admin password.");
                alert.showAndWait();
                return;
            }
        }

        // Regular user login
        User user = DataStore.getUser(username, password);

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        } else {
            try {
                navigateToHome(user);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to navigate: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
