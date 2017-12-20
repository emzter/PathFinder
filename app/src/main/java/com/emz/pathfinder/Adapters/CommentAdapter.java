package com.emz.pathfinder.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
import com.emz.pathfinder.Models.Comments;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.PostActivity;
import com.emz.pathfinder.R;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;

import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comments> commentsList;
    private HashMap<Integer, Users> usersList;
    private RecyclerView mRecyclerView;
    private PostActivity postActivity;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    private Utils utils;
    private UserHelper usrHelper;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading = false;
    private boolean isMoreDataAvailable = true;

    public CommentAdapter(Context context, List<Comments> commentsList, HashMap<Integer, Users> usersList, RecyclerView mRecyclerView, PostActivity postActivity) {
        utils = new Utils(context);
        usrHelper = new UserHelper(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                Log.d("TimelineAdapter", "TOTAL: "+totalItemCount+" LAST: "+lastVisibleItem);

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && isMoreDataAvailable) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                        Log.d("CommentAdapter", "LOADMORE");
                    }
                    loading = true;
                }
            }
        });

        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
        this.mRecyclerView = mRecyclerView;
        this.postActivity = postActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_comment, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Comments comment = commentsList.get(position);
        Users user = usersList.get(comment.getAuthor());

        holder.nameTv.setText(user.getFullName());
        String time = utils.gettimestamp(comment.getCreateAt());
        holder.timeStampTv.setText(time);
        holder.mainTv.setText(comment.getMessage());
        holder.likeBtnText.setText(" " + comment.getLikeCount());

        if (comment.isLikeStatus()) {
            holder.likeBtnIcon.setTextColor(context.getResources().getColor(R.color.liked));
            holder.likeBtnText.setTextColor(context.getResources().getColor(R.color.liked));
        } else {
            holder.likeBtnIcon.setTextColor(context.getResources().getColor(R.color.monsoon));
            holder.likeBtnText.setTextColor(context.getResources().getColor(R.color.monsoon));
        }

        Glide.with(context).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(holder.profilePic);
        final int likePosition = position;
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CommentAdapter", "LIKE");
                postActivity.createLike(likePosition, usrHelper.getUserId(), String.valueOf(comment.getId()), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, timeStampTv, mainTv, likeBtnIcon, likeBtnText;
        ImageView profilePic;
        LinearLayout likeBtn;
        MyViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.feed_name);
            timeStampTv = itemView.findViewById(R.id.feed_timestamp);
            mainTv = itemView.findViewById(R.id.feed_main_text);
            likeBtnIcon = itemView.findViewById(R.id.likeBtnIcon);
            likeBtnText = itemView.findViewById(R.id.likeBtnText);
            profilePic = itemView.findViewById(R.id.feed_profile_pic);
            likeBtn = itemView.findViewById(R.id.feed_button_like);
        }
    }
}
