package com.matsvei.photosapp.album;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.photo.Photo;
import com.matsvei.photosapp.session.AlbumSession;
import com.matsvei.photosapp.session.PhotoSession;
import com.matsvei.photosapp.session.UserSession;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;

/**
 * Controller for the album view.
 * Displays photos in an album and allows adding, removing, copying, and moving photos.
 * 
 * @author matsvei
 */
public class AlbumController {
    public Button backButton;
    public Button openPhotoButton;
    private Album album;
    private Photo selectedPhoto;

    @FXML
    public void initialize() {
        this.album = AlbumSession.get();
        this.albumName.setText(album.getName());
        displayPhotos();
    }

    @FXML
    private Label albumName;

    @FXML
    private TilePane photoTilePane;

    @FXML
    public void onAddPhoto(javafx.event.ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");

        // Optional: restrict to photos
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            album.addPhoto(new Photo(file.getAbsolutePath()));
            displayPhotos();
            DataStore.save();
        }
    }

    private void displayPhotos() {
        photoTilePane.getChildren().clear();

        for (Photo photo : album.getPhotos()) {
            File file = new File(photo.getFilePath());
            Image image = new Image(file.toURI().toString(), 120, 120, true, true);
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(120);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(false);

            Rectangle clip = new Rectangle(120, 120);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);

            StackPane container = new StackPane(imageView);
            container.setStyle("-fx-background-radius: 20; -fx-padding: 0;");

            container.setOnMouseClicked(event -> {
                selectedPhoto = photo;
                highlightSelected(container);
            });

            photoTilePane.getChildren().add(container);
        }
    }

    private void highlightSelected(StackPane selectedContainer) {
        for (Node node : photoTilePane.getChildren()) {
            node.setStyle("");
        }
        selectedContainer.setStyle(
                "-fx-border-color: #1E90FF; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 12; " +
                        "-fx-padding: 0;"
        );
    }

    @FXML
    private void onOpenPhoto(ActionEvent event) {
        if (selectedPhoto == null) {
            showAlert("No Photo Selected", "Please select a photo to open.");
            return;
        }
        
        try {
            PhotoSession.set(selectedPhoto);
            NavigationService.navigate("/com/matsvei/photosapp/photo.fxml");
        } catch (IOException e) {
            showAlert("Error", "Failed to open photo: " + e.getMessage());
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            NavigationService.navigate("/com/matsvei/photosapp/home.fxml");
        } catch (IOException e) {
            showAlert("Error", "Failed to navigate back: " + e.getMessage());
        }
    }

    @FXML
    public void onRemovePhoto(ActionEvent event) {
        if (selectedPhoto == null) {
            showAlert("No photo selected", "Please select a photo to remove.");
            return;
        }
        
        album.removePhoto(selectedPhoto);
        displayPhotos();
        selectedPhoto = null;
        DataStore.save();
    }

    @FXML
    public void onCopy(ActionEvent event) {
        handleCopyOrMove(false);
    }

    @FXML
    public void onMove(ActionEvent event) {
        handleCopyOrMove(true);
    }

    private void handleCopyOrMove(boolean move) {
        if (selectedPhoto == null) {
            showAlert("No photo selected", "Please select a photo first.");
            return;
        }

        List<Album> targetAlbums = UserSession.get().albums.stream()
                .filter(a -> a != album)
                .toList();

        if (targetAlbums.isEmpty()) {
            showAlert("No other albums", "There are no other albums to " + (move ? "move" : "copy") + " to.");
            return;
        }

        ChoiceDialog<Album> dialog = new ChoiceDialog<>(targetAlbums.getFirst(), targetAlbums);
        dialog.setTitle(move ? "Move Photo" : "Copy Photo");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose target album:");

        Optional<Album> result = dialog.showAndWait();
        result.ifPresent(targetAlbum -> {
            if (move) {
                album.movePhoto(selectedPhoto, album, targetAlbum);
            } else {
                album.copyPhoto(selectedPhoto, targetAlbum);
            }
            DataStore.save();
            if (move) {
                // refresh the current album view after moving
                displayPhotos();
                selectedPhoto = null;
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
