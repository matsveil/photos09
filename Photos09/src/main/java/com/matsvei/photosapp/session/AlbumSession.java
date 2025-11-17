package com.matsvei.photosapp.session;
import com.matsvei.photosapp.album.Album;

/**
 * Session manager for storing the currently selected album.
 * 
 * @author matsvei
 */
public class AlbumSession {
    private static Album currentAlbum;

    public static void set(Album album) {
        currentAlbum = album;
    }

    public static Album get() {
        return currentAlbum;
    }
}
