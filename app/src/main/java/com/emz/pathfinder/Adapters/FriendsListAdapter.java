package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.ProfileActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.StartActivity;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {
    private Context context;
    private List<Users> friendsList;

    private Utils utils;
    private UserHelper usrHelper;

    public FriendsListAdapter(Context context, List<Users> friendsList) {
        utils = new Utils(context);
        usrHelper = new UserHelper(context);

        this.context = context;
        this.friendsList = friendsList;
    }


    @Override
    public FriendsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendsListAdapter.MyViewHolder holder, int position) {
        Users user = friendsList.get(position);
        holder.nameTv.setText(user.getFullName());
        holder.emailTv.setText(user.getEmail());
        Glide.with(context).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.profilePic);

        final int id = user.getId();

        holder.friendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout friendCard;
        TextView nameTv, emailTv;
        Button actionBtn;
        ImageView profilePic;

        MyViewHolder(View itemView) {
            super(itemView);

            friendCard = itemView.findViewById(R.id.friend_card);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            profilePic = itemView.findViewById(R.id.profilePic);
        }

        String setActionButton(int status) {
            switch (status){
                case 0:
                    return "Add Friend";
                case 1:
                    return "Request Sent";
                case 2:
                    return "Accept Request";
                case 3:
                    return "Friend";
                case 4:
                    return "Edit Profile";
            }
            return null;
        }
    }
}
