package com.matsvei.photosapp.photo;

import com.matsvei.photosapp.album.Album;
import javafx.scene.Node;

import java.io.File;
import java.io.Serial;
import java.time.Instant;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a photo in the Photos application.
 * Each photo has a file path, date, caption, and a collection of tags.
 * 
 * @author matsvei
 */
public class Photo extends Node implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String filePath;
    private LocalDateTime date;
    private String caption;
    private List<Tag> tags;

    public Photo(String filePath) {
        this.filePath = filePath;
        this.date = getModifiedDate();
        this.tags = new ArrayList<>();
    }

    private LocalDateTime getModifiedDate() {
        File file = new File(filePath);
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(file.lastModified()),
                ZoneId.systemDefault()
        );
    }

    public List<Tag> getTags() {
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(Tag tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo p)) return false;
        return filePath.equalsIgnoreCase(p.filePath);
    }

    @Override
    public int hashCode() {
        return filePath.toLowerCase().hashCode();
    }

}
