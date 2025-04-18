package com.example.myapplication.ui.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.data.Model.Property.Amenities;
import com.example.myapplication.data.Model.Property.AmenityStatus;
import com.example.myapplication.ui.adapters.PostImageAdapter;
import com.example.myapplication.ui.misc.Amenity;
import com.example.myapplication.ui.misc.Post;
import com.example.myapplication.ui.misc.WishlistManager;
import com.example.myapplication.utils.DialogUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HouseDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageButton heartButton;
    private Post post;

    //doi mau 
    private boolean isTopBarWhite = false;

    private MapView miniMap;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";




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
        ViewPager2 postImage = findViewById(R.id.viewPagerImages);
        ConstraintLayout topBar = findViewById(R.id.top_button_bar);

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


        post = getIntent().getParcelableExtra("post");
        if (post != null) {
            TextView title = findViewById(R.id.title);
            //ImageView postImageView = findViewById(R.id.post_image);
            ViewPager2 viewPager = findViewById(R.id.viewPagerImages);

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
            List<String> allImages = post.getSub_photos();
            if (allImages == null) allImages = new ArrayList<>();
            if (!allImages.contains(post.getImageResId())) {
                allImages.add(0, post.getImageResId()); // chèn ảnh chính vào đầu
            }

            PostImageAdapter adapter = new PostImageAdapter(this, allImages, post, false);
            viewPager.setAdapter(adapter);

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
                new Amenity("TV", R.drawable.ic_tv, a.tv),
                new Amenity("Wi-Fi", R.drawable.ic_wifi, a.wifi),
                new Amenity("Thú cưng", R.drawable.ic_pets, a.petAllowance),
                new Amenity("Hồ bơi", R.drawable.ic_pool, a.pool),
                new Amenity("Máy giặt", R.drawable.ic_bed, a.washingMachine),
                new Amenity("Bữa sáng", R.drawable.ic_free_breakfast, a.breakfast),
                new Amenity("Máy lạnh", R.drawable.ic_airconditioner, a.airConditioner),
                new Amenity("BBQ", R.drawable.ic_outdoor_grill, a.bbq)
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




    //MapView setup
    @Override
    protected void onResume() {
        super.onResume();
        miniMap.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        miniMap.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        miniMap.onStop();
    }

    @Override
    protected void onPause() {
        miniMap.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        miniMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        miniMap.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        miniMap.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        showDestination();

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            } else {
                Toast.makeText(this, "Không tìm thấy vị trí đích", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tìm tọa độ đích", Toast.LENGTH_SHORT).show();
        }
    }
}
