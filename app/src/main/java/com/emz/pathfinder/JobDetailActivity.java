package com.emz.pathfinder;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Utils.UlTagHandler;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class JobDetailActivity extends AppCompatActivity {

    //TODO: EMP CATEGORY

    private static final String TAG = "JobDetailActivity";

    private int jobId;
    private Utils utils;
    private Jobs currentJob;
    private Employer currentEmp;
    private UserHelper usrHelper;

    private ScrollView jobDetailLayout;
    private ProgressBar progressBar;
    private LinearLayout empAboutLayout, empAddressLayout, empEmailLayout, empTelLayout;
    private TextView empName, empAbout, empCat, empEmail, empTel, jobResponse, jobQualify, jobBenefit, jobCapacity, applyBtnIcon, applyBtnText,
            jobCapacityUnit, jobLevel, jobSalary, jobSalaryType, jobNegotiable, jobExp, jobEdu, jobCategory, jobPostedDate, favStar;
    private ImageView empLogo;
    private LinearLayout saveBtn, applyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                            setupView();
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

        setEmpView();
        setJobView();

        setFavStar();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavJob();
            }
        });

        if(currentJob.isApply()){
            applyBtnIcon.setText(R.string.fa_check);
            applyBtnText.setText(R.string.sent);
        }else{
            applyBtnIcon.setText(R.string.fa_paper_plane);
            applyBtnText.setText(R.string.apply);
            applyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent apply = new Intent(JobDetailActivity.this, JobApplyActivity.class);
                    apply.putExtra("job", currentJob);
                    apply.putExtra("emp", currentEmp);
                    startActivityForResult(apply, 0);
                }
            });
        }

        jobDetailLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }

    private void setFavJob() {
        Velocity.post(utils.UTILITIES_URL+"saveJob/"+jobId)
                .withFormData("uid", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObject.get("success").getAsBoolean();

                        if(status){
                            boolean add = jsonObject.get("add").getAsBoolean();
                            if(add){
                                currentJob.setFavorite(true);
                                setFavStar();
                                Log.d(TAG, "SUCCESS ADD");
                            }else{
                                currentJob.setFavorite(false);
                                setFavStar();
                                Log.d(TAG, "SUCCESS DELETE");
                            }

                        }else{
                            Log.e(TAG, "ERROR FAVORITES");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "ERROR FAVORITES CAN'T CONNECT");
                    }
                });
    }

    private void setFavStar() {
        if(currentJob.isFavorite()){
            favStar.setText(R.string.fa_star);
            favStar.setTextColor(getResources().getColor(R.color.favorited));
        }else{
            favStar.setText(R.string.fa_star_o);
            favStar.setTextColor(getResources().getColor(R.color.monsoon));
        }
    }

    private void setJobView() {
        jobPostedDate.setText(utils.parseDate(currentJob.getCreatedAt()));
        jobResponse.setText(Html.fromHtml(currentJob.getResponsibility(), null, new UlTagHandler()));
        jobQualify.setText(Html.fromHtml(currentJob.getQualification(), null, new UlTagHandler()));
        jobBenefit.setText(Html.fromHtml(currentJob.getBenefit(), null, new UlTagHandler()));

        if(currentJob.getCapType() == 1){
            jobCapacity.setText(R.string.multirates);
            jobCapacityUnit.setVisibility(View.GONE);
        }else{
            jobCapacity.setText(String.valueOf(currentJob.getCapacity()));
        }

        jobLevel.setText(utils.getJobLevel(currentJob.getLevel()));
        jobSalary.setText(String.valueOf(currentJob.getSalary()));
        jobSalaryType.setText(utils.getGetSalaryType(currentJob.getSalaryType()));
        jobCategory.setText(currentJob.getCategory());
        jobEdu.setText(utils.getEduReq(currentJob.getEdu_req()));
        jobExp.setText(utils.getExpReq(currentJob.getExp_req()));
    }

    private void setEmpView() {
        empName.setText(currentEmp.getName());
        empCat.setText(String.valueOf(currentEmp.getCategory_id()));

        if(!Objects.equals(currentEmp.getAbout(), "")){
            empAbout.setText(currentEmp.getAbout());
            empAboutLayout.setVisibility(View.VISIBLE);
        }

        if(!Objects.equals(currentEmp.getContactEmail(), "")){
            empEmail.setText(currentEmp.getContactEmail());
            empEmailLayout.setVisibility(View.VISIBLE);
        }

        if(!Objects.equals(currentEmp.getTelephone(), "")){
            empTel.setText(currentEmp.getTelephone());
            empTelLayout.setVisibility(View.VISIBLE);
        }

        Glide.with(this).load(utils.EMPPIC_URL+currentEmp.getLogo()).apply(RequestOptions.centerInsideTransform().error(R.drawable.default_emp_logo)).into(empLogo);
    }

    private void bindView() {
        jobDetailLayout = findViewById(R.id.job_detail_layout);
        progressBar = findViewById(R.id.job_detail_progressbar);

        empName = findViewById(R.id.emp_detail_name);
        empAbout = findViewById(R.id.emp_detail_about);
        empCat = findViewById(R.id.emp_detail_category);
        empEmail = findViewById(R.id.emp_detail_contact_email);
        empTel = findViewById(R.id.emp_detail_contact_number);
        empLogo = findViewById(R.id.emp_detail_logo);

        jobPostedDate = findViewById(R.id.job_detail_posted_date);
        jobResponse = findViewById(R.id.job_detail_responsibility);
        jobQualify = findViewById(R.id.job_detail_qualify);
        jobBenefit = findViewById(R.id.job_detail_benefit);
        jobCapacity = findViewById(R.id.job_detail_capacity);
        jobCapacityUnit = findViewById(R.id.job_detail_capacity_rates);
        jobLevel = findViewById(R.id.job_detail_level);
        jobSalary = findViewById(R.id.job_detail_salary);
        jobSalaryType = findViewById(R.id.job_detail_salary_type);
        jobNegotiable = findViewById(R.id.job_detail_salary_negotiable);
        jobExp = findViewById(R.id.job_detail_experiences_requirement);
        jobEdu = findViewById(R.id.job_detail_edu_requirement);
        jobCategory = findViewById(R.id.job_detail_category);

        empAboutLayout = findViewById(R.id.emp_detail_about_layout);
        empAddressLayout = findViewById(R.id.emp_detail_address_layout);
        empEmailLayout = findViewById(R.id.emp_detail_contact_email_layout);
        empTelLayout = findViewById(R.id.emp_detail_contact_number_layout);

        favStar = findViewById(R.id.favStar);
        saveBtn = findViewById(R.id.save_btn);
        applyBtn = findViewById(R.id.applyBtn);
        applyBtnIcon = findViewById(R.id.applyBtn_icon);
        applyBtnText = findViewById(R.id.applyBtn_text);

        jobDetailLayout.setVisibility(View.GONE);
        empAddressLayout.setVisibility(View.GONE);
        empAboutLayout.setVisibility(View.GONE);
        empEmailLayout.setVisibility(View.GONE);
        empTelLayout.setVisibility(View.GONE);
    }
}
