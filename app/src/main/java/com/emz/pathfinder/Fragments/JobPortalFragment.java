package com.emz.pathfinder.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emz.pathfinder.Adapters.FeaturedEmpAdapter;
import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class JobPortalFragment extends Fragment{
    private static final String TAG = "JobPortalFragment";

    private LinkedHashMap<Integer, Employer> empList;
    private List<Employer> featuredEmpList;
    private List<Jobs> jobList;

    private RecyclerView jobRecyclerView, empRecyclerView;
    private FeaturedJobAdapter jobAdapter;
    private FeaturedEmpAdapter empAdapter;

    private Utils utils;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserHelper usrHelper;

    public JobPortalFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jobportal, container, false);

        utils = new Utils(getContext());
        empList = new LinkedHashMap<>();
        jobList = new ArrayList<>();
        featuredEmpList = new ArrayList<>();

        usrHelper = new UserHelper(getContext());

        mSwipeRefreshLayout = rootView.findViewById(R.id.job_portal_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        jobRecyclerView = rootView.findViewById(R.id.catRecyclerView);
        jobRecyclerView.setHasFixedSize(true);

        empRecyclerView = rootView.findViewById(R.id.empRecyclerView);
        empRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager jobLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        RecyclerView.LayoutManager empLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        jobRecyclerView.setLayoutManager(jobLayoutManager);
        empRecyclerView.setLayoutManager(empLayoutManager);

        return rootView;
    }

    public void setFavoriteJob(final int position, int jid){
        final String TAG = "FavoriteMethod";

        Velocity.post(utils.UTILITIES_URL+"saveJob/"+jid)
                .withFormData("uid", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, "FAV: "+response.body);
                        Log.d(TAG, "FAVLINK: "+response.requestUrl);

                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObject.get("success").getAsBoolean();

                        if(status){
                            boolean add = jsonObject.get("add").getAsBoolean();
                            if(add){
                                jobList.get(position).setFavorite(true);
                                jobAdapter.notifyItemChanged(position);
                                Log.d(TAG, "SUCCESS ADD");
                            }else{
                                jobList.get(position).setFavorite(false);
                                jobAdapter.notifyItemChanged(position);
                                Log.d(TAG, "SUCCESS DELETE");
                            }

                        }else{
                            Log.e(TAG, "ERROR FAVORITES");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "ERROR FAVORITES CAN'T CONNECT");
                    }
                });

    }

    private void refreshItems() {
        loadAllEmp();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadAllEmp();
    }

    private void loadFeaturedJobs(){
        if(jobList.size() > 0){
            jobList.clear();
        }

        Velocity.post(utils.UTILITIES_URL+"getJobDetail")
                .withFormData("uid", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if (response.body != "") {
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Jobs job = gson.fromJson(mJson, Jobs.class);
                                jobList.add(job);
                            }

                            if(jobRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                jobAdapter = new FeaturedJobAdapter(getContext(), jobList, empList, JobPortalFragment.this);
                                jobRecyclerView.setAdapter(jobAdapter);
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                jobAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }else{
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadAllEmp(){
        if(empList.size() > 0){
            empList.clear();
        }

        if(featuredEmpList.size() > 0){
            featuredEmpList.clear();
        }

        Velocity.get(utils.JOBS_URL)
                .withPathParam("status","loadallemp")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if (response.body != "") {
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Employer employer = gson.fromJson(mJson, Employer.class);
                                empList.put(employer.getId(), employer);
                                featuredEmpList.add(employer);
                            }

                            if(empRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                empAdapter = new FeaturedEmpAdapter(getContext(), featuredEmpList);
                                empRecyclerView.setAdapter(empAdapter);
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                empAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            loadFeaturedJobs();

                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }
}
