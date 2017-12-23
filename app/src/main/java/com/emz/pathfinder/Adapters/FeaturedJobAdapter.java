package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Fragments.JobPortalFragment;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.JobDetailActivity;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import java.util.LinkedHashMap;
import java.util.List;

public class FeaturedJobAdapter extends RecyclerView.Adapter<FeaturedJobAdapter.MyViewHolder> {

    private Context context;
    private List<Jobs> jobList;
    private LinkedHashMap<Integer, Employer> empList;

    private Utils utils;

    public FeaturedJobAdapter(Context context, List<Jobs> jobList, LinkedHashMap<Integer, Employer> empList){
        utils = new Utils(context);
        this.context = context;
        this.jobList = jobList;
        this.empList = empList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Jobs job = jobList.get(position);
        final int jobid = job.getId();
        Employer emp = empList.get(job.getCompany_id());

        holder.jobTitle.setText(job.getName());
        holder.empName.setText(emp.getName());
        holder.location.setText(job.getLocation());
        holder.money.setText(job.getSalary());
        Glide.with(context).load(utils.EMPPIC_URL+emp.getLogo()).apply(RequestOptions.centerInsideTransform().error(R.drawable.default_emp_logo)).into(holder.employerLogo);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent post = new Intent(context, JobDetailActivity.class);
                post.putExtra("id", jobid);
                context.startActivity(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView employerLogo;
        TextView jobTitle, empName, location, money;

        MyViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.job_card);
            employerLogo = itemView.findViewById(R.id.employer_logo);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            empName = itemView.findViewById(R.id.empName);
            location = itemView.findViewById(R.id.locationName);
            money = itemView.findViewById(R.id.moneyText);
        }
    }
}
