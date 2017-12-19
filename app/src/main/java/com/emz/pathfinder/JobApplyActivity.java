package com.emz.pathfinder;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.Objects;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class JobApplyActivity extends AppCompatActivity {

    private static final String TAG = "JobApplyActivity";

    private Utils utils;
    private Jobs currentJob;
    private Employer currentEmp;
    private UserHelper usrHelper;

    private ScrollView jobApplyLayout;
    private TextView titleTv, fromTv, toTv;
    private EditText messageEt;
    private ProgressBar progressBar;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_apply);
        overridePendingTransition( R.anim.trans_left_in, R.anim.trans_left_out);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        if(getIntent().getExtras() != null) {
            currentJob = (Jobs) getIntent().getExtras().get("job");
            currentEmp = (Employer) getIntent().getExtras().get("emp");
            bindView();
            loadUser(usrHelper.getUserId());
        }else{
            finish();
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
                onBackPressed();
                overridePendingTransition( R.anim.trans_right_in, R.anim.trans_right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadUser(String userId) {
        Velocity.get(utils.UTILITIES_URL+"getProfile/"+userId)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        users = response.deserialize(Users.class);
                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        View v = findViewById(R.id.job_apply_main_layout);
                        createSnackbar(v, getString(R.string.connection_error));
                    }
                });
    }

    private void setupView() {
        getSupportActionBar().setTitle(currentJob.getName());
        titleTv.setText("I want to apply for "+currentJob.getName());
        toTv.setText(currentEmp.getName());
        fromTv.setText(users.getFullName()+" ("+users.getEmail()+")");

        jobApplyLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void bindView() {
        jobApplyLayout = findViewById(R.id.job_apply_layout);
        progressBar = findViewById(R.id.job_apply_progressbar);

        titleTv = findViewById(R.id.job_apply_message_title);
        fromTv = findViewById(R.id.job_apply_message_from);
        toTv = findViewById(R.id.job_apply_message_to);

        messageEt = findViewById(R.id.job_apply_message);

        LinearLayout applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyJob();
            }
        });

        jobApplyLayout.setVisibility(View.GONE);
    }


    public void applyJob(){
        final MaterialDialog md = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_title)
                .content(R.string.sending_application)
                .progress(true, 0)
                .cancelable(false)
                .show();

        String message = messageEt.getText().toString();
        Velocity.post(utils.JOBS_URL+"sendapply")
                .withFormData("id", String.valueOf(currentJob.getId()))
                .withFormData("message", message)
                .withFormData("uid", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObject.get("success").getAsBoolean();
                        if(status){
                            md.dismiss();
                            Intent returnIntent = getIntent();
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }else{
                            View v = findViewById(R.id.job_apply_main_layout);
                            createSnackbar(v, getString(R.string.cant_apply_job));
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        View v = findViewById(R.id.job_apply_main_layout);
                        createSnackbar(v, getString(R.string.connection_error));
                    }
                });
    }
}
