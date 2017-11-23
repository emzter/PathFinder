package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.Models.Jobs;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class FeaturedJobAdapter extends RecyclerView.Adapter<FeaturedJobAdapter.MyViewHolder> {

    private Context context;
    private List<Jobs> jobList;
    private HashMap<Integer, Employer> empList;

    private Utils utils = new Utils(context);

    public FeaturedJobAdapter(Context context, List<Jobs> jobList, HashMap<Integer, Employer> empList){
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Jobs job = jobList.get(position);
        Employer emp = empList.get(job.getCompany_id());
        holder.jobTitle.setText(job.getName());
//        holder.employerName.setText(emp.getName());
        Glide.with(context).load(utils.EMPPIC_URL+emp.getLogo()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.employerLogo);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView employerLogo;
        TextView jobTitle;
        public MyViewHolder(View itemView) {
            super(itemView);

            employerLogo = itemView.findViewById(R.id.employer_logo);
            jobTitle = itemView.findViewById(R.id.jobTitle);
//            employerName = itemView.findViewById(R.id.empName);
        }
    }
}
