package com.emz.pathfinder.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emz.pathfinder.Adapters.TimelineAdapter;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TimelineFragment extends Fragment{

    //TODO: Loadmore
    //TODO: SwipetoRefresh won't work

    private static final String TAG = "TimelineFragment";

    private ArrayList<Posts> postsList;
    private HashMap<Integer, Users> usersList;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private static TimelineAdapter mAdapter;
    private Utils utils;
    private UserHelper usrHelper;

    protected Handler handler;

    private boolean profile = false;
    private int uid;

    public TimelineFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        if(getArguments() != null){
            profile = true;
            uid = getArguments().getInt("id");
            Log.d(TAG, "UID: "+uid);
        }

        usrHelper = new UserHelper(getContext());

        utils = new Utils(this.getContext());

        usersList = new HashMap<>();
        postsList = new ArrayList<>();

        handler = new Handler();

        mSwipeRefreshLayout = rootView.findViewById(R.id.feed_main_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        mRecyclerView = rootView.findViewById(R.id.timelineRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    void refreshItems(){
        if(profile){
            loadProfileTimeline(0, 10);
        }else{
            loadTimeline(0, 10);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadUser();
    }

    public void createLike(final int position, String id, String pid, int type) {
        final String TAG = "LikeMethod";

        Velocity.post(utils.TIMELINE_URL+"like")
                .withFormData("id", pid)
                .withFormData("uid", id)
                .withFormData("type", String.valueOf(type))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);
                        if(response.body.equals("SuccessAdd")){
                            Log.d(TAG, "Doing New Like");
                            postsList.get(position).setLikeStatus(true);
                            postsList.get(position).setLikeCount(postsList.get(position).getLikeCount() + 1);
                            mAdapter.notifyItemChanged(position);
                        }else if(response.body.equals("SuccessDelete")){
                            Log.d(TAG, "Doing Delete Like");
                            postsList.get(position).setLikeStatus(false);
                            postsList.get(position).setLikeCount(postsList.get(position).getLikeCount() - 1);
                            mAdapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                    }
                });
    }

    private void loadUser() {
        Velocity.post(utils.UTILITIES_URL+"getAllProfiles")
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

                        if(profile){
                            loadProfileTimeline(0, 10);
                        }else{
                            loadTimeline(0, 10);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    private void loadProfileTimeline(int offset, int limit){
        Log.d(TAG, "TIMELINELOAD: PROFILE");
        if(offset == 0){
            if(postsList.size() > 0){
                postsList.clear();
            }
        }
        Velocity.post(utils.UTILITIES_URL+"getProfileTimeline")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("pid", String.valueOf(uid))
                .withFormData("offset", String.valueOf(offset))
                .withFormData("limit", String.valueOf(limit))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);

                        if(response.body != ""){
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for(int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Posts posts = gson.fromJson(mJson, Posts.class);
                                postsList.add(posts);
                            }

                            Log.d(TAG, "POST LOADDED");

                            int size = postsList.size();
                            Log.d(TAG, "POSTSIZE: "+String.valueOf(size));

                            if(mRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                setAdapter();
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                mAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }else{
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    private void loadTimeline(int offset, int limit){
        Log.d(TAG, "TIMELINELOAD: TIMELINE");
        if(offset == 0){
            if(postsList.size() > 0){
                postsList.clear();
            }
        }
        Velocity.post(utils.UTILITIES_URL+"getpost")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("offset", String.valueOf(offset))
                .withFormData("limit", String.valueOf(limit))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);

                        if(response.body != ""){
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for(int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Posts posts = gson.fromJson(mJson, Posts.class);
                                postsList.add(posts);
                            }

                            Log.d(TAG, "POST LOADDED");

                            int size = postsList.size();
                            Log.d(TAG, "POSTSIZE: "+String.valueOf(size));

                            if(mRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                setAdapter();
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                mAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }else{
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    private void setAdapter() {
        mAdapter = new TimelineAdapter(getContext(), usersList, postsList, mRecyclerView, TimelineFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(postsList.size() > 0){
                            int start = postsList.get(postsList.size() - 1).getId();
                            int end = start + 10;

                            Log.d(TAG, "POST End"+end);

                            if(profile){
                                loadProfileTimeline(start, end);
                            }else{
                                loadTimeline(start, end);
                            }

                            mAdapter.setLoaded();
                        }
                    }
                }, 2000);
            }
        });
    }

    public static void updateList() {
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
}
