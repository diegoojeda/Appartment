package com.uma.tfg.appartment.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class Util {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void toast(Context c, String text){
        Toast.makeText(c, text, Toast.LENGTH_LONG).show();
    }
}
