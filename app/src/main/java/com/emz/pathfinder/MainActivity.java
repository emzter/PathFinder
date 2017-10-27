package com.emz.pathfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.emz.pathfinder.Utils.UserHelper;

public class MainActivity extends AppCompatActivity {

    private UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usrHelper = new UserHelper(this);

        authCheck();
    }

    private void authCheck(){
        if(!usrHelper.getLoginStatus()){
            onActionLogoutClicked();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                onActionLogoutClicked();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onActionLogoutClicked() {
        usrHelper.deleteSession();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
