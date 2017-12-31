package com.emz.pathfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.emz.pathfinder.Models.VolunteerCategory;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class CreateProfileActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private ImageView cameraIcon;
    private TextView genderText, birthDateText, disabilityText;
    private EditText nameEt, lastNameEt;
    private LinearLayout genderPicker, birthdatePicker, disabilityPicker;

    private MaterialDialog materialDialog;

    private Utils utils;

    private int gender = -1, disability = -1;
    ArrayList<String> disabilityOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Velocity.initialize(3);

        utils = new Utils(this);

        getDisabilityType();
    }

    private void getDisabilityType(){
        Velocity.get(utils.USER_URL+"/getAllDisabilityType")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonArray jsonArray = parser.parse(response.body).getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonElement mJson = jsonArray.get(i);
                            disabilityOptions.add(mJson.getAsString());
                        }

                        bindView();
                        setupView();
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }

    private void bindView() {
        profilePic = findViewById(R.id.profile_picture);
        cameraIcon = findViewById(R.id.cameraIcon);
        nameEt = findViewById(R.id.input_name);
        lastNameEt = findViewById(R.id.input_lastname);
        genderPicker = findViewById(R.id.gender_picker);
        birthdatePicker = findViewById(R.id.birthdate_picker);
        disabilityPicker = findViewById(R.id.disability_picker);
        genderText = findViewById(R.id.gender_text);
        birthDateText = findViewById(R.id.birthdate_text);
        disabilityText = findViewById(R.id.disability_text);
    }

    private void setupView() {
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(CreateProfileActivity.this, "Choose your profile picuture", 0);
            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(CreateProfileActivity.this, "Choose your profile picuture", 0);
            }
        });

        genderPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog.Builder(CreateProfileActivity.this)
                        .title("Select your gender")
                        .items(R.array.gender_array)
                        .itemsCallbackSingleChoice(gender, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                gender = which;
                                setGenderText();
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .negativeText(R.string.cancel)
                        .show();
            }
        });

        disabilityPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog.Builder(CreateProfileActivity.this)
                        .title("Explain how is your disability")
                        .items(disabilityOptions)
                        .itemsCallbackSingleChoice(gender, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                disability = which;
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .negativeText(R.string.cancel)
                        .show();
            }
        });
//        String[] genderArray = getResources().getStringArray(R.array.gender_array);
//        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, R.layout.spinner_text_item, genderArray);
//        genderSpinner.setAdapter(adapterGender);
    }

    private void setGenderText() {
        if(gender == 0){
            genderText.setText(R.string.male);
        }else if(gender == 1){
            genderText.setText(R.string.female);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Glide.with(CreateProfileActivity.this).load(imageFile).into(profilePic);
            }
        });
    }
}
