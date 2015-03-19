package com.uma.tfg.appartment.Util;

import android.util.Log;

import com.uma.tfg.appartment.Constants;

public class Logger {

    public static void d (String text){
        Log.d(Constants.APP_LOG_TAG, text);
    }

    public static void e (String text){
        Log.e(Constants.APP_LOG_TAG, text);
    }

    public static void w (String text){
        Log.w(Constants.APP_LOG_TAG, text);
    }
}
