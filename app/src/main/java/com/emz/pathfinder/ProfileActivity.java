package com.emz.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";

    private int uid;
    private Utils utils;
    private Users user;

    private TextView nameTV, emailTV;
    private CircleImageView profilePic;
    private Button addFriendBtn;
    private ImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().getExtras() == null) {
            finish();
        }

        bindView();

        utils = new Utils(this);

        uid = getIntent().getExtras().getInt("id");

        loadUser(uid);
    }

    private void bindView() {
        nameTV = findViewById(R.id.user_profile_name);
        emailTV = findViewById(R.id.user_email);
        profilePic = findViewById(R.id.user_profile_photo);
        addFriendBtn = findViewById(R.id.add_friend_btn);
        headerImage = findViewById(R.id.header_cover_image);
    }

    private void loadUser(int uid) {
        Velocity.get(utils.USER_URL)
                .withPathParam("status", "userloader")
                .withPathParam("id", String.valueOf(uid))
                .withHeader("Content-Type","text/javascript;charset=utf-8")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        user = response.deserialize(Users.class);
                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        noConnection();
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void setupView() {
        nameTV.setText(user.getFname()+" "+user.getLname());
        emailTV.setText(user.getEmail());
        Glide.with(this).load(utils.PROFILEPIC_URL+user.getProPic()).apply(RequestOptions.centerInsideTransform().error(R.drawable.defaultprofilepicture)).into(profilePic);
        Glide.with(this).load("https://www.pathfinder.in.th/themes/images/backgrounds/top.jpg").into(headerImage);
    }

    private void noConnection() {

    }
}
