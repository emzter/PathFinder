package com.emz.pathfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doodle.android.chips.ChipsView;
import com.emz.pathfinder.Adapters.CategoryChipsAdapter;
import com.emz.pathfinder.Models.Categories;
import com.emz.pathfinder.Models.UserModel;
import com.emz.pathfinder.Utils.Auth;
import com.emz.pathfinder.Utils.UserHelper;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.emz.pathfinder.R.drawable.defaultprofilepicture;
import static com.emz.pathfinder.Utils.Utils.AUTH_URL;
import static com.emz.pathfinder.Utils.Utils.JOBS_URL;
import static com.emz.pathfinder.Utils.Utils.PROFILEPIC_URL;
import static com.emz.pathfinder.Utils.Utils.USER_URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private NavigationView navigationView;

    private TextView navNameText, navEMailText;
    private ProgressBar progressBar;
    private CircleImageView navProPic;
    private View mChipView;

    private RecyclerView mRecyclerView;
    private CategoryChipsAdapter mAdapter;

    JSONArray jsonObject;

    private UserHelper usrHelper;
    private UserModel users;
    private List<Categories> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCategories();
        usrHelper = new UserHelper(this);

        authCheck();
        bindView();

        Log.d("TEST", usrHelper.getUserId());

        loadUser(usrHelper.getUserId());
    }

    private void loadUser(String userId) {
        Velocity.get(USER_URL)
                .withPathParam("status", "userloader")
                .withPathParam("id", userId)
                .withHeader("Content-Type","text/javascript;charset=utf-8")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        users = response.deserialize(UserModel.class);
                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("TEST", "CANT CONNECT");
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                onActionLogoutClicked();
                break;
            case R.id.nav_logout:
                onActionLogoutClicked();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void authCheck(){
        if(!usrHelper.getLoginStatus()){
            onActionLogoutClicked();
        }
    }

    private void onActionLogoutClicked() {
        usrHelper.deleteSession();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void bindView(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        catList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.catRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CategoryChipsAdapter(this, catList);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        drawer = findViewById(R.id.drawer_layout);
        toggler = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggler);
        toggler.syncState();

        progressBar = findViewById(R.id.main_activity_progressBar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        navProPic = navHeaderView.findViewById(R.id.navProfilePic);
        navEMailText = navHeaderView.findViewById(R.id.navEmailText);
        navNameText = navHeaderView.findViewById(R.id.navNameText);
    }

    private void loadCategories(){
        Velocity.get(JOBS_URL)
                .withPathParam("status","loadallcat")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        try {
                            jsonObject = new JSONArray(response.body);

                            for(int i = 0; i < jsonObject.length(); i++) {
                                JSONObject currentObj = jsonObject.getJSONObject(i);
                                int id = Integer.parseInt(currentObj.getString("id"));
                                String parent_id = currentObj.getString("parent_id");
                                String name = currentObj.getString("name");
                                String icon = currentObj.getString("icon");
                                catList.add(new Categories(id, parent_id, name, icon));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }

    private void setupView() {
        String fullname = users.getFirst_name()+" "+users.getLast_name();
        navEMailText.setText(users.getEmail());
        navNameText.setText(fullname);
        Glide.with(navProPic.getContext()).load(PROFILEPIC_URL+users.getGuid()+".jpg").apply(RequestOptions.centerCropTransform().error(R.drawable.defaultprofilepicture)).into(navProPic);

//        View chip = getLayoutInflater().inflate(R.layout.item_job_categories, mChipView, false);

        progressBar.setVisibility(View.GONE);
    }
}