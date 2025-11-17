package com.matsvei.photosapp.session;

import com.matsvei.photosapp.home.User;

/**
 * Session manager for storing the currently logged-in user.
 * 
 * @author matsvei
 */
public class UserSession {
    private static User currentUser;

    public static void set(User user) {
        currentUser = user;
    }

    public static User get() {
        return currentUser;
    }
}
