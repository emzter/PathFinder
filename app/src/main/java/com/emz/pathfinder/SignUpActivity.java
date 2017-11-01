package com.emz.pathfinder;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emz.pathfinder.Utils.UserHelper;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createProgressDialog;
import static com.emz.pathfinder.Utils.Ui.createSnackbar;
import static com.emz.pathfinder.Utils.Ui.dismissProgressDialog;
import static com.emz.pathfinder.Utils.Utils.AUTH_URL;
import static com.emz.pathfinder.Utils.Utils.REGISTER_URL;
import static com.emz.pathfinder.Utils.Utils.convertString;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout registerBox;
    private Button registerButton;
    private EditText nameText, lastnameText, emailText, passText, cpassText;
    private TextView loginLinkButton;

    private String name, lastname, email, pass, cpass;

    private UserHelper usrHelper;

    private boolean valid;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usrHelper = new UserHelper(this);

        Velocity.initialize(3);

        bindView();

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                register();
        }
    }

    private void bindView() {
        registerBox = findViewById(R.id.RegisterBox);
        registerButton = findViewById(R.id.btn_register);
        nameText = findViewById(R.id.input_name);
        lastnameText = findViewById(R.id.input_last_name);
        emailText = findViewById(R.id.input_email);
        passText = findViewById(R.id.input_password);
        cpassText = findViewById(R.id.input_re_password);
    }


    private void register() {
        convertRegisterInfo();

        if (!validate()) {
            onRegisterFailed(0);
            return;
        }

        registerButton.setEnabled(false);
        createProgressDialog(this, getString(R.string.registering));

        registerUser(name, lastname, email, pass);
    }

    private void registerUser(String name, String lastname, String email, String pass) {
        Velocity.post(REGISTER_URL)
                .withFormData("email",email)
                .withFormData("firstname",name)
                .withFormData("lastname",lastname)
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

    private void onRegisterSuccess() {
        createProgressDialog(this, getString(R.string.AuthenticatingText));
        authUser(email, pass);
    }

    private void authUser(String email, String password) {
        Velocity.post(AUTH_URL).withFormData("status","login").withFormData("email", email).withFormData("password", password).connect(new Velocity.ResponseListener() {
            @Override
            public void onVelocitySuccess(Velocity.Response response) {
                if(!Objects.equals(response.body, "Failed")){
                    onLoginSuccess(response.body);
                }else{
                    onRegisterFailed(0);
                }
            }

            @Override
            public void onVelocityFailed(Velocity.Response response) {
                onRegisterFailed(2);
            }
        });
    }

    private void onLoginSuccess(String uid){
        usrHelper.createSession(uid);
        startMainActivity();
    }

    private void startMainActivity() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                dismissProgressDialog();
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private void onRegisterFailed(int stage) {
        View view = findViewById(R.id.signup_root_view);
        if(stage == 0){
            createSnackbar(view, getString(R.string.something_went_wrong));
        }else if(stage == 1){
            createSnackbar(view, getString(R.string.email_already_used));
        }else if(stage == 2){
            createSnackbar(view, getString(R.string.connection_error));
        }
        registerButton.setEnabled(true);
        dismissProgressDialog();
    }

    private boolean validate() {
        valid = true;

        checkEmail();
        checkName();
        checkPassword();

        return valid;
    }

    private void checkName() {
        if (name.isEmpty()) {
            nameText.setError(getString(R.string.no_name_enter_error));
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (lastname.isEmpty()) {
            lastnameText.setError(getString(R.string.no_lastname_enter_error));
            valid = false;
        } else {
            lastnameText.setError(null);
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

    private void checkPassword() {
        if (pass.isEmpty() || pass.length() < 8 || pass.length() > 12) {
            passText.setError(getString(R.string.password_error));
            valid = false;
        } else {
            passText.setError(null);
        }

        if(!Objects.equals(cpass, pass)){
            cpassText.setError(getString(R.string.confirm_password_error));
            valid = false;
        } else {
            cpassText.setError(null);
        }
    }

    private void convertRegisterInfo() {
        name = convertString(nameText);
        lastname = convertString(lastnameText);
        email = convertString(emailText);
        pass = convertString(passText);
        cpass = convertString(cpassText);
    }
}
