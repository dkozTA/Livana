package com.example.myapplication.ui.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LargeMapDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_map);

        Bundle mapViewBundle = savedInstanceState != null ?
                savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY) : new Bundle();

        mapView = findViewById(R.id.largeMap);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        TextView name = findViewById(R.id.houseName);
        name.setText(getIntent().getStringExtra("name"));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        showDestination();
    }

    private void showDestination() {
        String locationName = getIntent().getStringExtra("location");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.addMarker(new MarkerOptions().position(location).title(locationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
            } else {
                Toast.makeText(this, "Không tìm thấy vị trí đích", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tìm tọa độ đích", Toast.LENGTH_SHORT).show();
        }
    }
}