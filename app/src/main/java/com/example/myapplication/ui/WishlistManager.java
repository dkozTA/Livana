package com.example.myapplication.ui;

import java.util.ArrayList;
import java.util.List;

public class WishlistManager {
    private static WishlistManager instance;
    private List<WishlistFolder> folders;
    private WishlistFolder defaultFolder;

    private WishlistManager() {
        folders = new ArrayList<>();
        defaultFolder = new WishlistFolder("All Wishlists");
        folders.add(defaultFolder);
    }

    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    public void createFolder(String name) {
        folders.add(new WishlistFolder(name));
    }

    public void togglePost(Post post) {
        if (isPostSaved(post)) {
            defaultFolder.removePost(post);
        } else {
            defaultFolder.addPost(post);
        }
    }

    public boolean isPostSaved(Post post) {
        return defaultFolder.getPosts().contains(post);
    }

    public List<WishlistFolder> getFolders() {
        return folders;
    }
}