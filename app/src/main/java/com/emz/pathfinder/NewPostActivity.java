package com.emz.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewPostActivity extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView nameTv;
    EditText statusText;
    Button galleryBtn, cameraBtn, postBtn;

    private UserHelper usrHelper;
    private Users users;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        utils = new Utils(this);

        usrHelper = new UserHelper(this);

        bindView();
    }

    private void bindView() {
        profile_pic = findViewById(R.id.newpost_propic);
        nameTv = findViewById(R.id.newpost_name);
        statusText = findViewById(R.id.newpost_status);
        galleryBtn = findViewById(R.id.btn_gallery);
        cameraBtn = findViewById(R.id.btn_camera);
        postBtn = findViewById(R.id.btn_post);

        loadUser(usrHelper.getUserId());
    }

    private void loadUser(String userId) {
        Velocity.get(utils.USER_URL)
                .withPathParam("status", "userloader")
                .withPathParam("id", userId)
                .withHeader("Content-Type","text/javascript;charset=utf-8")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        users = response.deserialize(Users.class);
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
        String fullname = users.getFname()+" "+users.getLname();
        nameTv.setText(fullname);
        Glide.with(profile_pic.getContext()).load(utils.PROFILEPIC_URL+users.getProPic()).apply(RequestOptions.centerCropTransform().error(R.drawable.defaultprofilepicture)).into(profile_pic);
    }

    private void noConnection() {
        
    }
}
