package com.matsvei.photosapp.search;

import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.home.User;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.photo.Photo;
import com.matsvei.photosapp.photo.Tag;
import com.matsvei.photosapp.session.AlbumSession;
import com.matsvei.photosapp.session.PhotoSession;
import com.matsvei.photosapp.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the search functionality.
 * Allows users to search photos by date range or tag-based queries.
 * 
 * @author matsvei
 */
public class SearchController {
    private User user;
    private List<Photo> searchResults = new ArrayList<>();
    private Photo selectedPhoto;

    @FXML private RadioButton dateSearchRadio;
    @FXML private RadioButton tagSearchRadio;
    @FXML private VBox dateSearchBox;
    @FXML private VBox tagSearchBox;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private TextField tagType1Field;
    @FXML private TextField tagValue1Field;
    @FXML private ComboBox<String> operatorComboBox;
    @FXML private HBox tag2Box;
    @FXML private TextField tagType2Field;
    @FXML private TextField tagValue2Field;
    @FXML private TilePane resultsTilePane;
    @FXML private Label resultsLabel;

    /**
     * Initializes the search controller.
     */
    @FXML
    public void initialize() {
        user = UserSession.get();
        
        // Set up operator combo box
        operatorComboBox.getItems().addAll("Single Tag", "AND", "OR");
        operatorComboBox.setValue("Single Tag");
        
        // Add listener to show/hide second tag based on operator
        operatorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean showSecondTag = "AND".equals(newVal) || "OR".equals(newVal);
            tag2Box.setVisible(showSecondTag);
            tag2Box.setManaged(showSecondTag);
        });
        
        // Add listeners to toggle between date and tag search
        dateSearchRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            dateSearchBox.setVisible(newVal);
            dateSearchBox.setManaged(newVal);
            tagSearchBox.setVisible(!newVal);
            tagSearchBox.setManaged(!newVal);
        });
        
        tagSearchRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            tagSearchBox.setVisible(newVal);
            tagSearchBox.setManaged(newVal);
            dateSearchBox.setVisible(!newVal);
            dateSearchBox.setManaged(!newVal);
        });
    }

    /**
     * Performs the search based on selected criteria.
     */
    @FXML
    private void onSearch() {
        searchResults.clear();
        resultsTilePane.getChildren().clear();
        
        if (dateSearchRadio.isSelected()) {
            searchByDateRange();
        } else if (tagSearchRadio.isSelected()) {
            searchByTags();
        }
        
        displayResults();
        updateResultsLabel();
    }

    /**
     * Searches photos by date range.
     */
    private void searchByDateRange() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        
        if (fromDate == null || toDate == null) {
            showError("Please select both start and end dates.");
            return;
        }
        
        if (fromDate.isAfter(toDate)) {
            showError("Start date must be before or equal to end date.");
            return;
        }
        
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
        
        Set<Photo> allPhotos = user.getAllPhotos();
        
        for (Photo photo : allPhotos) {
            LocalDateTime photoDate = photo.getDate();
            if (!photoDate.isBefore(fromDateTime) && !photoDate.isAfter(toDateTime)) {
                searchResults.add(photo);
            }
        }
    }

    /**
     * Searches photos by tag criteria.
     */
    private void searchByTags() {
        String tagType1 = tagType1Field.getText().trim();
        String tagValue1 = tagValue1Field.getText().trim();
        String operator = operatorComboBox.getValue();
        
        if (tagType1.isEmpty() || tagValue1.isEmpty()) {
            showError("Please enter both tag type and value.");
            return;
        }
        
        Set<Photo> allPhotos = user.getAllPhotos();
        Tag tag1 = new Tag(tagType1, tagValue1);
        
        if ("Single Tag".equals(operator)) {
            // Single tag search
            for (Photo photo : allPhotos) {
                if (photo.getTags().contains(tag1)) {
                    searchResults.add(photo);
                }
            }
        } else {
            // AND or OR operation
            String tagType2 = tagType2Field.getText().trim();
            String tagValue2 = tagValue2Field.getText().trim();
            
            if (tagType2.isEmpty() || tagValue2.isEmpty()) {
                showError("Please enter both tag type and value for the second tag.");
                return;
            }
            
            Tag tag2 = new Tag(tagType2, tagValue2);
            
            if ("AND".equals(operator)) {
                // Conjunctive search - photo must have both tags
                for (Photo photo : allPhotos) {
                    if (photo.getTags().contains(tag1) && photo.getTags().contains(tag2)) {
                        searchResults.add(photo);
                    }
                }
            } else if ("OR".equals(operator)) {
                // Disjunctive search - photo must have at least one tag
                for (Photo photo : allPhotos) {
                    if (photo.getTags().contains(tag1) || photo.getTags().contains(tag2)) {
                        searchResults.add(photo);
                    }
                }
            }
        }
    }

    /**
     * Displays the search results as photo thumbnails.
     */
    private void displayResults() {
        resultsTilePane.getChildren().clear();
        
        for (Photo photo : searchResults) {
            File file = new File(photo.getFilePath());
            if (!file.exists()) {
                continue;
            }
            
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
                
                // Double-click to open photo
                if (event.getClickCount() == 2) {
                    openPhoto();
                }
            });
            
            resultsTilePane.getChildren().add(container);
        }
    }

    /**
     * Highlights the selected photo thumbnail.
     */
    private void highlightSelected(StackPane selectedContainer) {
        for (Node node : resultsTilePane.getChildren()) {
            node.setStyle("-fx-background-radius: 20; -fx-padding: 0;");
        }
        selectedContainer.setStyle(
                "-fx-border-color: #1E90FF; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 12; " +
                "-fx-padding: 0;"
        );
    }

    /**
     * Opens the selected photo in detail view.
     */
    private void openPhoto() {
        if (selectedPhoto == null) {
            showError("Please select a photo first.");
            return;
        }
        
        // Find which album contains this photo and set it as the album session
        for (Album album : user.getAlbums()) {
            if (album.getPhotos().contains(selectedPhoto)) {
                AlbumSession.set(album);
                break;
            }
        }
        
        try {
            PhotoSession.set(selectedPhoto);
            NavigationService.navigate("/com/matsvei/photosapp/photo.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the results label with the number of photos found.
     */
    private void updateResultsLabel() {
        int count = searchResults.size();
        if (count == 0) {
            resultsLabel.setText("No photos found matching the search criteria.");
        } else if (count == 1) {
            resultsLabel.setText("Found 1 photo.");
        } else {
            resultsLabel.setText("Found " + count + " photos.");
        }
    }

    /**
     * Creates a new album from the search results.
     */
    @FXML
    private void onCreateAlbum() {
        if (searchResults.isEmpty()) {
            showError("No search results to create an album from.");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album from Search Results");
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
                return;
            }
            
            // Create new album and add all search result photos
            Album newAlbum = new Album(name.trim());
            for (Photo photo : searchResults) {
                newAlbum.addPhoto(photo);
            }
            
            user.addAlbum(newAlbum);
            DataStore.save();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Album '" + name.trim() + "' created with " + searchResults.size() + " photo(s).");
            alert.showAndWait();
        });
    }

    /**
     * Clears all search fields and results.
     */
    @FXML
    private void onClear() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        tagType1Field.clear();
        tagValue1Field.clear();
        tagType2Field.clear();
        tagValue2Field.clear();
        operatorComboBox.setValue("Single Tag");
        searchResults.clear();
        resultsTilePane.getChildren().clear();
        resultsLabel.setText("No search performed yet");
        selectedPhoto = null;
    }

    /**
     * Navigates back to the home screen.
     */
    @FXML
    private void onBack() {
        try {
            NavigationService.navigate("/com/matsvei/photosapp/home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
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
