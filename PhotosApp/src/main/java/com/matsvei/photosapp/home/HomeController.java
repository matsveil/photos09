package com.matsvei.photosapp.home;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.session.AlbumSession;
import com.matsvei.photosapp.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import java.io.IOException;  
import java.util.Optional;  

public class HomeController {
    public Button logoutButton;

    private User user;

    private Album selectedAlbum;

    @FXML
    public TilePane albumTilePane;

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        user = UserSession.get();

        if (user == null) {
            System.out.println("NO USER IN SESSION — this means login didn’t set it.");
            return;
        }

        welcomeText.setText("Welcome, " + user.getUsername() + "!");
        refreshAlbumTiles();
    }

    private void refreshAlbumTiles() {
        albumTilePane.getChildren().clear();

        for (Album album : user.albums) {
            Button tile = new Button(album.getName());
            tile.setPrefSize(100, 100);
            tile.setWrapText(true);
            tile.getStyleClass().add("album-tile");

            tile.setOnAction(e -> {
                selectedAlbum = album;

                albumTilePane.getChildren().forEach(node -> node.getStyleClass().remove("album-tile-selected"));

                tile.getStyleClass().add("album-tile-selected");

                System.out.println("Selected album: " + album.getName());
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
        } catch (IOException e) { // <-- Catch IOException specifically
            e.printStackTrace();
        }
    }
}