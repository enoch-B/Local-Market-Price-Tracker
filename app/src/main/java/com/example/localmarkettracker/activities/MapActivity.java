package com.example.localmarkettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.localmarkettracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = FirebaseFirestore.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

// In MapActivity.java

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if a specific location was passed from MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MARKET_LAT")) {
            double lat = intent.getDoubleExtra("MARKET_LAT", 0);
            double lng = intent.getDoubleExtra("MARKET_LNG", 0);
            String name = intent.getStringExtra("MARKET_NAME");

            LatLng marketLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(marketLocation).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marketLocation, 15f)); // Zoom in closer
        } else {
            // If no specific location, show the default view with all markers
            LatLng addisAbaba = new LatLng(9.03, 38.74);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addisAbaba, 12));
            loadMarketMarkers(); // Your existing method to load all markers
        }
    }

    private void loadMarketMarkers() {
        db.collection("markets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                // Check if latitude and longitude fields exist and are valid numbers
                if (doc.contains("latitude") && doc.contains("longitude")) {
                    try {
                        double lat = doc.getDouble("latitude");
                        double lng = doc.getDouble("longitude");
                        String marketName = doc.getString("name");
                        String marketLocation = doc.getString("location");

                        // Create a LatLng object for the marker
                        LatLng marketLatLng = new LatLng(lat, lng);

                        // Add a marker to the map
                        mMap.addMarker(new MarkerOptions()
                                .position(marketLatLng)
                                .title(marketName)
                                .snippet(marketLocation)); // Snippet is the text below the title

                    } catch (Exception e) {
                        // This might happen if lat/lng are stored as strings.
                        // Handle gracefully, maybe log the error.
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // Handle the error, e.g., show a Toast
        });
    }
}
    