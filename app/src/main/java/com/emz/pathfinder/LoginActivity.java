package com.emz.pathfinder;

import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private TextView emailTv, passTv;
    private Button loginBtn;
    private String email, pass;

    private Utils utils;

    private boolean valid;
    private UserHelper usrHelper;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        authCheck();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        setResult(RESULT_CANCELED, returnIntent);

        super.onBackPressed();

        overridePendingTransition( R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition( R.anim.trans_right_in, R.anim.trans_right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindView() {
        emailTv = findViewById(R.id.login_input_email);
        passTv = findViewById(R.id.login_input_password);
        loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
    }

    private void authCheck() {
        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }
    }

    private void login() {

        email = emailTv.getText().toString();
        pass = passTv.getText().toString();

        loginBtn.setEnabled(false);

        if (!validate()) {
            onLoginFailed(0);
            return;
        }

        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_title)
                .content(R.string.AuthenticatingText)
                .progress(true, 0)
                .cancelable(false)
                .show();

        Velocity.post(utils.LOGIN_URL)
                .withFormData("email", email)
                .withFormData("password", pass)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Users user = response.deserialize(Users.class);
                        onLoginSuccess(String.valueOf(user.getId()));
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        onLoginFailed(response.responseCode);
                    }
                });
    }

    private void onLoginSuccess(String uid){
        materialDialog.dismiss();
        usrHelper.createSession(uid);
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void onLoginFailed(int stage) {
        View view = findViewById(R.id.login_view);
        if(stage == 401){
            createSnackbar(view, getString(R.string.auth_failed));
        }else if(stage == 422){
            createSnackbar(view, getString(R.string.connection_error));
        }else if(stage == 404){
            createSnackbar(view, getString(R.string.no_user_found));
        }

        loginBtn.setEnabled(true);
        materialDialog.dismiss();
    }

    private boolean validate() {
        valid = true;

        checkEmail();
        checkPassword();

        return valid;
    }

    private void checkPassword() {
        if (pass.isEmpty() || pass.length() < 8 || pass.length() > 12) {
            passTv.setError(getString(R.string.password_error));
            valid = false;
        } else {
            passTv.setError(null);
        }
    }

    private void checkEmail() {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTv.setError(getString(R.string.email_error));
            valid = false;
        } else {
            emailTv.setError(null);
        }
    }
}
