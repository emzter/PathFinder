package com.emz.pathfinder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Locations;
import com.emz.pathfinder.Models.Volunteer;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private String extra;
    private Utils utils;

    private UserHelper usrHelper;

    private View mainView;

    private LocationManager lm;
    private Location location;
    private static Double longitude;
    private static Double latitude;
    private LinkedHashMap<Integer, Marker> volMarker;
    private ScheduledExecutorService executorService;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        volMarker = new LinkedHashMap<>();

        mainView = findViewById(R.id.map_view);

        usrHelper = new UserHelper(this);
        utils = new Utils(this);

        FloatingActionButton gpsFixed = findViewById(R.id.goto_user_fab);
        gpsFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUser();
            }
        });

        if (getIntent().getExtras() != null) {
            extra = getIntent().getExtras().getString("sender_id");
        } else {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            markMap();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void goToUser() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 16.0f));
    }

    private void markMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
            return;
        }

        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            LocationListener ln = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ln);
        }
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

    private void checkPermission() {
        ActivityCompat.requestPermissions(LocationActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
    }

    private void getInRangeVolunteer(){
        Velocity.get(utils.VOLUNTEER_URL+"getVolunteerLocation")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        List<Volunteer> volList = new ArrayList<>();

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Volunteer volunteer = gson.fromJson(mJson, Volunteer.class);
                            volList.add(volunteer);
                        }

                        markVolunteer(volList);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }

    private boolean isInBound(LatLng anotherLocation){
        float[] dist = new float[1];
        double anotherLat = anotherLocation.latitude;
        double anotherLng = anotherLocation.longitude;

        Location.distanceBetween(latitude, longitude, anotherLat, anotherLng, dist);

        if(dist[0]/1000 > 1){
            return false;
        }else{
            return true;
        }
    }

    private void markVolunteer(List<Volunteer> volList) {
        for (Volunteer vol : volList) {
            LatLng latlng = new LatLng(vol.getLat(), vol.getLng());
            Marker currentMarker = volMarker.get(vol.getId());
            if(vol.isOnline()){
                if(isInBound(latlng)){
                    if(currentMarker != null){
                        currentMarker.setPosition(latlng);
                    }else{
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(vol.getFirstName()+" "+vol.getLastName()));
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                        volMarker.put(vol.getId(), marker);
                    }
                }else{
                    if(currentMarker != null){
                        currentMarker.remove();
                        volMarker.remove(vol.getId());
                    }
                }
            }else{
                if(currentMarker != null){
                    currentMarker.remove();
                    volMarker.remove(vol.getId());
                }
            }
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
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
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
            myMarker = mMap.addMarker(new MarkerOptions().position(current).title("You're here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

            getInRangeVolunteer();
            scheduleRandomUpdates();
        }
    }

    private void scheduleRandomUpdates() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                (LocationActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        markMap();
                        getInRangeVolunteer();
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
