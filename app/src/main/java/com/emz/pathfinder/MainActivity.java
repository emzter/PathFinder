package com.emz.pathfinder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emz.pathfinder.Fragments.JobPortalFragment;
import com.emz.pathfinder.Fragments.NotificationFragment;
import com.emz.pathfinder.Fragments.SearchFragment;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Models.Volunteer;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rw.velocity.Velocity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private NavigationView navigationView;

    private TextView navNameText, navEMailText;
    private ProgressBar progressBar;
    private CircleImageView navProPic;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private UserHelper usrHelper;
    private Users users;
    private Utils utils;

    String token = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        Log.d(TAG, "USER: "+usrHelper.getUserId());

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[]{
                    new JobPortalFragment(),
                    new TimelineFragment(),
                    new SearchFragment(),
                    new NotificationFragment()
            };

            private final String[] mFragmentNames = new String[]{
                    getResources().getString(R.string.fa_home),
                    getResources().getString(R.string.fa_rss),
                    getResources().getString(R.string.fa_search),
                    getResources().getString(R.string.fa_bell)
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
        Velocity.post(utils.UTILITIES_URL+"getProfile/"+userId)
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        users = response.deserialize(Users.class);

                        if(users.getStatus() == 0){
                            createProfile();
                        }

                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        noConnection();
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void createProfile() {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        intent.putExtra("user", users);
        startActivity(intent);
        finish();
    }

    private void noConnection() {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                onActionHomeClicked();
                break;
            case R.id.nav_profile:
                onActionProfileClicked();
                break;
            case R.id.nav_favjob:
                onActionFavJobsClicked();
                break;
            case R.id.nav_location:
                onActionLocationClicked();
                break;
            case R.id.nav_jobs_centre:
                onActionJobsCentreClicked();
                break;
            case R.id.nav_logout:
                onActionLogoutClicked();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onActionLocationClicked() {
        Intent intent = new Intent(this, VolunteerActivity.class);
        startActivity(intent);
    }

    private void onActionProfileClicked() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id", Integer.parseInt(usrHelper.getUserId()));
        startActivity(intent);
    }

    private void onFabNewPostClicked() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    private void onActionFavJobsClicked() {
        Intent intent = new Intent(this, FavJobActivity.class);
        startActivity(intent);
    }

    private void onActionJobsCentreClicked() {
        Intent intent = new Intent(this, JobsCentreActivity.class);
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
        utils.deleteToken(token, usrHelper.getUserId());
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

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabNewPostClicked();
            }
        });

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private final String[] mPageNames = new String[]{
                    getString(R.string.home),
                    getString(R.string.timeline),
                    getString(R.string.search_title),
                    getString(R.string.notifications)
            };

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                toolbar.setTitle(mPageNames[position]);

                if(position == 1){
                    fab.setVisibility(View.VISIBLE);
                }else{
                    fab.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setupView() {
        navEMailText.setText(users.getEmail());
        navNameText.setText(users.getFullName());
        Log.d(TAG, users.getProPic());

        Glide.with(navProPic.getContext()).load(utils.PROFILEPIC_URL+users.getProPic()).apply(RequestOptions.centerCropTransform().error(R.drawable.defaultprofilepicture)).into(navProPic);

        utils.sendRegistrationToServer(token, this);

        progressBar.setVisibility(View.GONE);
    }
}