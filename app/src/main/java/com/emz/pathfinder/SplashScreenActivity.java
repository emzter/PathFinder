package com.emz.pathfinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.emz.pathfinder.Utils.UserHelper;
import com.rw.velocity.Velocity;

public class SplashScreenActivity extends AppCompatActivity {

    private UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Velocity.initialize(3);

        usrHelper = new UserHelper(this);

        authCheck();
    }

    private void authCheck() {
        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }else{
            startLoginActivity();
        }
    }

    private void startMainActivity() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private void startLoginActivity() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, StartActivity.class));
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
}
