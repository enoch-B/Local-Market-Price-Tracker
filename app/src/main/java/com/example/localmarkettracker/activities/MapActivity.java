package com.example.localmarkettracker.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localmarkettracker.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.example.localmarkettracker.utils.MapUtils;

/**
 * MapActivity - shows markets on a Google Map.
 * Requires Maps API key in AndroidManifest meta-data and Play Services.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_map_view);
        SupportMapFragment frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        frag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        MapUtils.addAllMarketsToMap(map, this);
    }
}
