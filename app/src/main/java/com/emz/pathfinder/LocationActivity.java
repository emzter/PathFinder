package com.emz.pathfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.emz.pathfinder.Models.Locations;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private String extra;
    private Utils utils;

    private UserHelper usrHelper;

    private View mainView;

    double longitude, latitude;

    LocationManager lm;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mainView = findViewById(R.id.map_view);

        usrHelper = new UserHelper(this);
        utils = new Utils(this);

        FloatingActionButton fab = findViewById(R.id.sosfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabSOSClicked();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        if (getIntent().getExtras() != null) {
            extra = getIntent().getExtras().getString("sender_id");
        } else {

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkpermission();
                return;
            }else{
                markMap();
            }
        }
    }

    private void onFabSOSClicked() {
        Velocity.post(utils.UTILITIES_URL + "saveLocation/true/")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("lat", String.valueOf(latitude))
                .withFormData("lng", String.valueOf(longitude))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(Objects.equals(response.body, "Success")){
                            createSnackbar(mainView, "Success");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        createSnackbar(mainView, getString(R.string.no_internet_connection));
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void markMap() {
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    markMap();
                } else {
                    createSnackbar(mainView, "Permissions are needed for this function.");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkpermission() {
        ActivityCompat.requestPermissions(LocationActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
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
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                        }

                        @Override
                        public void onVelocityFailed(Velocity.Response response) {
                            View view = findViewById(R.id.map);
                            createSnackbar(view, getString(R.string.no_internet_connection));
                        }
                    });
        }else{
            mMap = googleMap;
            LatLng current = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(current).title("You're here."));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        }
    }
}
