package com.uma.tfg.appartment;

import android.content.Context;
import android.content.SharedPreferences;

public class AppartmentSharedPreferences {

    private static final String SHARED_PREFERENCES_FILE_NAME = "SettingsSharedPreferences";

    private static Context mContext;

    public static void initSharedPreferences(Context context) {
        mContext = context;
    }

    public static void setSessionId(String sessionId){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sessionId", sessionId);
        editor.apply();
    }

    public static String getSessionId() {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("sessionId", null);
    }

    public static void setUserId(String userId){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public static String getUserId(){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("userId", null);
    }

    public static void setUserName(String userName){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public static String getUserName(){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("userName", null);
    }

    public static void setUserEmail(String userEmail){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userEmail", userEmail);
        editor.apply();
    }

    public static String getUserEmail(){
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("userEmail", null);
    }

    public static boolean isUserLoggedIn() {
        return getUserEmail() != null && getUserId() != null && getUserName() != null;
    }
}
