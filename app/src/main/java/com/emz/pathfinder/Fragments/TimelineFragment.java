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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimelineFragment extends Fragment{
    private static final String TAG = "TimelineFragment";

    private List<Posts> postsList;
    private HashMap<Integer, Users> usersList;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TimelineAdapter mAdapter;
    private Utils utils;
    private UserHelper usrHelper;

    protected Handler handler;

    public TimelineFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        usrHelper = new UserHelper(getContext());

        utils = new Utils(this.getContext());

        usersList = new HashMap<>();

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
        loadTimeline(0, 5);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadUser();
    }

    public void like(final int position, String id, String pid, String type) {
        final String TAG = "LikeMethod";

        Velocity.post(utils.TIMELINE_URL+"like")
                .withFormData("id", pid)
                .withFormData("uid", id)
                .withFormData("type", type)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);
                        if(response.body.equals("SuccessAdd")){
                            Log.d(TAG, "Doing New Like");
                            mAdapter.notifyItemChanged(position);
                        }else if(response.body.equals("SuccessDelete")){
                            Log.d(TAG, "Doing Delete Like");
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

                        loadTimeline(0, 5);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    private void loadTimeline(int offset, int limit){
        if(offset == 0){
            postsList = new ArrayList<>();
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
                                setAdapter();
                            }else{
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
        mAdapter = new TimelineAdapter(getContext(), usersList, postsList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                postsList.add(null);
//
//                mAdapter.notifyItemInserted(postsList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        postsList.remove(postsList.size() - 1);
//                        mAdapter.notifyItemRemoved(postsList.size());

                        if(postsList.size() > 0){
                            int start = postsList.get(postsList.size() - 1).getId();

                            int size = postsList.size();
                            Log.d(TAG, "POSTSIZE: "+postsList.get(postsList.size() - 1));

                            Log.d(TAG, "POST Start"+start);

                            int end = start + 10;

                            Log.d(TAG, "POST End"+end);

                            loadTimeline(start, end);
                            mAdapter.setLoaded();
                        }
                    }
                }, 2000);
            }
        });
    }
}
