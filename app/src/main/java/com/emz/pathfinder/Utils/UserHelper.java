package com.emz.pathfinder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EMZ on 27/10/2560.
 */

public class UserHelper {
    Context context;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    static String prefsName = "CUser";
    static int prefsMode = 0;

    public UserHelper(Context context){
        this.context = context;
        this.sharedPrefs = this.context.getSharedPreferences(prefsName, prefsMode);
        this.editor = sharedPrefs.edit();
    }

    public void createSession(String uid){
        editor.putBoolean("LoginStatus", true);
        editor.putString("user_id", uid);

        editor.commit();
    }

    public void deleteSession(){
        editor.clear();
        editor.commit();
    }

    public boolean getLoginStatus(){
        return sharedPrefs.getBoolean("LoginStatus", false);
    }

    public String getUserId(){
        return sharedPrefs.getString("user_id", null);
    }
}
