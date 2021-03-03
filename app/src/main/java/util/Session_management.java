package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import codecanyon.jagatpharma.LoginActivity;

import static Config.BaseURL.IS_LOGIN;
import static Config.BaseURL.KEY_ADDRESS;
import static Config.BaseURL.KEY_BDATE;
import static Config.BaseURL.KEY_CITY;
import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_GENDER;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_IMAGE;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;
import static Config.BaseURL.KEY_REFERALCODE;
import static Config.BaseURL.KEY_TYPE_ID;
import static Config.BaseURL.PREFS_NAME;

/**
 * Created by Rajesh on 2017-09-08.
 */

public class Session_management {

    SharedPreferences prefs;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void createLoginSession(String id, String email, String name
            , String type_id, String bdate, String mobile, String image,
                                   String gender, String address, String city) {

        if (!id.isEmpty()) {
            editor.putBoolean(IS_LOGIN, true);
        }

        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_TYPE_ID, type_id);
        editor.putString(KEY_BDATE, bdate);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.commit();
    }

    public void createLoginSession(String id, String email, String name
            , String type_id, String bdate, String mobile, String image,
                                   String gender, String address, String city,String code) {

        if (!id.isEmpty()) {
            editor.putBoolean(IS_LOGIN, true);
        }

        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_TYPE_ID, type_id);
        editor.putString(KEY_BDATE, bdate);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_REFERALCODE, code);

        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));

        user.put(KEY_TYPE_ID, prefs.getString(KEY_TYPE_ID, null));

        user.put(KEY_BDATE, prefs.getString(KEY_BDATE, null));

        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));

        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));

        user.put(KEY_GENDER, prefs.getString(KEY_GENDER, null));

        user.put(KEY_ADDRESS, prefs.getString(KEY_ADDRESS, null));

        user.put(KEY_CITY, prefs.getString(KEY_CITY, null));

        user.put(KEY_REFERALCODE, prefs.getString(KEY_REFERALCODE, null));

        // return user
        return user;
    }

    public void updateData(String name, String gender, String bdate, String mobile, String address, String city) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_BDATE, bdate);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);

        editor.apply();
    }

    public void updateImage(String image) {
        editor.putString(KEY_IMAGE, image);

        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);


    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
