package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.EmployerDetailActivity;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.List;

public class FeaturedEmpAdapter extends RecyclerView.Adapter<FeaturedEmpAdapter.MyViewHolder> {

    private Context context;
    private List<Employer> empList;

    private Utils utils;

    public FeaturedEmpAdapter(Context context, List<Employer> empList) {
        utils = new Utils(context);

        this.context = context;
        this.empList = empList;
    }

    @Override
    public FeaturedEmpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employer, parent, false);
        return new FeaturedEmpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturedEmpAdapter.MyViewHolder holder, int position) {
        Employer emp = empList.get(position);
        Glide.with(context).load(utils.EMPPIC_URL+emp.getLogo()).apply(RequestOptions.centerInsideTransform().error(R.drawable.default_emp_logo)).into(holder.empLogo);

        final int id = emp.getId();
        holder.empCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmployerDetailActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout empCard;
        ImageView empLogo;
        MyViewHolder(View itemView) {
            super(itemView);

            empCard = itemView.findViewById(R.id.empCard);
            empLogo = itemView.findViewById(R.id.emp_logo);
        }
    }
}
