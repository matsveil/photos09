package com.matsvei.photosapp.photo;
import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.navigation.NavigationService;
import com.matsvei.photosapp.session.AlbumSession;
import com.matsvei.photosapp.session.PhotoSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PhotoController {
    private Photo photo;

    @FXML private ImageView photoImageView;
    @FXML private Label captionLabel;
    @FXML private Label dateLabel;
    @FXML private FlowPane tagPane;

    @FXML
    private void initialize() {
        tagPane.getChildren().clear();

        this.photo = PhotoSession.get();

        Image image = new Image(new File(photo.getFilePath()).toURI().toString());
        photoImageView.setImage(image);

        captionLabel.setText(photo.getCaption());

        formatDate();

        photo.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.getName());
            tagPane.getChildren().add(tagLabel);
        });
    }

    private void formatDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm");
        dateLabel.setText(photo.getDate().format(fmt));
    }

    @FXML
    private void onBack() {
        try {
            NavigationService.navigate("/com/matsvei/photosapp/album.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onNext() {
        try {
            Album currentAlbum = AlbumSession.get();
            int index = currentAlbum.getPhotos().indexOf(photo);

            if (index < currentAlbum.getPhotos().size() - 1) {
                PhotoSession.set(currentAlbum.getPhotos().get(index + 1));
                NavigationService.navigate("/com/matsvei/photosapp/photo.fxml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onPrevious() {
        try {
            Album currentAlbum = AlbumSession.get();
            int index = currentAlbum.getPhotos().indexOf(photo);

            if (index > 0) {
                PhotoSession.set(currentAlbum.getPhotos().get(index - 1));
                NavigationService.navigate("/com/matsvei/photosapp/photo.fxml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onEditCaption() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Caption");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new caption:");
        dialog.getEditor().setText(photo.getCaption());
        dialog.showAndWait().ifPresent(caption -> {
            photo.setCaption(caption);
            captionLabel.setText(caption);
            DataStore.save();
        });
    }

    public void onDeleteTag(ActionEvent actionEvent) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", photo.getTags().stream().map(Tag::getName).toArray(String[]::new));
        dialog.setTitle("Delete Tag");
        dialog.setHeaderText(null);
        dialog.setContentText("Select tag to delete:");
        dialog.showAndWait().ifPresent(tagName -> {
            photo.removeTag(new Tag(tagName));
            DataStore.save();

            initialize();
        });
    }

    public void onAddTag() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new tag:");
        dialog.showAndWait().ifPresent(tagName -> {
            photo.addTag(new Tag(tagName));
            DataStore.save();

            initialize();
        });
    }
}
