package com.cybertoper.journeys.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cybertoper.journeys.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by DRX on 9/9/2015.
 */
public class SessionManager {

    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    private static final String PREF_NAME = "JourneysPref";
    public static final String KEY_MAIL = "email";
    public static final String KEY_PASS = "password";
    private static final String IS_LOGIN = "IsLoggedIn";

    //GCM variables
    private static final String SENT_TOKEN_TO_SERVER="TokenSentOrNot";
    private static final String REGISTRATION_COMPLETE="RegStatus";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    // Added later for GCM services
    public void addTokenData(){
        editor.putBoolean(SENT_TOKEN_TO_SERVER,false);
        editor.putBoolean(REGISTRATION_COMPLETE,false);
    }

    //Creating Login Session
    public void createLoginSession(String email, String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MAIL, email);
        editor.putString(KEY_PASS, password);
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            //Redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
        else{
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_MAIL, pref.getString(KEY_MAIL, null));
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));
        return user;
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
