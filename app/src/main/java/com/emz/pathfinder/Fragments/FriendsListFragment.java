package com.emz.pathfinder.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.R;

public class FriendsListFragment extends Fragment {

    private Users currentUser;

    public FriendsListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_friends_list, container, false);

        if(getArguments() != null){
            currentUser = (Users) getArguments().getSerializable("currentUser");
        }

        return rootView;
    }
}
