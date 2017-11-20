package com.emz.pathfinder.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.emz.pathfinder.StartActivity;

import static android.support.v4.content.ContextCompat.startActivity;

public class Utils {

    public static final String LOGIN_URL = "https://www.pathfinder.in.th/home/login/";
    public static final String REGISTER_URL = "https://www.pathfinder.in.th/registration/signup/";
    public static final String USER_URL = "https://www.pathfinder.in.th/dist/home/users.php";
    public static final String JOBS_URL = "https://www.pathfinder.in.th/dist/home/jobs.php";
    public static final String PROFILEPIC_URL = "https://www.pathfinder.in.th/uploads/profile_image/";
    public static final String EMPPIC_URL = "https://www.pathfinder.in.th/uploads/logo_images/";
    public static final String UTILITIES_URL = "https://www.pathfinder.in.th/utilities/";

    @NonNull
    public static String convertString(EditText editText) {
        return editText.getText().toString();
    }
}