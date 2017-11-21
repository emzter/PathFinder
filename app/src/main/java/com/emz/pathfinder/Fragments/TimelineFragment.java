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

public class TimelineFragment extends Fragment{
    private static final String TAG = "TimelineFragment";

    public TimelineFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        return rootView;
    }
}
