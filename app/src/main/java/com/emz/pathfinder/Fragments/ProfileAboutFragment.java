package com.emz.pathfinder.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emz.pathfinder.Adapters.AboutEduAdapter;
import com.emz.pathfinder.Adapters.AboutExpAdapter;
import com.emz.pathfinder.Models.Comments;
import com.emz.pathfinder.Models.Educations;
import com.emz.pathfinder.Models.Experiences;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class ProfileAboutFragment extends Fragment {
    private static final String TAG = "ProfileAboutFragment";

    private Users currentUser;

    private LinearLayout nameLayout, genderLayout, birthdateLayout, telephoneLayout, facebookLayout, twitterLayout, lineLayout, otherLayout;
    private TextView nameTv, genderTv, birthdateTv, telephoneTv, facebookTv, twitterTv, lineTv, otherTv;
    private Utils utils;


    private RelativeLayout expLayout, eduLayout;

    private AboutEduAdapter eduAdapter;
    private RecyclerView expRecyclerView, eduRecyclerView;

    private List<Experiences> experiencesList;
    private List<Educations> educationsList;

    public ProfileAboutFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        utils = new Utils(this.getContext());
        experiencesList = new ArrayList<>();
        educationsList = new ArrayList<>();

        if(getArguments() != null){
            currentUser = (Users) getArguments().getSerializable("currentUser");
        }

        bindView(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        loadExp();
        loadEdu();
    }

    private void loadEdu() {
        Velocity.get(utils.UTILITIES_URL+"getEdu/"+currentUser.getId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Educations educations = gson.fromJson(mJson, Educations.class);
                            educationsList.add(educations);
                        }

                        if(educationsList.size() == 0){
                            eduLayout.setVisibility(GONE);
                        }else{
                            setupEduAdapter();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: No Connection
                    }
                });
    }

    private void loadExp() {
        Velocity.get(utils.UTILITIES_URL+"getExp/"+currentUser.getId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Experiences exp = gson.fromJson(mJson, Experiences.class);
                            experiencesList.add(exp);
                        }

                        if(experiencesList.size() == 0){
                            expLayout.setVisibility(GONE);
                        }else{
                            setupExpAdapter();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: No Connection
                    }
                });
    }

    private void setupExpAdapter() {
        AboutExpAdapter expAdapter = new AboutExpAdapter(getContext(), experiencesList);
        expRecyclerView.setAdapter(expAdapter);
    }

    private void setupEduAdapter() {
        AboutEduAdapter eduAdapter = new AboutEduAdapter(getContext(), educationsList);
        eduRecyclerView.setAdapter(eduAdapter);
    }

    private void setupView() {
        hideLayout();

        nameTv.setText(currentUser.getFullName());
        genderTv.setText(utils.getGender(currentUser.getSex()));

        if(!Objects.equals(currentUser.getBirthdate(), "1900-01-01")){
            birthdateTv.setText(utils.parseDate(currentUser.getBirthdate(), "yyyy-MM-dd"));
            birthdateLayout.setVisibility(View.VISIBLE);
        }
        if(currentUser.getTelephone() != null){
            telephoneTv.setText(currentUser.getTelephone());
            telephoneLayout.setVisibility(View.VISIBLE);
        }
        if(currentUser.getFacebook() != null){
            facebookTv.setText(currentUser.getFacebook());
            facebookLayout.setVisibility(View.VISIBLE);
        }
        if(currentUser.getTwitter() != null){
            twitterTv.setText(currentUser.getTwitter());
            twitterLayout.setVisibility(View.VISIBLE);
        }
        if(currentUser.getLine() != null){
            lineTv.setText(currentUser.getLine());
            lineLayout.setVisibility(View.VISIBLE);
        }
        if(currentUser.getOtherlink() != null){
            otherTv.setText(currentUser.getOtherlink());
            otherLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideLayout() {
        birthdateLayout.setVisibility(GONE);
        telephoneLayout.setVisibility(GONE);
        facebookLayout.setVisibility(GONE);
        twitterLayout.setVisibility(GONE);
        lineLayout.setVisibility(GONE);
        otherLayout.setVisibility(GONE);
    }

    private void bindView(View rootView) {
        nameLayout = rootView.findViewById(R.id.aboutme_namelayout);
        genderLayout = rootView.findViewById(R.id.aboutme_genderlayout);
        birthdateLayout = rootView.findViewById(R.id.aboutme_birthdatelayout);
        telephoneLayout = rootView.findViewById(R.id.aboutme_telephonelayout);
        facebookLayout = rootView.findViewById(R.id.aboutme_facebooklayout);
        twitterLayout = rootView.findViewById(R.id.aboutme_twitterlayout);
        lineLayout = rootView.findViewById(R.id.aboutme_linelayout);
        otherLayout = rootView.findViewById(R.id.aboutme_otherlayout);
        nameTv = rootView.findViewById(R.id.aboutme_name);
        genderTv = rootView.findViewById(R.id.aboutme_gender);
        birthdateTv = rootView.findViewById(R.id.aboutme_birthdate);
        telephoneTv = rootView.findViewById(R.id.aboutme_telephone);
        facebookTv = rootView.findViewById(R.id.aboutme_facebook);
        twitterTv = rootView.findViewById(R.id.aboutme_twitter);
        lineTv = rootView.findViewById(R.id.aboutme_line);
        otherTv = rootView.findViewById(R.id.aboutme_otherlink);

        expLayout = rootView.findViewById(R.id.about_exp_view);
        eduLayout = rootView.findViewById(R.id.about_edu_view);

        expRecyclerView = rootView.findViewById(R.id.expRecyclerView);
        expRecyclerView.setHasFixedSize(true);

        eduRecyclerView = rootView.findViewById(R.id.eduRecyclerView);
        eduRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager expLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        expRecyclerView.setLayoutManager(expLayoutManager);

        RecyclerView.LayoutManager eduLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        eduRecyclerView.setLayoutManager(eduLayoutManager);
    }
}
