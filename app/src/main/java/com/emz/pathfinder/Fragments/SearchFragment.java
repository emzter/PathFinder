package com.emz.pathfinder.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emz.pathfinder.Adapters.FeaturedEmpAdapter;
import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Adapters.FriendsListAdapter;
import com.emz.pathfinder.Models.Comments;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Users;
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
import java.util.LinkedHashMap;
import java.util.List;

public class SearchFragment extends Fragment{
    private static final String TAG = "SearchFragment";

    private NestedScrollView mainList;
    private LinearLayout jobSearchList, userSearchList, empSearchList;
    private RecyclerView jobRecyclerView, userRecyclerView, empRecyclerView;
    private FeaturedJobAdapter featuredJobAdapter;
    private FriendsListAdapter friendsListAdapter;
    private ProgressBar progressBar;
    private TextView resultTv;
    private EditText searchInput;

    private Utils utils;
    private UserHelper usrHelper;

    private LinkedHashMap<Integer, Employer> empList;
    private List<Jobs> jobsList;
    private List<Employer> employerList;
    private List<Users> usersList;

    public SearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        bindView(rootView);

        utils = new Utils(getContext());
        usrHelper = new UserHelper(getContext());

        empList = new LinkedHashMap<>();
        jobsList = new ArrayList<>();
        employerList = new ArrayList<>();
        usersList = new ArrayList<>();

        loadAllEmp();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                doSearch(query);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


        });

        return rootView;
    }

    private void doSearch(String query) {
        if(usersList.size() > 0){
            usersList.clear();
        }

        if(jobsList.size() > 0){
            jobsList.clear();
        }

        if(employerList.size() > 0){
            employerList.clear();
        }
        Velocity.post(utils.SEARCH_URL+"search/"+query)
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            JsonObject jsonObject = mJson.getAsJsonObject();
                            int type = jsonObject.get("result").getAsInt();

                            if(type == 1){
                                Jobs job = gson.fromJson(mJson, Jobs.class);
                                jobsList.add(job);
                            }else if(type == 2){
                                Users user = gson.fromJson(mJson, Users.class);
                                usersList.add(user);
                            }else if(type == 3){
                                Employer emp = gson.fromJson(mJson, Employer.class);
                                employerList.add(emp);
                            }
                        }

                        setAdapter();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: On Failed to Connect
                    }
                });
    }

    private void loadAllEmp(){
        if(empList.size() > 0){
            empList.clear();
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
                            }
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("LOADJOB", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void setAdapter() {
        if(jobsList.size() > 0){
            featuredJobAdapter = new FeaturedJobAdapter(getContext(), jobsList, empList);
            jobRecyclerView.setAdapter(featuredJobAdapter);

            if(jobSearchList.getVisibility() != View.VISIBLE){
                jobSearchList.setVisibility(View.VISIBLE);
            }

            if(mainList.getVisibility() != View.VISIBLE){
                mainList.setVisibility(View.VISIBLE);
            }
        }else{
            if(jobSearchList.getVisibility() == View.VISIBLE){
                jobSearchList.setVisibility(View.GONE);
            }
        }

        if(usersList.size() > 0){
            friendsListAdapter = new FriendsListAdapter(getContext(), usersList);
            userRecyclerView.setAdapter(friendsListAdapter);

            if(userSearchList.getVisibility() != View.VISIBLE){
                userSearchList.setVisibility(View.VISIBLE);
            }

            if(mainList.getVisibility() != View.VISIBLE){
                mainList.setVisibility(View.VISIBLE);
            }
        }else{
            if(userSearchList.getVisibility() == View.VISIBLE){
                userSearchList.setVisibility(View.GONE);
            }
        }

//        if(jobsList.size() > 0){
//            featuredJobAdapter = new FeaturedJobAdapter(getContext(), jobsList, empList);
//            jobRecyclerView.setAdapter(featuredJobAdapter);
//
//            if(userSearchList.getVisibility() != View.VISIBLE){
//                userSearchList.setVisibility(View.VISIBLE);
//            }
//
//            if(mainList.getVisibility() != View.VISIBLE){
//                mainList.setVisibility(View.VISIBLE);
//            }
//        }

        progressBar.setVisibility(View.GONE);
    }

    private void bindView(View rootView) {
        mainList = rootView.findViewById(R.id.mainList);

        jobSearchList = rootView.findViewById(R.id.jobSearchList);
        jobSearchList.setVisibility(View.GONE);

        userSearchList = rootView.findViewById(R.id.userSearchList);
        userSearchList.setVisibility(View.GONE);

        empSearchList = rootView.findViewById(R.id.empSearchList);
        empSearchList.setVisibility(View.GONE);

        RecyclerView.LayoutManager jobLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        RecyclerView.LayoutManager userLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        RecyclerView.LayoutManager empLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        jobRecyclerView = rootView.findViewById(R.id.jobRecyclerView);
        jobRecyclerView.setHasFixedSize(true);
        jobRecyclerView.setLayoutManager(jobLayoutManager);

        userRecyclerView = rootView.findViewById(R.id.userRecyclerView);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(userLayoutManager);

        empRecyclerView = rootView.findViewById(R.id.empRecyclerView);
        empRecyclerView.setHasFixedSize(true);
        empRecyclerView.setLayoutManager(empLayoutManager);

        resultTv = rootView.findViewById(R.id.resultTv);
        searchInput = rootView.findViewById(R.id.search_input);
        progressBar = rootView.findViewById(R.id.progressBar);
    }
}
