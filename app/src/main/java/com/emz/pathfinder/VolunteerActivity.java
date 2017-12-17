package com.emz.pathfinder;

import android.*;
import android.Manifest;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.emz.pathfinder.Models.Volunteer;
import com.emz.pathfinder.Models.VolunteerCategory;
import com.emz.pathfinder.Utils.Ui;
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
import com.google.gson.JsonObject;
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
    private List<VolunteerCategory> volCatList;
    private Marker myMarker;
    private ScheduledExecutorService executorService;
    private MaterialDialog materialDialog;

    private ScheduledExecutorService orderExcutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        ButterKnife.bind(this);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        volCatList = new ArrayList<>();
        volMarker = new LinkedHashMap<>();

        getAllCat();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
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

    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    @Override
    public void onLocationUpdated(Location location) {
        markMap();
    }

    @OnClick(R.id.goto_user_fab)
    public void goToUser() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), 15.5f));
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
                        //TODO: Can't Connect to the Server
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
        ArrayList<String> options = new ArrayList<>();
        for (VolunteerCategory vol : volCatList) {
            options.add(vol.getName());
        }
        new MaterialDialog.Builder(this)
                .title("CALL VOLUNTEER")
                .items(options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        requestVolunteer(position);
                    }
                })
                .show();
    }

    private void requestVolunteer(int position) {
        Velocity.post(utils.VOLUNTEER_URL+"requestVolunteer")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("volcat", String.valueOf(volCatList.get(position).getId()))
                .withFormData("lat", String.valueOf(latitude))
                .withFormData("lng", String.valueOf(longitude))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObj = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObj.get("status").getAsBoolean();
                        if(status){
                            final int orderId = jsonObj.get("order_id").getAsInt();
                            materialDialog = new MaterialDialog.Builder(VolunteerActivity.this)
                                    .title(R.string.progress_dialog_title)
                                    .content(R.string.requesting_a_volunteer)
                                    .progress(true, 0)
                                    .canceledOnTouchOutside(false)
                                    .autoDismiss(false)
                                    .negativeText("Cancel")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            cancelOrder(orderId);
                                        }
                                    })
                                    .show();
                            checkOrderStatus(orderId);
                        }

                        Log.d(TAG, "VOLUNTEER-REQUEST: Sent Order");
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: Can't Connect to the Server
                    }
                });
    }

    private void checkOrderStatus(final int orderId) {
//        ORDER STATUS (0: Placing, 1: Accepted, 2: On Duty, 3: Completed, 4: Canceled)
        orderExcutorService = Executors.newSingleThreadScheduledExecutor();
        orderExcutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Velocity.get(utils.VOLUNTEER_URL+"checkOrderStatus/"+orderId)
                                .connect(new Velocity.ResponseListener() {
                                    @Override
                                    public void onVelocitySuccess(Velocity.Response response) {
                                        JsonParser parser = new JsonParser();
                                        JsonObject jsonObj = parser.parse(response.body).getAsJsonObject();

                                        boolean status = jsonObj.get("status").getAsBoolean();
                                        if(status){
                                            Log.d(TAG, "VOLUNTEER-REQUEST: Order Accepted");
                                            int vid = jsonObj.get("volunteer_id").getAsInt();
                                            orderAccepted(vid);
                                            orderExcutorService.shutdown();
                                        }

                                        Log.d(TAG, "VOLUNTEER-REQUEST: Status Checking");
                                    }

                                    @Override
                                    public void onVelocityFailed(Velocity.Response response) {
                                        //TODO: Can't Connect to the Server
                                    }
                                });
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void orderAccepted(int vid) {
        //TODO: When order is accept show volunteer profile
        //TODO: Add Url
        Velocity.get(utils.VOLUNTEER_URL+"getUserDetail/"+vid)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Volunteer volunteer = response.deserialize(Volunteer.class);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: Can't Connect to the Server
                    }
                });
        materialDialog.dismiss();
    }

    private void cancelOrder(int orderId) {
        Velocity.post(utils.VOLUNTEER_URL+"setOrderStatus/"+orderId)
                .withFormData("status", String.valueOf(4))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObj = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObj.get("status").getAsBoolean();
                        if(status){
                            Log.d(TAG, "VOLUNTEER-REQUEST: Order Canceled");
                            materialDialog.dismiss();
                            orderExcutorService.shutdown();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: Can't Connect to the Server
                    }
                });
    }

    private void onLocationServiceUnavailable() {
        new MaterialDialog.Builder(this)
                .title(R.string.google_location_service_request_title)
                .content(R.string.google_location_service_request_body)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .negativeText(R.string.disagree)
                .show();
    }

    private void getAllCat() {
        Velocity.get(utils.VOLUNTEER_URL+"getAllCat")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            VolunteerCategory cat = gson.fromJson(mJson, VolunteerCategory.class);
                            volCatList.add(cat);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: Can't Connect to the Server
                    }
                });
    }
}
