package com.emz.pathfinder.Utils;

import android.content.Context;
import com.emz.pathfinder.Models.UserModel;
import com.rw.velocity.Velocity;
import java.util.Objects;
import static com.emz.pathfinder.Utils.Utils.AUTH_URL;

public class Auth {

    private UserModel users;
    private UserHelper usrHelper;

    public Auth(Context context){
        usrHelper = new UserHelper(context);
    }

    public static void userLoader(int id){
        String uid = String.valueOf(id);
        Velocity.get(AUTH_URL)
                .withHeader("id", uid)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {

                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }

    public void login(String email, String password){
        boolean valid = false;
        Velocity.post(AUTH_URL)
                .withFormData("status","login")
                .withFormData("email",email)
                .withFormData("pass",password)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        if(!Objects.equals(response.body, "Failed")){
                            usrHelper.createSession(response.body);
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                    }
                });
    }

    public void register(String email, String password, String name, String lname){
        Velocity.post(AUTH_URL)
                .withFormData("status","register")
                .withFormData("email",email)
                .withFormData("pass",password)
                .withFormData("name",password)
                .withFormData("lastname",password)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {

                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {

                    }
                });
    }
}
