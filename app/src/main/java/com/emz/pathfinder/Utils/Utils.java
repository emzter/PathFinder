package com.emz.pathfinder.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.emz.pathfinder.Adapters.FeaturedJobAdapter;
import com.emz.pathfinder.Adapters.TimelineAdapter;
import com.emz.pathfinder.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rw.velocity.Velocity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {

    public final String LOGIN_URL = "https://www.pathfinder.in.th/home/login/";
    public final String REGISTER_URL = "https://www.pathfinder.in.th/registration/signup/";
    public final String USER_URL = "https://www.pathfinder.in.th/dist/home/users.php";
    public final String JOBS_URL = "https://www.pathfinder.in.th/dist/home/jobs.php";
    public final String PROFILEPIC_URL = "https://www.pathfinder.in.th/uploads/profile_image/";
    public final String HEADERPIC_URL = "https://www.pathfinder.in.th/uploads/header_images/";
    public final String EMPPIC_URL = "https://www.pathfinder.in.th/uploads/logo_images/";
    public final String UTILITIES_URL = "https://www.pathfinder.in.th/utilities/";
    public final String TIMELINE_URL = "https://www.pathfinder.in.th/timeline/";

    private Context context;

    public Utils(Context current){
        this.context = current;
    }

    @NonNull
    public String convertString(EditText editText) {
        return editText.getText().toString();
    }

    public void sendRegistrationToServer(String refreshedToken, Context context) {
        final String TAG = "MyFirebaseIDService";

        UserHelper usrHelper = new UserHelper(context);

        if(usrHelper.getLoginStatus()){
            String id = usrHelper.getUserId();
            Velocity.post(UTILITIES_URL+"newtoken")
                    .withFormData("id", id)
                    .withFormData("token", refreshedToken)
                    .connect(new Velocity.ResponseListener() {
                        @Override
                        public void onVelocitySuccess(Velocity.Response response) {
                            Log.d(TAG, response.body);
                            Log.d(TAG, "Registered Token");
                        }

                        @Override
                        public void onVelocityFailed(Velocity.Response response) {
                            Log.e(TAG, response.body);
                            Log.e(TAG, "Failed to Registered Token");
                        }
                    });
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public String gettimestamp(String time){
        int granularity = 1;
        long oldtime;
        String newtime = "";
        long date = getDateInMillis(time);
        long current = Calendar.getInstance().getTimeInMillis() / 1000;
        long different = current - date;
        LinkedHashMap<String, Long> peroids = getPeriodsTime();
        int i = 1;

        for(Map.Entry<String, Long> entry : peroids.entrySet()){
            String key = entry.getKey();
            Long value = entry.getValue();
            if (different >= value) {
                oldtime = (long) Math.floor(different / value);
                different = different%value;
                newtime = ((!Objects.equals(newtime, "")) ? " " : "")+oldtime+" "+key;
                granularity--;
            }
            if(granularity == 0) break;
        }

        return newtime+" "+context.getString(R.string.ago);
    }

    private LinkedHashMap<String, Long> getPeriodsTime(){
        LinkedHashMap<String, Long> periods = new LinkedHashMap<>();

        periods.put(context.getString(R.string.decades), (long) 315360000);
        periods.put(context.getString(R.string.years), (long) 31536000);
        periods.put(context.getString(R.string.months), (long) 2628000);
        periods.put(context.getString(R.string.weeks), (long) 604800);
        periods.put(context.getString(R.string.days), (long) 86400);
        periods.put(context.getString(R.string.hours), (long) 3600);
        periods.put(context.getString(R.string.minutes), (long) 60);
        periods.put(context.getString(R.string.seconds), (long) 1);

        return periods;
    }

    public String getGender(int sex){
        String gender = context.getString(R.string.male);
        switch (sex){
            case 0:
                gender = context.getString(R.string.male);
                break;
            case 1:
                gender = context.getString(R.string.female);
                break;
        }
        return gender;
    }

    private long getDateInMillis(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;

        try {
            Date date = format.parse(time);
            dateInMillis = date.getTime();
            return dateInMillis/1000;
        } catch (ParseException e) {
            Log.d("Exception", e.getMessage());
            e.printStackTrace();
        }

        return dateInMillis;
    }

    public void deleteToken(String token, String userId) {
        final String TAG = "DeleteTokenMethod";

        Velocity.post(UTILITIES_URL+"deleteToken")
                .withFormData("id", userId)
                .withFormData("token", token)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        Log.d(TAG, response.body);
                        Log.d(TAG, "Token Deleted");
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response response) {
                        Log.e(TAG, response.body);
                        Log.e(TAG, "Error Deleting Token");
                    }
                });
    }
}