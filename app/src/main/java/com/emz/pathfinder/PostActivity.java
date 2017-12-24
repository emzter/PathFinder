package com.emz.pathfinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Adapters.CommentAdapter;
import com.emz.pathfinder.Adapters.TimelineAdapter;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Listeners.OnLoadMoreListener;
import com.emz.pathfinder.Models.Comments;
import com.emz.pathfinder.Models.Posts;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = PostActivity.class.getSimpleName();

    private int postId;
    private Posts post;
    private HashMap<Integer, Users> usersList;

    private List<Comments> commentsList;

    TextView nameTV, timestampTV, mainTV, toTV, name2TV, likeIcon, likeTV, commentTV;
    ImageView profilePic;
    LinearLayout likeBtn, commentBtn;
    EditText commentEt;
    Button makeCommentBtn;

    private Utils utils;
    private UserHelper usrHelper;

    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        utils = new Utils(this);
        usrHelper = new UserHelper(this);
        handler = new Handler();

        if(getIntent().getExtras() != null){
            commentsList = new ArrayList<>();
            if(getIntent().getExtras().get("post") != null){
                post = (Posts) getIntent().getExtras().get("post");
                usersList = (HashMap<Integer, Users>) getIntent().getExtras().get("userLists");
                bindView();
            }else{
                postId = getIntent().getExtras().getInt("postid");
                Log.d(TAG, "POSTID: "+postId);
                usersList = new HashMap<>();
                loadPost();
            }
        }else{
            finish();
        }
    }

    private void loadPost() {
        Velocity.post(utils.UTILITIES_URL+"getPostById/"+postId)
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, "GET POST DETAIL");
                        post = response.deserialize(Posts.class);
                        loadUserList();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.d(TAG, "FAILED TO GET POST DETAIL");
                        //TODO: Connection Failed
                    }
                });
    }

    private void loadUserList() {
        Velocity.post(utils.UTILITIES_URL+"getAllProfiles")
                .withFormData("id", usrHelper.getUserId())
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for(int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            Users user = gson.fromJson(mJson, Users.class);
                            usersList.put(user.getId(), user);
                        }

                        Log.d(TAG, "USERS LOADDED");

                        bindView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, getResources().getString(R.string.no_internet_connection));
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindView() {
        nameTV = findViewById(R.id.feed_name);
        timestampTV = findViewById(R.id.feed_timestamp);
        mainTV = findViewById(R.id.feed_main_text);
        toTV = findViewById(R.id.feed_to);
        name2TV = findViewById(R.id.feed_2nd_name);
        likeIcon = findViewById(R.id.likeBtnIcon);
        likeTV = findViewById(R.id.likeBtnText);
        commentTV = findViewById(R.id.commentBtnText);
        profilePic = findViewById(R.id.feed_profile_pic);
        likeBtn = findViewById(R.id.feed_button_like);
        commentBtn = findViewById(R.id.feed_button_reply);
        commentEt = findViewById(R.id.commentEt);

        makeCommentBtn = findViewById(R.id.btn_comment);

        mRecyclerView = findViewById(R.id.commentRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        setupView();
        loadComment(0, 10);
    }

    void refreshItems(){
        loadComment(0, 10);
    }

    private void loadComment(int offset, int limit) {
        if(offset == 0){
            if(commentsList.size() > 0){
                commentsList.clear();
            }

            if(mAdapter != null){
                mAdapter.setMoreDataAvailable(true);
            }
        }
        Velocity.post(utils.UTILITIES_URL+"commentload/"+post.getId())
                .withFormData("id", usrHelper.getUserId())
                .withFormData("offset", String.valueOf(offset))
                .withFormData("limit", String.valueOf(limit))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, "COMMENTS: "+response.body);
                        Log.d(TAG, "COMMENTS: "+response.requestUrl);
                        if(response.body != ""){
                            Gson gson = new Gson();
                            JsonParser parser = new JsonParser();
                            JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                            for(int i = 0; i < jsonArray.size(); i++) {
                                JsonElement mJson = jsonArray.get(i);
                                Comments comment = gson.fromJson(mJson, Comments.class);
                                commentsList.add(comment);
                            }

                            Log.d(TAG, "COMMENTS LOADDED");
                            int size = commentsList.size();
                            Log.d(TAG, "COMMENTS: "+String.valueOf(size));

                            if(mRecyclerView.getAdapter() == null){
                                Log.d(TAG, "ADAPTER SET");
                                setAdapter();
                            }else{
                                Log.d(TAG, "COMMENTS REFRESHED");
                                mAdapter.notifyDataSetChanged();
                            }
                        }else{
                            mAdapter.setMoreDataAvailable(false);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: NOCONNECTION
                    }
                });
    }

    private void setAdapter() {
        mAdapter = new CommentAdapter(this, commentsList, usersList, mRecyclerView, PostActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(commentsList.size() > 0){
                            int start = commentsList.get(commentsList.size() - 1).getId();
                            int end = start + 10;

                            Log.d(TAG, "POST End"+end);

                            loadComment(start, end);

                            mAdapter.setLoaded();
                        }
                    }
                }, 2000);
            }
        });

        Log.d(TAG, "COMMENTS: "+mAdapter.getItemCount());
    }

    private void setupView() {
        final Users user = usersList.get(post.getAuthor());

        if (post.getRecipient() == 0) {
            name2TV.setText("");
            toTV.setText("");
        } else {
            final Users recipient = usersList.get(post.getRecipient());
            name2TV.setText(recipient.getFullName());
            toTV.setText("▶");

            View.OnClickListener openProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(PostActivity.this, ProfileActivity.class);
                    profile.putExtra("id", recipient.getId());
                    startActivity(profile);
                }
            };

            name2TV.setOnClickListener(openProfile);
        }

        View.OnClickListener openProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(PostActivity.this, ProfileActivity.class);
                profile.putExtra("id", user.getId());
                startActivity(profile);
            }
        };

        nameTV.setOnClickListener(openProfile);
        profilePic.setOnClickListener(openProfile);

        nameTV.setText(user.getFullName());
        mainTV.setText(post.getMessage());
        String time = utils.gettimestamp(post.getCreated());
        timestampTV.setText(time);
        commentTV.setText(" " + post.getCommentCount());

        setLikeButton();

        Glide.with(this).load(utils.PROFILEPIC_URL + user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(profilePic);

        makeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEt.getText().toString();
                createComment(comment);
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLike(-1, usrHelper.getUserId(), String.valueOf(post.getId()), 0);
            }
        });

        //TODO: Open Profile

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentEt, InputMethodManager.SHOW_IMPLICIT);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentEt.requestFocus();
            }
        });
    }

    private void setLikeButton() {
        likeTV.setText(" " + post.getLikeCount());

        if (post.getLikeStatus()) {
            likeTV.setTextColor(getResources().getColor(R.color.liked));
            likeIcon.setTextColor(getResources().getColor(R.color.liked));
        } else {
            likeTV.setTextColor(getResources().getColor(R.color.monsoon));
            likeIcon.setTextColor(getResources().getColor(R.color.monsoon));
        }
    }

    private void createComment(String comment) {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog_title)
                .content(R.string.creating_new_post)
                .progress(true, 0)
                .cancelable(false)
                .show();

        Velocity.post(utils.TIMELINE_URL+"comment")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("pid", String.valueOf(post.getId()))
                .withFormData("message", comment)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        materialDialog.dismiss();
                        post.setCommentCount(post.getCommentCount() + 1);
                        refreshItems();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        //TODO: No Connection
                    }
                });
    }

    public void createLike(final int position, String id, String pid, int type) {
        final String TAG = "LikeMethod";

        Velocity.post(utils.TIMELINE_URL+"like")
                .withFormData("id", pid)
                .withFormData("uid", id)
                .withFormData("type", String.valueOf(type))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);
                        if(response.body.equals("SuccessAdd")){
                            Log.d(TAG, "Doing New Like");
                            if(position == -1){
                                updatePost(true);
                            }else{
                                commentsList.get(position).setLikeStatus(true);
                                commentsList.get(position).setLikeCount(commentsList.get(position).getLikeCount() + 1);
                                mAdapter.notifyItemChanged(position);
                            }
                        }else if(response.body.equals("SuccessDelete")){
                            Log.d(TAG, "Doing Delete Like");
                            if(position == -1){
                                updatePost(false);
                            }else{
                                commentsList.get(position).setLikeStatus(false);
                                commentsList.get(position).setLikeCount(commentsList.get(position).getLikeCount() - 1);
                                mAdapter.notifyItemChanged(position);
                            }
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                    }
                });
    }

    private void updatePost(boolean add) {
        if(add){
            post.setLikeStatus(true);
            post.setLikeCount(post.getLikeCount() + 1);
        }else{
            post.setLikeStatus(false);
            if(post.getLikeCount() != 0){
                post.setLikeCount(post.getLikeCount() - 1);
            }
        }
        setLikeButton();
    }
}