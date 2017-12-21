package com.emz.pathfinder.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bumptech.glide.util.Util;
import com.emz.pathfinder.Adapters.FriendsListAdapter;
import com.emz.pathfinder.Models.Jobs;
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
import java.util.List;

public class FriendsListFragment extends Fragment {

    private Users currentUser;

    private Utils utils;
    private UserHelper usrHelper;

    private List<Users> friendsList;

    private RecyclerView mRecyclerView;

    public FriendsListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_friends_list, container, false);

        utils = new Utils(getContext());
        usrHelper = new UserHelper(getContext());

        friendsList = new ArrayList<>();

        if(getArguments() != null){
            currentUser = (Users) getArguments().getSerializable("currentUser");
        }

        bindView(rootView);

        loadFriend();

        return rootView;
    }

    private void bindView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.friendRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadFriend() {
        Velocity.post(utils.UTILITIES_URL+"getFriends/"+currentUser.getId())
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Users friend = gson.fromJson(mJson, Users.class);
                            friendsList.add(friend);
                        }

                        setupAdapter();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }

    private void setupAdapter() {
        FriendsListAdapter mAdapter = new FriendsListAdapter(getContext(), friendsList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
