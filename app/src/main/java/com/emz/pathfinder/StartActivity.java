package com.emz.pathfinder;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createProgressDialog;
import static com.emz.pathfinder.Utils.Ui.createSnackbar;
import static com.emz.pathfinder.Utils.Ui.dismissProgressDialog;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACTIVITY_CONSTANT = 0;
    private LinearLayout loginBox;
    private ImageView appLogo;
    private Animation animTranslate;
    private Animation animFade;
    private Button loginButton;
    private TextView registerButton;
    private EditText emailText;
    private EditText passwordText;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int RC_SIGN_IN = 9001;

    private UserHelper usrHelper;

    private String password;
    private String email;
    private boolean valid;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        utils = new Utils(this);

        usrHelper = new UserHelper(this);

        Velocity.initialize(3);

        bindView();
        setupView();
        authCheck();

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.link_register:
                signup();
                break;
        }
    }

    private void signup() {
        startActivityForResult(new Intent(StartActivity.this, SignUpActivity.class), ACTIVITY_CONSTANT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_CONSTANT) {
            this.finish();
        }
    }

    private void login() {
        convertLoginInfo();

        if (!validate()) {
            onLoginFailed(0);
            return;
        }

        loginButton.setEnabled(false);
        createProgressDialog(this, getString(R.string.AuthenticatingText));

        authUser(email, password);
    }

    private void authUser(String email, String password) {
        Velocity.post(utils.LOGIN_URL).withFormData("email", email).withFormData("password", password).connect(new Velocity.ResponseListener() {
            @Override
            public void onVelocitySuccess(Velocity.Response response) {
                if(response.body.contains("Success")){
                    String uid = response.body.replace("Success", "");
                    onLoginSuccess(uid);
                }else if(Objects.equals(response.body, "FailedNoUsers")){
                    onLoginFailed(2);
                }else{
                    onLoginFailed(0);
                }
            }

            @Override
            public void onVelocityFailed(Velocity.Response response) {
                Log.w(TAG, response.toString());
                onLoginFailed(1);
            }
        });
    }

    private void onLoginSuccess(String uid){
        usrHelper.createSession(uid);

        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }else{
            onLoginFailed(0);
        }
    }

    private void onLoginFailed(int stage) {
        View view = findViewById(R.id.signin_root_view);
        if(stage == 0){
            createSnackbar(view, getString(R.string.auth_failed));
        }else if(stage == 1){
            createSnackbar(view, getString(R.string.connection_error));
        }else if(stage == 2){
            createSnackbar(view, getString(R.string.no_user_found));
        }

        loginButton.setEnabled(true);
        dismissProgressDialog();
    }

    private boolean validate() {
        valid = true;

        checkEmail();
        checkPassword();

        return valid;
    }

    private void checkPassword() {
        if (password.isEmpty() || password.length() < 8 || password.length() > 12) {
            passwordText.setError(getString(R.string.password_error));
            valid = false;
        } else {
            passwordText.setError(null);
        }
    }

    private void checkEmail() {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.email_error));
            valid = false;
        } else {
            emailText.setError(null);
        }
    }

    private void convertLoginInfo() {
        email = utils.convertString(emailText);
        password = utils.convertString(passwordText);
    }

    private void authCheck() {
        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }else{
            animStart();
        }
    }

    private void startMainActivity() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                dismissProgressDialog();
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private void animStart() {
        appLogo.startAnimation(animTranslate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginBox.setVisibility(View.VISIBLE);
                loginBox.startAnimation(animFade);
                animTranslate.setFillAfter(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setupView() {
        loginBox.setVisibility(View.GONE);
    }

    private void bindView() {
        loginBox = findViewById(R.id.LoginBox);
        appLogo = findViewById(R.id.appLogo);
        animTranslate = AnimationUtils.loadAnimation(StartActivity.this, R.anim.translate);
        animFade = AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade);
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.link_register);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
    }
}