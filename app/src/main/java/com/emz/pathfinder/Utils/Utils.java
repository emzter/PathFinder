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

    public static final String URL_STORAGE_REFERENCE = "gs://mechat-515c9.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";
    public static final String FOLDER_STORAGE_PROFILE_IMG = "images/profile-img/";

    public static final String LOGIN_URL = "http://192.168.1.2/dist/home/login.php";
    public static final String REGISTER_URL = "http://192.168.1.2/dist/home/register.php";

    @NonNull
    public static String convertString(EditText editText) {
        return editText.getText().toString();
    }

    public static boolean verifyConnectivity(Context context) {
        boolean connection;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connection = connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
        return connection;
    }
}
