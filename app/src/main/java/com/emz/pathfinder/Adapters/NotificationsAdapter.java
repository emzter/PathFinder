package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Notifications;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.ProfileActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    private Context context;
    private List<Notifications> notificationList;
    private HashMap<Integer, Users> userLists;

    private Utils utils;

    public NotificationsAdapter(Context context, List<Notifications> notificationList, HashMap<Integer, Users> userLists) {
        utils = new Utils(context);
        this.context = context;
        this.notificationList = notificationList;
        this.userLists = userLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notifications noti = notificationList.get(position);
        if(noti.getSender() != 0) {
            Users user = userLists.get(noti.getSender());
            holder.nameTV.setText(user.getFullName());
            holder.setStatusText(noti.getType());
            holder.setOnClickListener(noti.getType(), noti.getRef());
            holder.timeTV.setText(utils.gettimestamp(noti.getCreated()));
            Glide.with(context).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.proPic);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout notiCard;
        ImageView proPic;
        TextView nameTV, statusTV, timeTV;

        MyViewHolder(View itemView) {
            super(itemView);

            notiCard = itemView.findViewById(R.id.notiCard);
            proPic = itemView.findViewById(R.id.noti_profile_pic);
            nameTV = itemView.findViewById(R.id.noti_name);
            statusTV = itemView.findViewById(R.id.noti_status);
            timeTV = itemView.findViewById(R.id.noti_timestamp);
        }

        void setStatusText(String status){
            if(statusTV == null) return;
            String newmessage = "";

            switch (status){
                case "post.like":
                    newmessage = context.getString(R.string.loved_your_post);
                    break;
                case "post.comment":
                    newmessage = context.getString(R.string.comment_post);
                    break;
                case "post.comment.like":
                    newmessage = context.getString(R.string.loved_comment);
                    break;
                case "tag.post":
                    newmessage = context.getString(R.string.tagged_post);
                    break;
                case "tag.comment":
                    newmessage = context.getString(R.string.tagged_comment);
                    break;
                case "post.feed":
                    newmessage = context.getString(R.string.feed_posted);
                    break;
                case "dis.callhelp":
                    newmessage = context.getString(R.string.noti_callhelp);
                    break;
                case "friend.add":
                    newmessage = context.getString(R.string.noti_friend_add);
                    break;
                case "friend.accept":
                    newmessage = context.getString(R.string.noti_friend_accept);
                    break;
                case "relative.add":
                    newmessage = context.getString(R.string.noti_relative_add);
                    break;
                case "relative.accept":
                    newmessage = context.getString(R.string.noti_relative_accept);
                    break;
            }

            statusTV.setText(newmessage);
        }

        void setOnClickListener(final String status, final String ref){
            notiCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStartActivity(status, ref);
                }
            });
        }

        void setStartActivity(String status, String ref) {
            Intent intent;
            switch (status){
                case "post.like":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("\\d", "")));
                    context.startActivity(intent);
                    break;
                case "post.comment":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("\\d", "")));
                    context.startActivity(intent);
                    break;
                case "post.comment.like":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("\\d", "")));
                    context.startActivity(intent);
                    break;
                case "tag.post":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("\\d", "")));
                    context.startActivity(intent);
                    break;
                case "tag.comment":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
                case "post.feed":
                    intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postid", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
                case "friend.add":
                    intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("id", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
                case "friend.accept":
                    intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("id", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
                case "relative.add":
                    intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("id", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
                case "relative.accept":
                    intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("id", Integer.parseInt(ref.replaceAll("[\\D]", "")));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
