package com.emz.pathfinder;

import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.emz.pathfinder.Models.Locations;
import com.emz.pathfinder.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rw.velocity.Velocity;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private String extra;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        utils = new Utils(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        if (getIntent().getExtras() != null) {
            extra = getIntent().getExtras().getString("sender_id");
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (getIntent().getExtras() != null) {
            Velocity.post(utils.UTILITIES_URL + "getLocation")
                    .withFormData("id", extra)
                    .connect(new Velocity.ResponseListener() {
                        @Override
                        public void onVelocitySuccess(Velocity.Response response) {
                            Locations loc = response.deserialize(Locations.class);
                            mMap = googleMap;
                            LatLng current = new LatLng(loc.getLat(), loc.getLng());
                            mMap.addMarker(new MarkerOptions().position(current).title(getString(R.string.relative_position_text)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                        }

                        @Override
                        public void onVelocityFailed(Velocity.Response response) {
                            View view = findViewById(R.id.map);
                            createSnackbar(view, getString(R.string.no_internet_connection));
                        }
                    });
        }
    }
}
