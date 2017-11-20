package com.emz.pathfinder;

import android.util.Log;

import com.emz.pathfinder.Utils.UserHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rw.velocity.Velocity;

import static com.emz.pathfinder.Utils.Utils.UTILITIES_URL;

/**
 * Created by im_ae on 20/11/2560.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token:" + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        UserHelper usrHelper = new UserHelper(this);

        if(usrHelper.getLoginStatus()){
            String id = usrHelper.getUserId();
            Velocity.post(UTILITIES_URL+"setTokens")
                    .withFormData("id", id)
                    .withFormData("token", refreshedToken)
                    .connect(new Velocity.ResponseListener() {
                        @Override
                        public void onVelocitySuccess(Velocity.Response response) {
                            Log.d(TAG, "Registered Token");
                        }

                        @Override
                        public void onVelocityFailed(Velocity.Response response) {
                            Log.e(TAG, "Failed to Registered Token");
                        }
                    });
        }

    }
}
