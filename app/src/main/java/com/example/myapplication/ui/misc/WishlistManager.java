package com.example.myapplication.ui.misc;

import java.util.ArrayList;
import java.util.List;

public class WishlistManager {
    private static WishlistManager instance;
    private List<WishlistFolder> folders;
    private WishlistFolder recentViewedFolder;

    private WishlistFolder interestedFolder;



    private WishlistManager() {
        folders = new ArrayList<>();
        recentViewedFolder = new WishlistFolder("Đã xem gần đây");
        interestedFolder = new WishlistFolder("Yêu thích");
        folders.add(recentViewedFolder);
        folders.add(interestedFolder);
    }

    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    // Thêm vào "Recently viewed"
    public void addToRecentlyViewed(Post post) {
        if (recentViewedFolder.getPosts().contains(post)) {
            recentViewedFolder.removePost(post);
        }
        recentViewedFolder.getPosts().add(0, post); // luôn thêm lên đầu
    }


    public void removeFromRecentlyView(Post post) {
        if (recentViewedFolder.getPosts().contains(post)) {
            recentViewedFolder.removePost(post);
        }
    }


    // Kiểm tra post có trong folder wishlist nào không (trừ "Recently viewed")
    public boolean isPostInRecentlyWishlist(Post post) {
        return recentViewedFolder.getPosts().contains(post);
    }

    // Thêm vào 'Mục yêu thích"
    public void addToInterestedView(Post post) {
        interestedFolder.getPosts().add(0, post);
    }

    // Xóa khỏi folder "Yêu thích"
    public void removeFromInterestedView(Post post) {
        if (interestedFolder.getPosts().contains(post)) {
            interestedFolder.removePost(post);
        }
    }


    // Kiểm tra post có trong folder wishlist nào không (trừ "Recently viewed")
    public boolean isPostInInterestedWishlist(Post post) {
        return interestedFolder.getPosts().contains(post);
    }

    public List<WishlistFolder> getFolders() {
        return folders;
    }
}
