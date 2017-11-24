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
import com.emz.pathfinder.Models.Notifications;
import com.emz.pathfinder.Models.Users;
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
        Users user = userLists.get(noti.getSender());
        holder.nameTV.setText(user.getFname()+" "+user.getLname());
        holder.statusTV.setText(noti.getType());
        holder.timeTV.setText(noti.getCreated());
        Glide.with(context).load(utils.PROFILEPIC_URL+user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.proPic);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView proPic;
        TextView nameTV, statusTV, timeTV;

        private MyViewHolder(View itemView) {
            super(itemView);

            proPic = itemView.findViewById(R.id.noti_profile_pic);
            nameTV = itemView.findViewById(R.id.noti_name);
            statusTV = itemView.findViewById(R.id.noti_status);
            timeTV = itemView.findViewById(R.id.noti_timestamp);
        }
    }
}
