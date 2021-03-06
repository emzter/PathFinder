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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

public class JobPortalFragment extends Fragment{
    private static final String TAG = "JobPortalFragment";

    private LinkedHashMap<Integer, Employer> empList;
    private List<Employer> featuredEmpList;
    private List<Jobs> jobList, recommendJobList;

    private RecyclerView jobRecyclerView, empRecyclerView, rcmJobRecyclerView;
    private FeaturedJobAdapter jobAdapter, rcmJobAdapter;
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
        recommendJobList = new ArrayList<>();
        featuredEmpList = new ArrayList<>();

        usrHelper = new UserHelper(getContext());

        mSwipeRefreshLayout = rootView.findViewById(R.id.job_portal_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        rcmJobRecyclerView = rootView.findViewById(R.id.rcmJobRecyclerView);
        rcmJobRecyclerView.setHasFixedSize(true);

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

        RecyclerView.LayoutManager rcmJobLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        RecyclerView.LayoutManager empLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        jobRecyclerView.setLayoutManager(jobLayoutManager);
        rcmJobRecyclerView.setLayoutManager(rcmJobLayoutManager);
        empRecyclerView.setLayoutManager(empLayoutManager);

        return rootView;
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

        if(recommendJobList.size() > 0){
            recommendJobList.clear();
        }

        Velocity.post(utils.UTILITIES_URL+"getJobDetail").withFormData("uid", usrHelper.getUserId()).queue(0);
        Velocity.get(utils.UTILITIES_URL+"getRecommendJob/"+usrHelper.getUserId()+"/3").queue(1);

        Velocity.executeQueue(new Velocity.MultiResponseListener() {
            @Override
            public void onVelocityMultiResponseSuccess(HashMap<Integer, Velocity.Response> hashMap) {
                for (Map.Entry<Integer, Velocity.Response> entry : hashMap.entrySet()) {
                    if(entry.getKey() == 0){
                        if (!Objects.equals(entry.getValue().body, "")) {
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(entry.getValue().body).getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Jobs job = gson.fromJson(mJson, Jobs.class);
                                jobList.add(job);
                            }

                            if(jobRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                jobAdapter = new FeaturedJobAdapter(getContext(), jobList, empList, jobRecyclerView);
                                jobRecyclerView.setAdapter(jobAdapter);
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                jobAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }else{
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }else{
                        if (!Objects.equals(entry.getValue().body, "")) {
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(entry.getValue().body).getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Jobs job = gson.fromJson(mJson, Jobs.class);
                                recommendJobList.add(job);
                            }

                            if(rcmJobRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                rcmJobAdapter = new FeaturedJobAdapter(getContext(), recommendJobList, empList, rcmJobRecyclerView);
                                rcmJobRecyclerView.setAdapter(rcmJobAdapter);
                            }else{
                                Log.d(TAG, "POST REFRESHED");
                                rcmJobAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }else{
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            }

            @Override
            public void onVelocityMultiResponseError(HashMap<Integer, Velocity.Response> hashMap) {
                for (Map.Entry<Integer, Velocity.Response> entry : hashMap.entrySet()) {
                    Log.e(TAG, "KEY"+entry.getKey().toString());
                    Log.e(TAG, "VALUE"+entry.getValue().body);
                }
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

        Velocity.get(utils.JOBS_URL+"loadallemp")
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
