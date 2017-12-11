package com.emz.pathfinder;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createProgressDialog;
import static com.emz.pathfinder.Utils.Ui.createSnackbar;
import static com.emz.pathfinder.Utils.Ui.dismissProgressDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText emailTv, passTv, cpassTv, fnameTv, lnameTv;
    private String email, pass, cpass, fname, lname;
    private Button registerBtn;
    private Toolbar toolbar;

    private Utils utils;

    private boolean valid;
    private UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        authCheck();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
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
        emailTv = findViewById(R.id.register_input_email);
        passTv = findViewById(R.id.register_input_password);
        cpassTv = findViewById(R.id.register_input_confirm_password);
        fnameTv = findViewById(R.id.register_input_name);
        lnameTv = findViewById(R.id.register_input_lastname);
        registerBtn = findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(this);
    }

    private void authCheck() {
        if(usrHelper.getLoginStatus()){
            startMainActivity();
        }
    }

    private void register() {
        convertRegisterInfo();

        if (!validate()) {
            onRegisterFailed(0);
            return;
        }

        registerBtn.setEnabled(false);
        createProgressDialog(this, getString(R.string.registering));

        registerUser(fname, lname, email, pass);
    }

    private void convertRegisterInfo() {
        email = emailTv.getText().toString();
        pass = passTv.getText().toString();
        cpass = cpassTv.getText().toString();
        fname = fnameTv.getText().toString();
        lname = lnameTv.getText().toString();
    }

    private void registerUser(String fname, String lname, String email, String pass) {
        Velocity.post(utils.REGISTER_URL)
                .withFormData("email",email)
                .withFormData("firstname",fname)
                .withFormData("lastname",lname)
                .withFormData("password",pass)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(Objects.equals(response.body, "Success")){
                            Log.d(TAG, response.body);
                            dismissProgressDialog();
                            onRegisterSuccess();
                        }else if(Objects.equals(response.body, "EMailUsed")){
                            Log.w(TAG, response.body);
                            onRegisterFailed(1);
                        }else{
                            Log.e(TAG, response.body);
                            onRegisterFailed(0);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        onRegisterFailed(2);
                    }
                });
    }

    private void authUser(String email, String password) {
        Velocity.post(utils.LOGIN_URL)
                .withFormData("email", email)
                .withFormData("password", password)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(response.body.contains("Success")){
                            String uid = response.body.replace("Success", "");
                            onLoginSuccess(uid);
                        }else if(Objects.equals(response.body, "FailedNoUsers")){
                            onLoginFailed(1);
                        }else{
                            onLoginFailed(0);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        onLoginFailed(2);
                    }
                });
    }

    private boolean validate(){
        valid = true;

        checkEmail();
        checkName();
        checkPassword();

        return valid;
    }

    private void checkEmail(){
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTv.setError(getString(R.string.email_error));
            valid = false;
        } else {
            emailTv.setError(null);
        }
    }

    private void checkPassword(){
        if (pass.isEmpty() || pass.length() < 8) {
            passTv.setError(getString(R.string.password_error));
            valid = false;
        } else {
            passTv.setError(null);
        }

        if(!Objects.equals(cpass, pass)){
            cpassTv.setError(getString(R.string.confirm_password_error));
            valid = false;
        } else {
            cpassTv.setError(null);
        }
    }

    private void checkName(){
        if (fname.isEmpty()){
            fnameTv.setError(getString(R.string.no_name_enter_error));
            valid = false;
        } else {
            fnameTv.setError(null);
        }

        if (lname.isEmpty()){
            lnameTv.setError(getString(R.string.no_lastname_enter_error));
            valid = false;
        } else {
            lnameTv.setError(null);
        }
    }

    private void onRegisterFailed(int stage) {
        View view = findViewById(R.id.register_view);

        if(stage == 0){
            createSnackbar(view, getString(R.string.something_went_wrong));
        }else if(stage == 1){
            createSnackbar(view, getString(R.string.email_already_used));
        }else if(stage == 2){
            createSnackbar(view, getString(R.string.connection_error));
        }

        registerBtn.setEnabled(true);
        dismissProgressDialog();
    }

    private void onRegisterSuccess(){
        dismissProgressDialog();
        createProgressDialog(this, getString(R.string.AuthenticatingText));
        authUser(email, pass);
    }

    private void onLoginSuccess(String uid){
        dismissProgressDialog();
        usrHelper.createSession(uid);
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    private void onLoginFailed(int stage) {
        View view = findViewById(R.id.login_view);
        if (stage == 0) {
            createSnackbar(view, getString(R.string.auth_failed));
        } else if (stage == 1) {
            createSnackbar(view, getString(R.string.no_user_found));
        } else if (stage == 2) {
            createSnackbar(view, getString(R.string.connection_error));
        }
        dismissProgressDialog();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
