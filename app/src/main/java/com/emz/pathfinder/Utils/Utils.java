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
