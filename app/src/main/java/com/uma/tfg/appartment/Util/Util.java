package com.uma.tfg.appartment.util;

import android.text.TextUtils;

public class Util {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
