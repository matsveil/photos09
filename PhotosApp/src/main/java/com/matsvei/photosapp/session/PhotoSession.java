package com.matsvei.photosapp.session;

import com.matsvei.photosapp.photo.Photo;

/**
 * Session manager for storing the currently selected photo.
 * 
 * @author matsvei
 */
public class PhotoSession {
    private static Photo currentPhoto;

    public static void set(Photo photo) {
        currentPhoto = photo;
    }

    public static Photo get() {
        return currentPhoto;
    }
}
