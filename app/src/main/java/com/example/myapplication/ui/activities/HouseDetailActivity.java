package com.example.myapplication.ui.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.Amenities;
import com.example.myapplication.data.Model.Property.AmenityStatus;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.example.myapplication.ui.misc.Amenity;
import com.example.myapplication.ui.misc.Post;
import com.example.myapplication.ui.misc.WishlistManager;
import com.example.myapplication.utils.DialogUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HouseDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageButton heartButton;
    private Post post;

    private MapView miniMap;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    //doi mau 
    private boolean isTopBarWhite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);
        getWindow().setBackgroundDrawableResource(android.R.color.white);

        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        ImageButton shareButton = findViewById(R.id.btnShare);
        shareButton.setOnClickListener(v -> sharePost());

        ScrollView scrollView = findViewById(R.id.scrollView);
        ImageView postImage = findViewById(R.id.post_image);
        ConstraintLayout topBar = findViewById(R.id.top_button_bar);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        miniMap = findViewById(R.id.miniMapView);
        miniMap.onCreate(mapViewBundle);
        miniMap.getMapAsync(this);

        //Set clickable cho mapView mở Large Map
        View mapClick = findViewById(R.id.mapClick);
        mapClick.setClickable(true);
        mapClick.setOnClickListener(v -> {
            Intent intent = new Intent(HouseDetailActivity.this, LargeMapDetailActivity.class);
            intent.putExtra("location", post.getLocation());
            intent.putExtra("name", post.getTitle());
            startActivity(intent);
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            int imageHeight = postImage.getHeight();

            if (scrollY > imageHeight - 100 && !isTopBarWhite) {
                isTopBarWhite = true;
                animateBackgroundColor(topBar, 0x00FFFFFF, 0xFFFFFFFF); // transparent → white
            } else if (scrollY <= imageHeight - 100 && isTopBarWhite) {
                isTopBarWhite = false;
                animateBackgroundColor(topBar, 0xFFFFFFFF, 0x00FFFFFF); // white → transparent
            }
        });


        post = getIntent().getParcelableExtra("post");
        if (post != null) {
            TextView title = findViewById(R.id.title);
            ImageView postImageView = findViewById(R.id.post_image);
            TextView location = findViewById(R.id.location);
            TextView detail = findViewById(R.id.details);
            TextView dateRange = findViewById(R.id.date_range);
            TextView price = findViewById(R.id.price);
            TextView avg_ratings = findViewById(R.id.avg_ratings);
            TextView total_reviews = findViewById(R.id.total_reviews);
            TextView house_rule = findViewById(R.id.house_rule);
            TextView special_feature = findViewById(R.id.special_feature);
            showAmenities(post);
            heartButton = findViewById(R.id.heart_button);

            title.setText(post.getTitle());
            Glide.with(this)
                    .load(post.getImageResId()) // hoặc getImageResId() nếu dùng R.drawable
                    .placeholder(R.drawable.photo1) // tùy chọn
                    .error(R.drawable.photo1) // tùy chọn
                    .into(postImageView);
            location.setText(post.getLocation());
            detail.setText(post.getDetail());
            dateRange.setText(post.getDateRange());
            price.setText(post.getPrice());
            avg_ratings.setText(post.getAvgRatings() + " ⭐ ");
            total_reviews.setText(post.getTotalReview() + " đánh giá");
            if (post.getAmenities() != null && post.getAmenities().houseRules != null) {
                house_rule.setText(post.getAmenities().houseRules);
            } else {
                house_rule.setText("Không có nội quy"); // hoặc để trống
            }

            if (post.getAmenities() != null && post.getAmenities().more != null) {
                special_feature.setText(post.getAmenities().more);
            } else {
                special_feature.setText("Không có"); // hoặc để trống
            }

            updateHeartIcon();

            heartButton.setOnClickListener(v -> handleHeartClick());
        }
    }

    private void animateBackgroundColor(View view, int startColor, int endColor) {
        ObjectAnimator colorAnim = ObjectAnimator.ofArgb(view, "backgroundColor", startColor, endColor);
        colorAnim.setDuration(300); // thời gian đổi màu (ms)
        colorAnim.start();
    }

    private void handleHeartClick() {
        boolean isInWishlist = WishlistManager.getInstance().isPostInAnyWishlist(post);

        if (!isInWishlist) {
            DialogUtils.showCreateWishlistDialog(this, post, this::updateHeartIcon);
        } else {
            WishlistManager.getInstance().removePostFromWishlists(post);
            updateHeartIcon();
        }
    }

    private void updateHeartIcon() {
        boolean isInWishlist = WishlistManager.getInstance().isPostInAnyWishlist(post);
        heartButton.setImageResource(isInWishlist ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
    }

    private void sharePost() {
        if (post == null) return;

        String shareText = "Check out this house!\n"
                + post.getTitle() + "\n"
                + post.getLocation() + "\n"
                + "Price: " + post.getPrice();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "House Listing");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void showAmenities(Post post) {
        LinearLayout amenityContainer = findViewById(R.id.amenity_list); // LinearLayout trong layout chính

        Amenities a = post.getAmenities();

        List<Amenity> amenityList = Arrays.asList(
                new Amenity("TV", R.drawable.ic_bed, a.tv),
                new Amenity("Wi-Fi", R.drawable.ic_bed, a.wifi),
                new Amenity("Thú cưng", R.drawable.ic_bed, a.petAllowance),
                new Amenity("Hồ bơi", R.drawable.ic_bed, a.pool),
                new Amenity("Máy giặt", R.drawable.ic_bed, a.washingMachine),
                new Amenity("Bữa sáng", R.drawable.ic_bed, a.breakfast),
                new Amenity("Máy lạnh", R.drawable.ic_bed, a.airConditioner),
                new Amenity("BBQ", R.drawable.ic_bed, a.bbq)
        );

        for (Amenity amenity : amenityList) {
            if (amenity.status == AmenityStatus.Hidden) continue;

            View view = LayoutInflater.from(this).inflate(R.layout.amenity_item, amenityContainer, false);
            ImageView icon = view.findViewById(R.id.amenity_icon);
            TextView name = view.findViewById(R.id.amenity_name);

            icon.setImageResource(amenity.iconResId);
            name.setText(amenity.name);

            if (amenity.status == AmenityStatus.Unavailable) {
                name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                name.setTextColor(Color.GRAY);
                icon.setColorFilter(Color.GRAY);
            }

            amenityContainer.addView(view);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        showDestination();

        // Tắt tất cả tương tác người dùng
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    private void showDestination() {
        String locationName = post.getLocation();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.addMarker(new MarkerOptions().position(location).title(locationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            } else {
                Toast.makeText(this, "Không tìm thấy vị trí đích", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tìm tọa độ đích", Toast.LENGTH_SHORT).show();
        }
    }
}
