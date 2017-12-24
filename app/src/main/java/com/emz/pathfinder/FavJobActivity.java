package com.emz.pathfinder;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FavJobActivity extends AppCompatActivity {

    private static final String TAG = FavJobActivity.class.getSimpleName();
    private LinkedHashMap<Integer, Employer> empList;
    private List<Jobs> jobList;

    private RecyclerView mRecyclerView;
    private FeaturedJobAdapter mAdapter;

    private Utils utils;
    private UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_job);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        empList = new LinkedHashMap<>();
        jobList = new ArrayList<>();

        bindView();

        loadAllEmp();
    }

    private void loadAllEmp() {
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
                            }

                            loadAllJob();

                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadAllJob() {
        Velocity.get(utils.JOBS_URL+"getFavJobs/"+usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, "onVelocitySuccess: "+response.requestUrl);
                        if (response.body != "") {
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Jobs job = gson.fromJson(mJson, Jobs.class);
                                jobList.add(job);
                            }

                            if(mRecyclerView.getAdapter() == null){
                                setupAdapter();
                            }else{
                                mAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO
                    }
                });
    }

    private void setupAdapter() {
        mAdapter = new FeaturedJobAdapter(this, jobList, empList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void bindView() {
        mRecyclerView = findViewById(R.id.jobRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
