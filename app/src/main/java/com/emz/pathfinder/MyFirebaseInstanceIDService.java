package com.emz.pathfinder;

import android.util.Log;

import com.emz.pathfinder.Utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIDService";
    private Utils utils;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        utils = new Utils(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token:" + refreshedToken);

        utils.sendRegistrationToServer(refreshedToken, this);
    }
}
