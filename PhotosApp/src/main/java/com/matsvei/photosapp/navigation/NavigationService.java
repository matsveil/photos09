package com.matsvei.photosapp.navigation;

/**
 * Service for handling navigation between different views in the application.
 * 
 * @author matsvei
 */
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class NavigationService {
    private static Scene scene;

    public static void initialize(Scene mainScene) {
        scene = mainScene;
    }

    public static void navigate(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlPath));
        Parent root = loader.load();
        scene.setRoot(root);

        scene.getStylesheets().clear();

        if (fxmlPath.contains("home")) {
            scene.getStylesheets().add(NavigationService.class.getResource("/com/matsvei/photosapp/home.css").toExternalForm());
        }
    }
}
