package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emz.pathfinder.Models.Educations;
import com.emz.pathfinder.Models.Experiences;
import com.emz.pathfinder.R;

import java.util.List;

public class AboutEduAdapter extends RecyclerView.Adapter<AboutEduAdapter.MyViewHolder> {
    private Context context;
    private List<Educations> educationsList;

    public AboutEduAdapter(Context context, List<Educations> educationsList) {
        this.context = context;
        this.educationsList = educationsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_edu, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Educations edu = educationsList.get(position);

        holder.nameTv.setText(edu.getName());
        String background = holder.getBackgroundLevel(edu.getBackground());
        holder.levelTv.setText(background);
        holder.gpaTv.setText(String.valueOf(edu.getGpa()));
        holder.majorTv.setText(edu.getMajor());
    }

    @Override
    public int getItemCount() {
        return educationsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, levelTv, majorTv, gpaTv;

        MyViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.edu_insitute);
            levelTv = itemView.findViewById(R.id.edu_level);
            majorTv = itemView.findViewById(R.id.edu_section);
            gpaTv = itemView.findViewById(R.id.edu_gpa);
        }

        String getBackgroundLevel(int background){
            switch (background){
                case 1:
                    return context.getString(R.string.edu_1);
                case 2:
                    return context.getString(R.string.edu_2);
                case 3:
                    return context.getString(R.string.edu_3);
                case 4:
                    return context.getString(R.string.edu_4);
                case 5:
                    return context.getString(R.string.edu_5);
            }
            return null;
        }
    }
}
