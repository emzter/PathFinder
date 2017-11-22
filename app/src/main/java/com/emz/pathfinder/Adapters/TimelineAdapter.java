package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.rw.velocity.Velocity;

import java.util.List;

import static com.emz.pathfinder.Utils.Utils.PROFILEPIC_URL;
import static com.emz.pathfinder.Utils.Utils.USER_URL;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

    private Context context;
    private List<Posts> postsList;

    public TimelineAdapter(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Posts post = postsList.get(position);
        Velocity.get(USER_URL)
                .withPathParam("status", "userloader")
                .withPathParam("id", String.valueOf(post.getAuthor()))
                .withHeader("Content-Type","text/javascript;charset=utf-8")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(post.getRecipient() == 0){
                            holder.name2TV.setText("");
                            holder.toTV.setText("");
                        }
                        Users user = response.deserialize(Users.class);
                        holder.nameTV.setText(user.getFirst_name()+" "+user.getLast_name());
                        holder.mainTV.setText(post.getMessage());
                        Glide.with(context).load(PROFILEPIC_URL+user.getProfile_image()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.profilePic);
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
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

        private void loadUser(String userId) {

        }
    }
}
