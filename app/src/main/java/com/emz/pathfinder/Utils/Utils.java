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

    public static final String AUTH_URL = "http://pathfinder.webstarterz.com/dist/home/auth.php";
    public static final String USER_URL = "http://pathfinder.webstarterz.com/dist/home/users.php";
    public static final String JOBS_URL = "http://pathfinder.webstarterz.com/dist/home/jobs.php";
    public static final String PROFILEPIC_URL = "http://pathfinder.webstarterz.com/uploads/profile_image/";
    public static final String EMPPIC_URL = "http://pathfinder.webstarterz.com/uploads/logo_images/";

    @NonNull
    public static String convertString(EditText editText) {
        return editText.getText().toString();
    }
}
