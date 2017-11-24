package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

    private Context context;
    private List<Posts> postsList;
    private HashMap<Integer, Users> usersList;

    private Utils utils;

    public TimelineAdapter(Context context, HashMap<Integer, Users> usersList, List<Posts> postsList) {
        utils = new Utils(context);
        this.context = context;
        this.postsList = postsList;
        this.usersList = usersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Posts post = postsList.get(position);
        Users user = usersList.get(post.getAuthor());
        if(post.getRecipient() == 0){
            holder.name2TV.setText("");
            holder.toTV.setText("");
        }else{
            Users recipient = usersList.get(post.getRecipient());
            holder.name2TV.setText(recipient.getFname()+" "+recipient.getLname());
            holder.toTV.setText("▶");
        }
        holder.nameTV.setText(user.getFname()+" "+user.getLname());
        holder.mainTV.setText(post.getMessage());
        String time = utils.gettimestamp(post.getCreated());
        holder.timestampTV.setText(time);
        Glide.with(context).load(utils.PROFILEPIC_URL+user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, timestampTV, mainTV, toTV, name2TV;
        ImageView profilePic;
        MyViewHolder(View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.feed_name);
            name2TV = itemView.findViewById(R.id.feed_2nd_name);
            toTV = itemView.findViewById(R.id.feed_to);
            timestampTV = itemView.findViewById(R.id.feed_timestamp);
            mainTV = itemView.findViewById(R.id.feed_main_text);
            profilePic = itemView.findViewById(R.id.feed_profile_pic);
        }

        private void timestamp(String datetime) {

        }
    }
}
