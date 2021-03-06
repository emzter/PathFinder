package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emz.pathfinder.Models.Experiences;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.List;

public class AboutExpAdapter extends RecyclerView.Adapter<AboutExpAdapter.MyViewHolder> {
    private Context context;
    private List<Experiences> experiencesList;

    private Utils utils;

    public AboutExpAdapter(Context context, List<Experiences> experiencesList) {
        utils = new Utils(context);

        this.context = context;
        this.experiencesList = experiencesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_exp, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Experiences exp = experiencesList.get(position);

        holder.nameTv.setText(exp.getName());
        holder.empTv.setText(exp.getEmployer());
        if(exp.getStatus() == 1){
            holder.periodTv.setText(utils.parseDate(exp.getStart(), "yyyy-MM-dd", "MMM y")+" - Now");
        }else{
            holder.periodTv.setText(utils.parseDate(exp.getStart(), "yyyy-MM-dd", "MMM y")+" - "+utils.parseDate(exp.getEnd(), "yyyy-MM-dd", "MMM y"));
        }
    }

    @Override
    public int getItemCount() {
        return experiencesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, empTv, periodTv;
        MyViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.job_title);
            empTv = itemView.findViewById(R.id.emp_name);
            periodTv = itemView.findViewById(R.id.exp_period);
        }
    }
}
