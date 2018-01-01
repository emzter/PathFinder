package com.emz.pathfinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.emz.pathfinder.Models.Users;
import com.emz.pathfinder.Utils.UserHelper;
import com.emz.pathfinder.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.emz.pathfinder.Utils.Ui.createSnackbar;

public class CreateProfileActivity extends AppCompatActivity {

    private static final String FRAG_TAG_DATE_PICKER = "DATEPICKER";
    private static final String TAG = CreateProfileActivity.class.getSimpleName();

    private CircleImageView profilePic;
    private ImageView cameraIcon;
    private EditText nameEt, lastNameEt, birthdateEt, genderEt, disabilityEt, telephoneEt;
    private Button createProfileBtn;

    private File newProfilePic;

    private MaterialDialog materialDialog;

    private Utils utils;

    private Calendar myCalendar;

    private int gender = -1, disability = -1;
    private String birthDate;
    ArrayList<String> disabilityOptions = new ArrayList<>();
    private boolean hasfile = false;
    private FileInputStream inputStream;
    private UserHelper usrHelper;
    private CoordinatorLayout mainView;

    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        myCalendar = Calendar.getInstance();

        utils = new Utils(this);
        usrHelper = new UserHelper(this);

        if(!usrHelper.getLoginStatus()){
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        }
        if(getIntent().getExtras() != null){
            user = (Users) getIntent().getExtras().get("user");
        }
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
        birthdateEt = findViewById(R.id.input_birthdate);
        genderEt = findViewById(R.id.input_gender);
        disabilityEt = findViewById(R.id.input_disability);
        telephoneEt = findViewById(R.id.input_telephone);
        createProfileBtn = findViewById(R.id.btn_create_profile);
        mainView = findViewById(R.id.create_profile_view);
    }

    private void setupView() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        if(user != null){
            nameEt.setText(user.getFname());
            lastNameEt.setText(user.getLname());
        }

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

        genderEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CreateProfileActivity.this)
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

        disabilityEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CreateProfileActivity.this)
                        .title("Explain how is your disability")
                        .items(disabilityOptions)
                        .itemsCallbackSingleChoice(disability, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                disability = which + 1;
                                setDisabilityText();
                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .negativeText(R.string.cancel)
                        .show();
            }
        });

        birthdateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        String mysqlFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        SimpleDateFormat mdf = new SimpleDateFormat(mysqlFormat, Locale.ENGLISH);
        birthdateEt.setText(sdf.format(myCalendar.getTime()));
        birthDate = mdf.format(myCalendar.getTime());
    }

    private void setDisabilityText() {
        disabilityEt.setText(disabilityOptions.get(disability - 1));
    }

    private void setGenderText() {
        if(gender == 0){
            genderEt.setText(R.string.male);
        }else if(gender == 1){
            genderEt.setText(R.string.female);
        }
    }

    private void createProfile(){
        final MaterialDialog md = new MaterialDialog.Builder(CreateProfileActivity.this)
                .title(R.string.progress_dialog_title)
                .content(R.string.completing)
                .progress(true, 0)
                .cancelable(false)
                .show();

        validateForm();

        String firstname = nameEt.getText().toString();
        String lastname = lastNameEt.getText().toString();
        String telephone = telephoneEt.getText().toString();

        if(newProfilePic != null){
            hasfile = true;
            try {
                inputStream = new FileInputStream(newProfilePic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(newProfilePic != null){
            //TODO: Add Upload Profile Link
            String fileName = newProfilePic.getName();
            Log.d(TAG, "UPDATEWITHFILE: "+fileName);

            Velocity.upload(utils.USER_URL+"editprofilepic/"+usrHelper.getUserId()+"/"+fileName)
                    .setUploadSource("file", "image/*", newProfilePic.getAbsolutePath())
                    .connect(new Velocity.ResponseListener() {
                        @Override
                        public void onVelocitySuccess(Velocity.Response response) {
                            startMainActivity();

                            Log.d(TAG, "UPDATEWITHFILE: "+response.body);

                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = parser.parse(response.body).getAsJsonObject();

                            boolean status = jsonObject.get("status").getAsBoolean();
                            if(status){
                                startMainActivity();
                            }else{
                                createSnackbar(mainView, "Failed to create profile. Please try again later.");
                                md.dismiss();
                            }
                        }

                        @Override
                        public void onVelocityFailed(Velocity.Response response) {
                            createSnackbar(mainView, getResources().getString(R.string.no_internet_connection));
                            md.dismiss();

                            Log.e(TAG, "UPDATEWITHFILE: "+response.body);
                            Log.e(TAG, "UPDATEWITHFILE: "+response.requestMethod);
                            Log.e(TAG, "UPDATEWITHFILE: ERROR");
                        }
                    });
        }

        Velocity.post(utils.USER_URL+"createProfile/")
                .withFormData("id", usrHelper.getUserId())
                .withFormData("firstname", firstname)
                .withFormData("lastname", lastname)
                .withFormData("gender", String.valueOf(gender))
                .withFormData("birthday", birthDate)
                .withFormData("disability", String.valueOf(disability))
                .withFormData("telephone", telephone)
                .withFormData("exp", String.valueOf(-1))
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        JsonParser parser = new JsonParser();
                        JsonObject jsonObject = parser.parse(response.body).getAsJsonObject();

                        boolean status = jsonObject.get("status").getAsBoolean();
                        if(status){
                            if(!hasfile) {
//                                startMainActivity();
                            }
                        }else{
                            if(!hasfile){
                                createSnackbar(mainView, "Failed to create profile. Please try again later.");
                                md.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        createSnackbar(mainView, getResources().getString(R.string.no_internet_connection));
                        md.dismiss();
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void validateForm() {
        //TODO: Validate Form
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
                newProfilePic = imageFile;
            }
        });
    }
}
