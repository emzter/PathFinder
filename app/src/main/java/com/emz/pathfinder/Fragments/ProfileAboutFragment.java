package com.emz.pathfinder.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.Objects;

public class ProfileAboutFragment extends Fragment {
    private static final String TAG = "ProfileAboutFragment";

    private Users currentUser;

    private LinearLayout nameLayout, genderLayout, birthdateLayout, telephoneLayout, facebookLayout, twitterLayout, lineLayout, otherLayout;
    private TextView nameTv, genderTv, birthdateTv, telephoneTv, facebookTv, twitterTv, lineTv, otherTv;
    private Utils utils;

    public ProfileAboutFragment(){}

    @SuppressLint("ValidFragment")
    public ProfileAboutFragment(Users currentUser){
        this.currentUser = currentUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        utils = new Utils(this.getContext());

        bindView(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
    }

    private void setupView() {
        hideLayout();

        nameTv.setText(currentUser.getFname()+" "+currentUser.getLname());
        genderTv.setText(utils.getGender(currentUser.getSex()));

        if(!Objects.equals(currentUser.getBirthdate(), "1900-01-01")){
            birthdateTv.setText(currentUser.getBirthdate());
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
        birthdateLayout.setVisibility(View.GONE);
        telephoneLayout.setVisibility(View.GONE);
        facebookLayout.setVisibility(View.GONE);
        twitterLayout.setVisibility(View.GONE);
        lineLayout.setVisibility(View.GONE);
        otherLayout.setVisibility(View.GONE);
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
    }
}
