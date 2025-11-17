package com.matsvei.photosapp.home;

import com.matsvei.photosapp.album.Album;
import com.matsvei.photosapp.photo.Photo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user in the Photos application.
 * Each user has a username, password, and a collection of albums.
 * 
 * @author matsvei
 */
public class User implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private String username;
    private String password;

    public List<Album> albums = new  ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Photo> getAllPhotos() {
        Set<Photo> photos = new HashSet<>();

        for (Album album : albums) {
            photos.addAll(album.getPhotos());
        }

        return photos;
    }


    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }

    public void removeAlbum(Album album) {
        if (albums.contains(album)) {
            albums.remove(album);
        }
    }
}
