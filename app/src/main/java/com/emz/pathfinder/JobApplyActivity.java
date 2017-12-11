package com.emz.pathfinder;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import java.util.Objects;

public class JobApplyActivity extends AppCompatActivity {

    private static final String TAG = "JobApplyActivity";

    private int jobId;
    private Utils utils;
    private Jobs currentJob;
    private Employer currentEmp;
    private UserHelper usrHelper;

    private ScrollView jobApplyLayout;
    private TextView titleTv, fromTv, toTv;
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
            jobId = getIntent().getExtras().getInt("id");
            bindView();
            loadJobDetail();
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
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadJobDetail() {
        Velocity.post(utils.UTILITIES_URL+"getJobDetail/"+jobId)
                .withFormData("uid", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(!Objects.equals(response.body, "")){
                            Log.d(TAG, response.requestUrl);
                            Log.d(TAG, response.body);
                            Log.d(TAG, "Job Details GET");
                            currentJob = response.deserialize(Jobs.class);
                            getEmpDetail(currentJob.getCompany_id());
                        }else{
                            Log.e(TAG, "Error Getting Job Details");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "Error Connect to server.");
                    }
                });
    }

    private void getEmpDetail(int company_id) {
        Velocity.post(utils.UTILITIES_URL+"getEmpDetail/"+company_id)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(!Objects.equals(response.body, "")){
                            Log.d(TAG, response.body);
                            Log.d(TAG, "Emp Details GET");
                            currentEmp = response.deserialize(Employer.class);
                            loadUser(usrHelper.getUserId());
                        }else{
                            Log.e(TAG, "Error Getting Emp Details");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "Error Connect to server.");
                    }
                });
    }

    private void setupView() {
        getSupportActionBar().setTitle(currentJob.getName());
        titleTv.setText("I want to apply for "+currentJob.getName());
        toTv.setText(currentEmp.getName());
        fromTv.setText(users.getFname()+" "+users.getLname()+" ("+users.getEmail()+")");

        jobApplyLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void bindView() {
        jobApplyLayout = findViewById(R.id.job_apply_layout);
        progressBar = findViewById(R.id.job_apply_progressbar);

        titleTv = findViewById(R.id.job_apply_message_title);
        fromTv = findViewById(R.id.job_apply_message_from);
        toTv = findViewById(R.id.job_apply_message_to);

        jobApplyLayout.setVisibility(View.GONE);
    }
}
