package com.example.myapplication.ui.misc;

import java.util.ArrayList;
import java.util.List;

public class WishlistManager {
    private static WishlistManager instance;
    private List<WishlistFolder> folders;
    private WishlistFolder defaultFolder;

    private WishlistManager() {
        folders = new ArrayList<>();
        defaultFolder = new WishlistFolder("Recently viewed");
        folders.add(defaultFolder);
    }

    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    // Thêm vào "Recently viewed"
    public void addToRecentlyViewed(Post post) {
        if (!defaultFolder.getPosts().contains(post)) {
            defaultFolder.addPost(post);
        }
    }

    // Xóa post khỏi tất cả wishlist (trừ "Recently viewed")
    public void removePostFromWishlists(Post post) {
        for (WishlistFolder folder : folders) {
            if (!folder.getName().equals("Recently viewed")) {
                folder.removePost(post);
            }
        }
    }

    // Kiểm tra post có trong folder wishlist nào không (trừ "Recently viewed")
    public boolean isPostInAnyWishlist(Post post) {
        for (WishlistFolder folder : folders) {
            if (!folder.getName().equals("Recently viewed") && folder.getPosts().contains(post)) {
                return true;
            }
        }
        return false;
    }

    // Tạo folder mới, trả về true nếu tạo thành công, false nếu trùng tên
    public boolean createFolder(String name) {
        folders.add(new WishlistFolder(name));
        return true;
    }

    // Di chuyển post sang 1 folder cụ thể (chỉ 1 folder duy nhất)
    public void movePostToFolder(Post post, String folderName) {
        removePostFromWishlists(post); // Xóa khỏi các folder khác
        for (WishlistFolder folder : folders) {
            if (folder.getName().equals(folderName)) {
                folder.addPost(post);
                break;
            }
        }
    }

    // Toggle trạng thái post trong folder "Recently viewed"
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
