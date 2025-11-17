package com.matsvei.photosapp.album;

import com.matsvei.photosapp.photo.Photo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

/**
 * Represents an album in the Photos application.
 * Each album has a name and contains a collection of photos.
 * 
 * @author matsvei
 */
public class Album implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private String name;
    private List<Photo> photos = new ArrayList<>();


    public List<Photo> getPhotos() {
        return photos;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album(String name) {
        this.name = name;
    }

    public void addPhoto(Photo photo) {
        if (!photos.contains(photo)) {
            photos.add(photo);
        }
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }

    public int getPhotoCount() {
        return photos.size();
    }

    public String getDateRange() {
        if (photos.isEmpty()) {
            return "N/A";
        }

        // Find the earliest and latest photo dates
        Optional<LocalDateTime> minDate = photos.stream()
                .map(Photo::getDate)
                .min(Comparator.naturalOrder());

        Optional<LocalDateTime> maxDate = photos.stream()
                .map(Photo::getDate)
                .max(Comparator.naturalOrder());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Format the date range string
        if (minDate.isPresent() && maxDate.isPresent()) {
            if (minDate.get().toLocalDate().equals(maxDate.get().toLocalDate())) {
                return minDate.get().format(formatter); // Only one date
            }
            return minDate.get().format(formatter) + " - " + maxDate.get().format(formatter);
        }

        return "N/A";
    }

    public void copyPhoto(Photo photo, Album album) {
        if (!album.getPhotos().contains(photo)) {
            album.addPhoto(photo);
        }
    }

    public void movePhoto(Photo photo, Album fromAlbum, Album toAlbum) {
        if (!toAlbum.getPhotos().contains(photo)) {
            toAlbum.addPhoto(photo);
            fromAlbum.removePhoto(photo);
        }
    }

    @Override
    public String toString() {
        // This will be displayed in the ListView
        // Example: "Vacation (15 photos) [01/01/2023 - 01/10/2023]"
        return String.format("%s (%d photos) [%s]",
                name,
                getPhotoCount(),
                getDateRange()
        );
    }
}
