package com.matsvei.photosapp.login;

import com.matsvei.photosapp.home.User;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
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

