package com.emz.pathfinder;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACTIVITY_CONSTANT = 0;

    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_CONSTANT) {
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                startLoginActivity();
                break;
            case R.id.registerBtn:
                startRegisterActivity();
                break;
        }
    }

    private void startLoginActivity() {
        Intent loginActivity = new Intent(StartActivity.this, LoginActivity.class);
        startActivityForResult(loginActivity, ACTIVITY_CONSTANT);
    }

    private void startRegisterActivity() {
        Intent loginActivity = new Intent(StartActivity.this, RegisterActivity.class);
        startActivityForResult(loginActivity, ACTIVITY_CONSTANT);
    }



    private void startMainActivity() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
}