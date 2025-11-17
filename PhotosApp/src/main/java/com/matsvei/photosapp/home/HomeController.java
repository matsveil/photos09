package com.matsvei.photosapp.home;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.session.AlbumSession;
import com.matsvei.photosapp.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import java.io.IOException;  
import java.util.Optional;  

/**
 * Controller for the home view.
 * Displays user's albums and allows creating, deleting, renaming, and opening albums.
 * 
 * @author matsvei
 */
public class HomeController {
    public Button logoutButton;

    private User user;

    private Album selectedAlbum;

    @FXML
    public TilePane albumTilePane;

    @FXML
    private Label welcomeText;
    
    @FXML
    private Label albumCountLabel;

    @FXML
    public void initialize() {
        user = UserSession.get();

        if (user == null) {
            return;
        }

        welcomeText.setText("Welcome, " + user.getUsername() + "!");
        refreshAlbumTiles();
    }

    private void refreshAlbumTiles() {
        albumTilePane.getChildren().clear();
        
        // Update album count
        int albumCount = user.albums.size();
        if (albumCountLabel != null) {
            albumCountLabel.setText(albumCount + (albumCount == 1 ? " album" : " albums"));
        }

        for (Album album : user.albums) {
            // Create album tile with name, photo count, and date range
            String tileText = album.getName() + "\n" + 
                             album.getPhotoCount() + " photo(s)\n" +
                             album.getDateRange();
            
            Button tile = new Button(tileText);
            tile.setPrefSize(180, 140);
            tile.setWrapText(true);
            tile.getStyleClass().add("album-tile");

            tile.setOnAction(e -> {
                selectedAlbum = album;

                albumTilePane.getChildren().forEach(node -> node.getStyleClass().remove("album-tile-selected"));

                tile.getStyleClass().add("album-tile-selected");
            });

            albumTilePane.getChildren().add(tile);
        }
    }

    @FXML
    private void onCreateAlbum(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter album name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showError("Album name cannot be empty.");
                return;
            }

            // Check for duplicate album names
            boolean nameExists = user.getAlbums().stream()
                                    .anyMatch(album -> album.getName().equalsIgnoreCase(name.trim()));

            if (nameExists) {
                showError("An album with this name already exists.");
            } else {
                Album newAlbum = new Album(name.trim());

                user.addAlbum(newAlbum);
                refreshAlbumTiles();
                DataStore.save();
            }
        });
    }

    @FXML
    private void onDeleteAlbum(ActionEvent event) {

        if (selectedAlbum == null) {
            showError("Please select an album to delete.");
            return;
        }

        user.removeAlbum(selectedAlbum);
        refreshAlbumTiles();
        selectedAlbum = null;
        DataStore.save();
    }

    @FXML
    private void onRenameAlbum(ActionEvent event) {
        if (selectedAlbum == null) {
            showError("Please select an album to rename.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedAlbum.getName());
        dialog.setTitle("Rename Album");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (newName.trim().isEmpty()) {
                showError("Album name cannot be empty.");
                return;
            }

            // Check if new name is the same as old name
            if (newName.trim().equalsIgnoreCase(selectedAlbum.getName())) {
                return; // Nothing to do
            }

            // Check for duplicate album names
            boolean nameExists = user.getAlbums().stream()
                    .anyMatch(album -> album.getName().equalsIgnoreCase(newName.trim()));

            if (nameExists) {
                showError("An album with this name already exists.");
            } else {
                selectedAlbum.setName(newName.trim());
                int index = user.albums.indexOf(selectedAlbum);
                user.albums.set(index, selectedAlbum);
                refreshAlbumTiles();
                DataStore.save();
            }
        });
    }

    @FXML
    private void onOpenAlbum(ActionEvent event) {
        if (selectedAlbum == null) {
            showError("Please select an album to open.");
            return;
        }
        try {
            AlbumSession.set(selectedAlbum);
            NavigationService.navigate("/com/matsvei/photosapp/album.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onLogout() {
        UserSession.set(null);
        user = null;

        try {
            NavigationService.navigate("/com/matsvei/photosapp/login.fxml");
            DataStore.save();
        } catch (IOException e) {
            showError("Error during logout: " + e.getMessage());
        }
    }

    public void onSearch() {
        try {
            NavigationService.navigate("/com/matsvei/photosapp/search.fxml");
            DataStore.save();
        } catch (IOException e) {
            showError("Error navigating to search: " + e.getMessage());
        }
    }
}