package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.ProfileActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.StartActivity;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

    private final UserHelper usrHelper;
    private Context context;
    private List<Posts> postsList;
    private HashMap<Integer, Users> usersList;
    private TimelineFragment timelineFragment;

    private Utils utils;

    public TimelineAdapter(Context context, HashMap<Integer, Users> usersList, List<Posts> postsList, TimelineFragment timelineFragment) {
        utils = new Utils(context);
        usrHelper = new UserHelper(context);

        this.timelineFragment = timelineFragment;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Posts post = postsList.get(position);
        final Users user = usersList.get(post.getAuthor());

        if(post.getRecipient() == 0){
            holder.name2TV.setText("");
            holder.toTV.setText("");
        }else{
            final Users recipient = usersList.get(post.getRecipient());
            holder.name2TV.setText(recipient.getFname()+" "+recipient.getLname());
            holder.toTV.setText("▶");

            View.OnClickListener openProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(context, ProfileActivity.class);
                    profile.putExtra("id", recipient.getId());
                    context.startActivity(profile);
                }
            };

            holder.name2TV.setOnClickListener(openProfile);
        }

        holder.nameTV.setText(user.getFname()+" "+user.getLname());
        holder.mainTV.setText(post.getMessage());
        String time = utils.gettimestamp(post.getCreated());
        holder.timestampTV.setText(time);
        holder.likeTV.setText(" "+post.getLikeCount());
        holder.commentTV.setText(" "+post.getCommentCount());

        if(post.getLikeStatus()){
            holder.likeTV.setTextColor(context.getResources().getColor(R.color.liked));
            holder.likeIcon.setTextColor(context.getResources().getColor(R.color.liked));
        }else{
            holder.likeTV.setTextColor(context.getResources().getColor(R.color.monsoon));
            holder.likeIcon.setTextColor(context.getResources().getColor(R.color.monsoon));
        }

        Glide.with(context).load(utils.PROFILEPIC_URL+user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.profilePic);

        View.OnClickListener openProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(context, ProfileActivity.class);
                profile.putExtra("id", user.getId());
                context.startActivity(profile);
            }
        };

        holder.nameTV.setOnClickListener(openProfile);
        holder.profilePic.setOnClickListener(openProfile);

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timelineFragment.like(position, usrHelper.getUserId(), String.valueOf(post.getId()), "0");
            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postAct = new Intent(context, PostActivity.class);
                postAct.putExtra("id", post.getId());
                context.startActivity(postAct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView timestampTV;
        TextView mainTV;
        TextView toTV;
        TextView name2TV;
        public TextView likeIcon;
        public TextView likeTV;
        TextView commentTV;
        TextView shareTV;
        ImageView profilePic;

        LinearLayout likeBtn, commentBtn, shareBtn;

        MyViewHolder(View itemView) {
            super(itemView);

            likeBtn= itemView.findViewById(R.id.feed_button_like);
            commentBtn= itemView.findViewById(R.id.feed_button_reply);
            shareBtn= itemView.findViewById(R.id.feed_button_share);

            nameTV = itemView.findViewById(R.id.feed_name);
            name2TV = itemView.findViewById(R.id.feed_2nd_name);
            toTV = itemView.findViewById(R.id.feed_to);
            timestampTV = itemView.findViewById(R.id.feed_timestamp);
            mainTV = itemView.findViewById(R.id.feed_main_text);
            profilePic = itemView.findViewById(R.id.feed_profile_pic);
            likeTV = itemView.findViewById(R.id.likeBtnText);
            likeIcon = itemView.findViewById(R.id.likeBtnIcon);
            commentTV = itemView.findViewById(R.id.commentBtnText);
            shareTV = itemView.findViewById(R.id.shareBtnText);
        }
    }
}