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

import com.emz.pathfinder.Adapters.FeaturedEmpAdapter;
import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    public JobPortalFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jobportal, container, false);

        utils = new Utils(getContext());
        empList = new LinkedHashMap<>();
        jobList = new ArrayList<>();
        featuredEmpList = new ArrayList<>();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadAllEmp();
    }

    private void loadFeaturedJobs(){
        Velocity.get(utils.JOBS_URL)
                .withPathParam("status","loadfeaturedjob")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Jobs job = gson.fromJson(mJson, Jobs.class);
                            jobList.add(job);
                        }

                        jobAdapter = new FeaturedJobAdapter(getContext(), jobList, empList);
                        jobRecyclerView.setAdapter(jobAdapter);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadAllEmp(){
        Velocity.get(utils.JOBS_URL)
                .withPathParam("status","loadallemp")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Employer employer = gson.fromJson(mJson, Employer.class);
                            empList.put(employer.getId(), employer);
                            featuredEmpList.add(employer);
                        }

                        loadFeaturedJobs();

                        empAdapter = new FeaturedEmpAdapter(getContext(), featuredEmpList);
                        empRecyclerView.setAdapter(empAdapter);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }
}
