package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.EmployerDetailActivity;
import com.emz.pathfinder.Models.Employer;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.List;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.MyViewHolder> {
    private Context context;
    private List<Employer> employerList;

    private Utils utils;

    public EmployerAdapter(Context context, List<Employer> employerList) {
        utils = new Utils(context);

        this.context = context;
        this.employerList = employerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emp_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Employer emp = employerList.get(position);

        holder.nameTv.setText(emp.getName());
        holder.catTv.setText(emp.getCategory());
        holder.phoneTv.setText(emp.getTelephone());

        Glide.with(context).load(utils.EMPPIC_URL + emp.getLogo()).apply(RequestOptions.centerInsideTransform().error(R.drawable.default_emp_logo)).into(holder.logo);

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
        return employerList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout empCard;
        TextView nameTv, catTv, phoneTv;
        ImageView logo;

        MyViewHolder(View itemView) {
            super(itemView);

            empCard = itemView.findViewById(R.id.emp_card);
            nameTv = itemView.findViewById(R.id.nameTv);
            catTv = itemView.findViewById(R.id.categoryTv);
            phoneTv = itemView.findViewById(R.id.telephoneTv);
            logo = itemView.findViewById(R.id.emp_logo);
        }
    }
}
