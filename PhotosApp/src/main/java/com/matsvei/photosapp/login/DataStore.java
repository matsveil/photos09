package com.matsvei.photosapp.login;

import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.home.User;
import com.matsvei.photosapp.photo.Photo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Data store for persisting user data using serialization.
 * Handles loading and saving user data to disk.
 * 
 * @author matsvei
 */
public class DataStore {
    private static final String DATA_FILE = "data/users.dat";

    private static Map<String, User> users = new HashMap<>();

    public static void load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) in.readObject();
        } catch (FileNotFoundException e) {
            users = new HashMap<>();
            initializeStockUser();
        } catch (Exception e) {
            // If data is corrupted, start fresh
            users = new HashMap<>();
            initializeStockUser();
        }
    }
    
    /**
     * Initializes the stock user with stock photos.
     * This is called on first run when no user data exists.
     */
    private static void initializeStockUser() {
        User stockUser = new User("stock", "stock");
        Album stockAlbum = new Album("stock");
        
        // Add stock photos (5-10 photos from the data directory)
        String[] stockPhotoNames = {
            "stock1.jpg",
            "stock2.jpg",
            "stock3.jpg",
            "stock4.jpg",
            "stock5.jpg"
        };
        
        // Get the absolute path to the data directory
        // This ensures stock photos work regardless of where the app runs from
        File dataFile = new File(DATA_FILE);
        File dataDir = dataFile.getAbsoluteFile().getParentFile();
        
        for (String photoName : stockPhotoNames) {
            File photoFile = new File(dataDir, photoName);
            if (photoFile.exists()) {
                // Use absolute path so photos can be found from anywhere
                stockAlbum.addPhoto(new Photo(photoFile.getAbsolutePath()));
            }
        }
        
        stockUser.addAlbum(stockAlbum);
        users.put("stock", stockUser);
        save();
    }

    public static void save() {
        java.io.File file = new java.io.File(DATA_FILE);
        java.io.File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(users);
        } catch (Exception e) {
            // Silently fail - data will be saved on next attempt
        }
    }

    public static User getUser(String username, String password) {
        User user = users.get(username.toLowerCase());

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; // incorrect username or password
    }

    public static void addUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
        save();
    }

    public static Map<String, User> getAllUsers() {
        return users;
    }

    public static void deleteUser(String username) {
        users.remove(username.toLowerCase());
        save();
    }
}

