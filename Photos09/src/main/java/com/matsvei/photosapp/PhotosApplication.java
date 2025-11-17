package com.matsvei.photosapp;

import com.matsvei.photosapp.login.DataStore;
import com.matsvei.photosapp.navigation.NavigationService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX Application class for the Photos application.
 * Handles initialization and launching of the application.
 * 
 * @author matsvei
 */
public class PhotosApplication extends Application {
    /**
     * Starts the JavaFX application.
     * Loads user data and displays the login screen.
     * 
     * @param stage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        DataStore.load();

        FXMLLoader fxmlLoader = new FXMLLoader(PhotosApplication.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        NavigationService.initialize(scene);
        stage.setTitle("Photos");
        stage.setScene(scene);
        
        // Save data when window is closed
        stage.setOnCloseRequest(event -> {
            DataStore.save();
        });
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
