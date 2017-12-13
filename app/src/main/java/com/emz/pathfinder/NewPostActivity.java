package com.emz.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emz.pathfinder.Adapters.TimelineAdapter;
import com.emz.pathfinder.Fragments.TimelineFragment;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.rw.velocity.Velocity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.emz.pathfinder.Utils.Ui.createProgressDialog;
import static com.emz.pathfinder.Utils.Ui.dismissProgressDialog;

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
        profile_pic = findViewById(R.id.newpost_propic);
        nameTv = findViewById(R.id.newpost_name);
        statusText = findViewById(R.id.newpost_status);
        galleryBtn = findViewById(R.id.btn_gallery);
        cameraBtn = findViewById(R.id.btn_camera);
        postBtn = findViewById(R.id.btn_post);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPost();
            }
        });

        loadUser(usrHelper.getUserId());
    }

    private void createNewPost() {
        String postcontent = statusText.getText().toString();
        createProgressDialog(this, getString(R.string.creating_new_post));
        Velocity.post(utils.TIMELINE_URL+"/post/")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("message", postcontent)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        dismissProgressDialog();
                        TimelineFragment.updateList();
                        finish();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        noConnection();
                        Log.e("TEST", String.valueOf(R.string.no_internet_connection));
                    }
                });
    }

    private void loadUser(String userId) {
        Velocity.get(utils.UTILITIES_URL+"getProfile/"+userId)
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
        nameTv.setText(users.getFullName());
        Glide.with(profile_pic.getContext()).load(utils.PROFILEPIC_URL+users.getProPic()).apply(RequestOptions.centerCropTransform().error(R.drawable.defaultprofilepicture)).into(profile_pic);
    }

    private void noConnection() {
        
    }
}
