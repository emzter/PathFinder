package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emz.pathfinder.Models.Categories;
import com.emz.pathfinder.R;

import java.util.List;

public class CategoryChipsAdapter extends RecyclerView.Adapter<CategoryChipsAdapter.MyViewHolder> {

    private Context context;
    private List<Categories> catList;

    public CategoryChipsAdapter(Context context, List<Categories> catList){
        this.context = context;
        this.catList = catList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_categories, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Categories categories = catList.get(position);
        holder.chipText.setText(categories.getName());
        holder.chipText.setTag(categories.getId());
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chipText;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
