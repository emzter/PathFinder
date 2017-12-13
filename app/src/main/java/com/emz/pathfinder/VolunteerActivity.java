package com.emz.pathfinder;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.emz.pathfinder.Models.Volunteer;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class VolunteerActivity extends AppCompatActivity implements OnMapReadyCallback, OnLocationUpdatedListener {

    private static final String TAG = VolunteerActivity.class.getSimpleName();

    private GoogleMap mMap;

    private Utils utils;
    private UserHelper usrHelper;

    private static Double longitude;
    private static Double latitude;
    private LinkedHashMap<Integer, Marker> volMarker;
    private Marker myMarker;
    private ScheduledExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        ButterKnife.bind(this);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        volMarker = new LinkedHashMap<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SmartLocation.with(this).location().state().locationServicesEnabled()){
            Log.d(TAG, "LocationService: Found");
            SmartLocation.with(this)
                    .location()
                    .config(LocationParams.NAVIGATION)
                    .start(this);
        }else{
            Log.e(TAG, "LocationService: None");
            onLocationServiceUnavailable();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SmartLocation.with(this)
                .location()
                .stop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationUpdated(Location location) {
        markMap();
    }

    @OnClick(R.id.goto_user_fab)
    public void goToUser() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 16.0f));
    }

    private void markMap() {
        Location location = SmartLocation.with(this).location().getLastLocation();
        latitude = location != null ? location.getLatitude() : 0;
        longitude = location != null ? location.getLongitude() : 0;
        LatLng current = new LatLng(latitude, longitude);
        Log.d(TAG, "CURRENTLOCATION: "+current);
        if(myMarker != null){
            myMarker.setPosition(current);
        }else{
            myMarker = mMap.addMarker(new MarkerOptions().position(current));
            myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mymarkersmall));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15.5f));

        if(executorService == null){
            Log.d(TAG, "LOCATIONUPDATE: 1");
            scheduleRandomUpdates();
        }
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

    private void markVolunteer(List<Volunteer> volList) {
        for (Volunteer vol : volList) {
            LatLng latlng = new LatLng(vol.getLat(), vol.getLng());
            Marker currentMarker = volMarker.get(vol.getId());
            if(vol.isOnline()){
                if(isInBound(latlng)){
                    if(currentMarker != null){
                        currentMarker.setPosition(latlng);
                    }else{
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng));
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker));
                        marker.setRotation(5);
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

    private boolean isInBound(LatLng anotherLocation){
        float[] dist = new float[1];
        double anotherLat = anotherLocation.latitude;
        double anotherLng = anotherLocation.longitude;

        Log.d(TAG, "ANOTHERLOCATION: "+anotherLocation);

        Location.distanceBetween(latitude, longitude, anotherLat, anotherLng, dist);

        return !(dist[0] / 1000 > 2);
    }

    private void scheduleRandomUpdates() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                (VolunteerActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getInRangeVolunteer();
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @OnClick(R.id.request_button)
    public void callVolunteer() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("CALL VOLUNTEER");
        String[] options = { "A", "B", "C" };
        dialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void onLocationServiceUnavailable() {
        new MaterialDialog.Builder(this)
                .title("Use Google's Location Services?")
                .content("Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.")
                .positiveText("Agree")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .negativeText("Disagree")
                .show();
    }
}
