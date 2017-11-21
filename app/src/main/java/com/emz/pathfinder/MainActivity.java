package com.emz.pathfinder;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Adapters.CategoryChipsAdapter;
import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Fragments.JobPortalFragment;
import com.emz.pathfinder.Fragments.NotificationFragment;
import com.emz.pathfinder.Fragments.SearchFragment;
import com.emz.pathfinder.Fragments.SettingsFragment;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Models.Categories;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.emz.pathfinder.Utils.Utils.JOBS_URL;
import static com.emz.pathfinder.Utils.Utils.PROFILEPIC_URL;
import static com.emz.pathfinder.Utils.Utils.USER_URL;
import static com.emz.pathfinder.Utils.Utils.sendRegistrationToServer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private NavigationView navigationView;
    private LinearLayout mainLayout;

    private TextView navNameText, navEMailText;
    private ProgressBar progressBar;
    private CircleImageView navProPic;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private UserHelper usrHelper;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usrHelper = new UserHelper(this);

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new JobPortalFragment(),
                    new TimelineFragment(),
                    new SearchFragment(),
                    new NotificationFragment(),
                    new SettingsFragment()
            };
            private final String[] mFragmentNames = new String[]{
                    "Portal",
                    "Timeline",
                    "Search",
                    "Notification",
                    "Settings",
            };

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        mViewPager = findViewById(R.id.container);

        bindView();

        authCheck();
    }

    private void loadUser(String userId) {
        Velocity.get(USER_URL)
                .withPathParam("status", "userloader")
                .withPathParam("id", userId)
                .withHeader("Content-Type","text/javascript;charset=utf-8")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        users = response.deserialize(Users.class);
                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        noConnection();
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void noConnection() {
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                onActionHomeClicked();
                break;
            case R.id.nav_location:
                onActionLocationClicked();
                break;
            case R.id.nav_logout:
                onActionLogoutClicked();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onActionLocationClicked() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void onActionHomeClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        }else{
            loadUser(usrHelper.getUserId());
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

        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void setupView() {
        String fullname = users.getFirst_name()+" "+users.getLast_name();
        navEMailText.setText(users.getEmail());
        navNameText.setText(fullname);
        Glide.with(navProPic.getContext()).load(PROFILEPIC_URL+users.getProfile_image()).apply(RequestOptions.centerCropTransform().error(R.drawable.defaultprofilepicture)).into(navProPic);

        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token, this);
        Log.d(TAG, "Token: " + token);

        progressBar.setVisibility(View.GONE);
    }
}