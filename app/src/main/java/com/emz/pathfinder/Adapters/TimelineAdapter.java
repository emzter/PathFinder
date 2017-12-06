package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.ProfileActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.Ui;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final UserHelper usrHelper;
    private Context context;
    private List<Posts> postsList;
    private HashMap<Integer, Users> usersList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private Utils utils;

    public TimelineAdapter(Context context, HashMap<Integer, Users> usersList, List<Posts> postsList, RecyclerView recyclerView) {
        utils = new Utils(context);
        usrHelper = new UserHelper(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                    Ui.createSnackbar(recyclerView, "No More");
                }else{
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }

                loading = true;
            }
        });

        this.context = context;
        this.postsList = postsList;
        this.usersList = usersList;
    }

    @Override
    public int getItemViewType(int position) {
        return postsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_cardview, parent, false);
            vh = new PostViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostViewHolder) {
            final Posts post = postsList.get(position);
            final Users user = usersList.get(post.getAuthor());

            if (post.getRecipient() == 0) {
                ((PostViewHolder) holder).name2TV.setText("");
                ((PostViewHolder) holder).toTV.setText("");
            } else {
                final Users recipient = usersList.get(post.getRecipient());
                ((PostViewHolder) holder).name2TV.setText(recipient.getFname() + " " + recipient.getLname());
                ((PostViewHolder) holder).toTV.setText("▶");

                View.OnClickListener openProfile = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile = new Intent(context, ProfileActivity.class);
                        profile.putExtra("id", recipient.getId());
                        context.startActivity(profile);
                    }
                };

                ((PostViewHolder) holder).name2TV.setOnClickListener(openProfile);
            }

            ((PostViewHolder) holder).nameTV.setText(user.getFname() + " " + user.getLname());
            ((PostViewHolder) holder).mainTV.setText(post.getMessage());
            String time = utils.gettimestamp(post.getCreated());
            ((PostViewHolder) holder).timestampTV.setText(time);
            ((PostViewHolder) holder).likeTV.setText(" " + post.getLikeCount());
            ((PostViewHolder) holder).commentTV.setText(" " + post.getCommentCount());

            if (post.getLikeStatus()) {
                ((PostViewHolder) holder).likeTV.setTextColor(context.getResources().getColor(R.color.liked));
                ((PostViewHolder) holder).likeIcon.setTextColor(context.getResources().getColor(R.color.liked));
            } else {
                ((PostViewHolder) holder).likeTV.setTextColor(context.getResources().getColor(R.color.monsoon));
                ((PostViewHolder) holder).likeIcon.setTextColor(context.getResources().getColor(R.color.monsoon));
            }

            Glide.with(context).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(((PostViewHolder) holder).profilePic);

            View.OnClickListener openProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(context, ProfileActivity.class);
                    profile.putExtra("id", user.getId());
                    context.startActivity(profile);
                }
            };

            ((PostViewHolder) holder).nameTV.setOnClickListener(openProfile);
            ((PostViewHolder) holder).profilePic.setOnClickListener(openProfile);

            ((PostViewHolder) holder).commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postAct = new Intent(context, PostActivity.class);
                    postAct.putExtra("id", post.getId());
                    context.startActivity(postAct);
                }
            });
        }else{
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView timestampTV;
        TextView mainTV;
        TextView toTV;
        TextView name2TV;
        TextView likeIcon;
        TextView likeTV;
        TextView commentTV;
        TextView shareTV;
        ImageView profilePic;

        LinearLayout likeBtn, commentBtn, shareBtn;

        private PostViewHolder(View itemView) {
            super(itemView);

            likeBtn = itemView.findViewById(R.id.feed_button_like);
            commentBtn = itemView.findViewById(R.id.feed_button_reply);
            shareBtn = itemView.findViewById(R.id.feed_button_share);

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

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        private ProgressViewHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }
}