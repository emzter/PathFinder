package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.ProfileActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private List<Posts> postsList;
    private HashMap<Integer, Users> usersList;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private TimelineFragment timelineFragment;

    private Utils utils;
    private UserHelper usrHelper;
    private boolean isMoreDataAvailable = true;

    public TimelineAdapter(Context context, HashMap<Integer, Users> usersList, List<Posts> postsList, RecyclerView recyclerView, TimelineFragment timelineFragment) {
        utils = new Utils(context);
        usrHelper = new UserHelper(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                Log.d("TimelineAdapter", "TOTAL: "+totalItemCount+" LAST: "+lastVisibleItem);

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && isMoreDataAvailable) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                        Log.d("TimelineAdapter", "LOADMORE");
                    }
                    loading = true;
                }
            }
        });

        this.context = context;
        this.postsList = postsList;
        this.usersList = usersList;
        this.timelineFragment = timelineFragment;
    }

    @Override
    public int getItemViewType(int position) {
        return postsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_LOADING){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_cardview, parent, false);
            return new PostViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PostViewHolder){
            final Posts post = postsList.get(position);
            final Users user = usersList.get(post.getAuthor());

            PostViewHolder postViewHolder = (PostViewHolder) holder;

            if (post.getRecipient() == 0) {
                postViewHolder.name2TV.setText("");
                postViewHolder.toTV.setText("");
            } else {
                final Users recipient = usersList.get(post.getRecipient());
                postViewHolder.name2TV.setText(recipient.getFullName());
                postViewHolder.toTV.setText("▶");

                View.OnClickListener openProfile = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile = new Intent(context, ProfileActivity.class);
                        profile.putExtra("id", recipient.getId());
                        context.startActivity(profile);
                    }
                };

                postViewHolder.name2TV.setOnClickListener(openProfile);
            }

            postViewHolder.nameTV.setText(user.getFullName());
            postViewHolder.mainTV.setText(post.getMessage());
            String time = utils.gettimestamp(post.getCreated());
            postViewHolder.timestampTV.setText(time);
            postViewHolder.likeTV.setText(" " + post.getLikeCount());
            postViewHolder.commentTV.setText(" " + post.getCommentCount());

            if (post.getLikeStatus()) {
                postViewHolder.likeTV.setTextColor(context.getResources().getColor(R.color.liked));
                postViewHolder.likeIcon.setTextColor(context.getResources().getColor(R.color.liked));
            } else {
                postViewHolder.likeTV.setTextColor(context.getResources().getColor(R.color.monsoon));
                postViewHolder.likeIcon.setTextColor(context.getResources().getColor(R.color.monsoon));
            }

            Glide.with(context).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(postViewHolder.profilePic);

            View.OnClickListener openProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(context, ProfileActivity.class);
                    profile.putExtra("id", user.getId());
                    context.startActivity(profile);
                }
            };

            postViewHolder.nameTV.setOnClickListener(openProfile);
            postViewHolder.profilePic.setOnClickListener(openProfile);

            final int likePostion = position;
            postViewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timelineFragment.createLike(likePostion, usrHelper.getUserId(), String.valueOf(post.getId()), 0);
                }
            });

            postViewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postAct = new Intent(context, PostActivity.class);
                    postAct.putExtra("post", post);
                    postAct.putExtra("user", user);
                    postAct.putExtra("userLists", usersList);
                    context.startActivity(postAct);
                }
            });
        }else if(holder instanceof LoadViewHolder){
            LoadViewHolder loadViewHolder = (LoadViewHolder) holder;
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

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        loading = false;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
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
//            shareBtn = itemView.findViewById(R.id.feed_button_share);

            nameTV = itemView.findViewById(R.id.feed_name);
            name2TV = itemView.findViewById(R.id.feed_2nd_name);
            toTV = itemView.findViewById(R.id.feed_to);
            timestampTV = itemView.findViewById(R.id.feed_timestamp);
            mainTV = itemView.findViewById(R.id.feed_main_text);
            profilePic = itemView.findViewById(R.id.feed_profile_pic);
            likeTV = itemView.findViewById(R.id.likeBtnText);
            likeIcon = itemView.findViewById(R.id.likeBtnIcon);
            commentTV = itemView.findViewById(R.id.commentBtnText);
//            shareTV = itemView.findViewById(R.id.shareBtnText);
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        private LoadViewHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }
}