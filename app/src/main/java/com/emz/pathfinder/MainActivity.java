package com.emz.pathfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.emz.pathfinder.Models.UserModel;
import com.emz.pathfinder.Utils.Auth;
import com.emz.pathfinder.Utils.UserHelper;
import com.rw.velocity.Velocity;

import org.json.JSONObject;

import static com.emz.pathfinder.Utils.Utils.AUTH_URL;
import static com.emz.pathfinder.Utils.Utils.USER_URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggler;
    private NavigationView navigationView;

    private TextView navNameText, navEMailText;
    private ProgressBar progressBar;

    private UserHelper usrHelper;
    private UserModel users;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usrHelper = new UserHelper(this);

        authCheck();

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
                        Log.e("TEST", response.body);
                        users = response.deserialize(UserModel.class);
                        bindView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("TEST", response.body);
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

        drawer = findViewById(R.id.drawer_layout);
        toggler = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggler);
        toggler.syncState();

        progressBar = findViewById(R.id.main_activity_progressBar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        navEMailText = navHeaderView.findViewById(R.id.navEmailText);
        navNameText = navHeaderView.findViewById(R.id.navNameText);

        setupView();
    }

    private void setupView() {
        String fullname = users.getFirst_name()+" "+users.getLast_name();
        navEMailText.setText(users.getEmail());
        navNameText.setText(fullname);
        progressBar.setVisibility(View.GONE);
    }
}