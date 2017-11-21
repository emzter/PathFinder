package com.emz.pathfinder.Fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.List;

import static com.emz.pathfinder.Utils.Utils.JOBS_URL;

public class JobPortalFragment extends Fragment{
    private static final String TAG = "JobPortalFragment";

    private List<Employer> empList;
    private List<Jobs> jobList;

    private RecyclerView mRecyclerView;
    private FeaturedJobAdapter mAdapter;

    public JobPortalFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_jobportal, container, false);

        empList = new ArrayList<>();
        jobList = new ArrayList<>();

        mRecyclerView = rootView.findViewById(R.id.catRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        loadAllEmp();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadFeaturedJobs();
    }

    private void loadFeaturedJobs(){
        Velocity.get(JOBS_URL)
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

                        mAdapter = new FeaturedJobAdapter(getContext(), jobList, empList);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadAllEmp(){
        Velocity.get(JOBS_URL)
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
                            empList.add(employer);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }
}
