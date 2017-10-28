package com.emz.pathfinder.Utils;

import android.content.Context;
import com.emz.pathfinder.Models.UserModel;
import com.rw.velocity.Velocity;
import java.util.Objects;
import static com.emz.pathfinder.Utils.Utils.AUTH_URL;

public class Auth {

    private UserModel users;
    private UserHelper usrHelper;

    private String json;

    public Auth(Context context){
        usrHelper = new UserHelper(context);
    }

    public String userLoader(int id){
        String uid = String.valueOf(id);
        Velocity.get(AUTH_URL)
                .withHeader("id", uid)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        json = response.body;
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
        return json;
    }
}
