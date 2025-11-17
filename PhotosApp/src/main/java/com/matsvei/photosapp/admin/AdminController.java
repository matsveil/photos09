package com.matsvei.photosapp.admin;

import com.matsvei.photosapp.home.User;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the admin subsystem.
 * Allows admin to list, create, and delete users.
 * 
 * @author matsvei
 */
public class AdminController {
    @FXML
    private ListView<String> userListView;

    private ObservableList<String> userList = FXCollections.observableArrayList();

    /**
     * Initializes the admin controller and loads the user list.
     */
    @FXML
    public void initialize() {
        userListView.setItems(userList);
        refreshUserList();
    }

    /**
     * Refreshes the user list display.
     */
    private void refreshUserList() {
        userList.clear();
        DataStore.getAllUsers().values().forEach(user -> {
            if (!"admin".equalsIgnoreCase(user.getUsername())) {
                userList.add(user.getUsername() + " (password: " + user.getPassword() + ")");
            }
        });
    }

    /**
     * Creates a new user.
     */
    @FXML
    private void onCreateUser() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Create New User");
        usernameDialog.setHeaderText(null);
        usernameDialog.setContentText("Enter username:");

        Optional<String> usernameResult = usernameDialog.showAndWait();
        usernameResult.ifPresent(username -> {
            if (username.trim().isEmpty()) {
                showError("Username cannot be empty.");
                return;
            }

            // Check if user already exists
            if (DataStore.getAllUsers().containsKey(username.toLowerCase())) {
                showError("User '" + username + "' already exists.");
                return;
            }

            // Check if username is "admin"
            if ("admin".equalsIgnoreCase(username.trim())) {
                showError("Cannot create a user with username 'admin'.");
                return;
            }

            // Get password
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Create New User");
            passwordDialog.setHeaderText(null);
            passwordDialog.setContentText("Enter password for " + username + ":");

            Optional<String> passwordResult = passwordDialog.showAndWait();
            passwordResult.ifPresent(password -> {
                if (password.trim().isEmpty()) {
                    showError("Password cannot be empty.");
                    return;
                }

                // Create new user
                User newUser = new User(username.trim(), password.trim());
                DataStore.addUser(newUser);
                refreshUserList();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User '" + username + "' created successfully.");
                alert.showAndWait();
            });
        });
    }

    /**
     * Deletes the selected user.
     */
    @FXML
    private void onDeleteUser() {
        String selectedItem = userListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("Please select a user to delete.");
            return;
        }

        // Extract username from "username (password: ...)" format
        String selectedUsername = selectedItem.split(" \\(password:")[0];

        // Confirm deletion
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete user '" + selectedUsername + "'?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataStore.deleteUser(selectedUsername);
            refreshUserList();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User '" + selectedUsername + "' deleted successfully.");
            alert.showAndWait();
        }
    }

    /**
     * Logs out the admin and returns to the login screen.
     */
    @FXML
    private void onLogout() {
        UserSession.set(null);

        try {
            NavigationService.navigate("/com/matsvei/photosapp/login.fxml");
            DataStore.save();
        } catch (IOException e) {
            showError("Error during logout: " + e.getMessage());
        }
    }

    /**
     * Displays an error message to the user.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

