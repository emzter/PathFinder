package com.emz.pathfinder;

import android.app.VoiceInteractor;
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
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createProgressDialog;
import static com.emz.pathfinder.Utils.Ui.createSnackbar;
import static com.emz.pathfinder.Utils.Ui.dismissProgressDialog;
import static com.emz.pathfinder.Utils.Utils.AUTH_URL;
import static com.emz.pathfinder.Utils.Utils.convertString;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        createProgressDialog(this, getString(R.string.AuthenticatingText));

        authUser(email, password);
    }

    private void authUser(String email, String password) {
        Velocity.post(AUTH_URL).withFormData("status","login").withFormData("email", email).withFormData("password", password).connect(new Velocity.ResponseListener() {
            @Override
            public void onVelocitySuccess(Velocity.Response response) {
                if(!Objects.equals(response.body, "Failed")){
                    onLoginSuccess(response.body);
                }else{
                    onLoginFailed();
                }
            }

            @Override
            public void onVelocityFailed(Velocity.Response response) {
                Log.w(TAG, response.toString());
                onLoginFailed();
            }
        });
    }

    private void onLoginSuccess(String uid){
        usrHelper.createSession(uid);

        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }else{
            onLoginFailed();
        }
    }

    private void onLoginFailed() {
        View view = findViewById(R.id.signin_root_view);
        createSnackbar(view, getString(R.string.auth_failed));
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
        email = convertString(emailText);
        password = convertString(passwordText);
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