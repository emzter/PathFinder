package com.emz.pathfinder;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class EmployerDetailActivity extends AppCompatActivity {

    private static final String TAG = EmployerDetailActivity.class.getSimpleName();
    private int empId;

    private Employer currentEmp;
    private LinkedHashMap<Integer, Employer> empList;
    private List<Jobs> jobsList;

    private Utils utils;
    private UserHelper usrHelper;

    private NestedScrollView mainLayout;
    private ProgressBar progressBar;
    private TextView empName, empAbout, empCat, empEmail, empTel;
    private LinearLayout empAboutLayout, empAddressLayout, empEmailLayout, empTelLayout;
    private ImageView empLogo;
    private RecyclerView mRecyclerView;
    private FeaturedJobAdapter featuredJobAdapter;

    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_detail);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);
        jobsList = new ArrayList<>();
        empList = new LinkedHashMap<>();

        if(getIntent().getExtras() != null){
            empId = getIntent().getExtras().getInt("id");
            bindView();
            loadEmpDetail();
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

    private void bindView() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(" ");

        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progressBar);

        empName = findViewById(R.id.emp_detail_name);
        empAbout = findViewById(R.id.emp_detail_about);
        empCat = findViewById(R.id.emp_detail_category);
        empEmail = findViewById(R.id.emp_detail_contact_email);
        empTel = findViewById(R.id.emp_detail_contact_number);
        empLogo = findViewById(R.id.emp_detail_logo);

        empAboutLayout = findViewById(R.id.emp_detail_about_layout);
        empAddressLayout = findViewById(R.id.emp_detail_address_layout);
        empEmailLayout = findViewById(R.id.emp_detail_contact_email_layout);
        empTelLayout = findViewById(R.id.emp_detail_contact_number_layout);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mRecyclerView = findViewById(R.id.jobRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mainLayout.setVisibility(View.GONE);
        empAddressLayout.setVisibility(View.GONE);
        empAboutLayout.setVisibility(View.GONE);
        empEmailLayout.setVisibility(View.GONE);
        empTelLayout.setVisibility(View.GONE);

    }

    private void loadEmpDetail() {
        Velocity.post(utils.UTILITIES_URL+"getEmpDetail/"+empId)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(!Objects.equals(response.body, "")){
                            Log.d(TAG, response.body);
                            Log.d(TAG, "Emp Details GET");
                            currentEmp = response.deserialize(Employer.class);
                            empList.put(currentEmp.getId(), currentEmp);
                            loadAllJob(0, 5);
                        }else{
                            Log.e(TAG, "Error Getting Emp Details");
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "GETEMPDETAIL: Error Connect to server.");
                    }
                });
    }

    //TODO: Load More

    private void loadAllJob(int offset, int limit) {
        Velocity.post(utils.UTILITIES_URL+"getJobByEmp/"+empId)
                .withFormData("uid", usrHelper.getUserId())
                .withFormData("offset", String.valueOf(offset))
                .withFormData("limit", String.valueOf(limit))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Jobs job = gson.fromJson(mJson, Jobs.class);
                            jobsList.add(job);
                        }

                        setAdapter();
                        Log.d(TAG, "JOBLOADDED: "+response.body);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, "GETJOBLIST: Error Connect to server.");
                    }
                });
    }

    private void setAdapter() {
        featuredJobAdapter = new FeaturedJobAdapter(this, jobsList, empList, mRecyclerView);
        mRecyclerView.setAdapter(featuredJobAdapter);

        setupView();

        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void setupView() {
        getSupportActionBar().setTitle(currentEmp.getName());

        empName.setText(currentEmp.getName());
        empCat.setText(currentEmp.getCategory());

        if(!Objects.equals(currentEmp.getAbout(), "")){
            empAbout.setText(Html.fromHtml(currentEmp.getAbout()));
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
}
