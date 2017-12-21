package com.emz.pathfinder.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emz.pathfinder.Adapters.NotificationsAdapter;
import com.emz.pathfinder.Adapters.TimelineAdapter;
import com.emz.pathfinder.Models.Notifications;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationFragment extends Fragment{

    //TODO: SET ONCLICK

    private static final String TAG = "NotificationFragment";
    private Utils utils;

    private HashMap<Integer, Users> usersList;
    private List<Notifications> notiLists;

    private RecyclerView mRecyclerView;
    private NotificationsAdapter mAdapter;
    private UserHelper usrHelper;

    public NotificationFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        usrHelper = new UserHelper(getContext());

        utils = new Utils(this.getContext());

        usersList = new HashMap<>();
        notiLists = new ArrayList<>();

        mRecyclerView = rootView.findViewById(R.id.notiRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadUsers();
    }

    private void loadUsers() {
        Velocity.post(utils.UTILITIES_URL+"getAllProfiles")
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Users user = gson.fromJson(mJson, Users.class);
                            usersList.put(user.getId(), user);
                        }

                        Log.d(TAG, "USERS LOADDED");

                        loadNotifications();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    private void loadNotifications() {
        Velocity.post(utils.UTILITIES_URL+"getnotis")
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Notifications noti = gson.fromJson(mJson, Notifications.class);
                            notiLists.add(noti);
                        }

                        Log.d(TAG, "NOTI LOADDED");

                        mAdapter = new NotificationsAdapter(getContext(), notiLists, usersList);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }
}
