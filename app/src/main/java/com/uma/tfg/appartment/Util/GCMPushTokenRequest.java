package com.uma.tfg.appartment.util;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GCMPushTokenRequest {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    enum GCMPushTokenError {
        NO_PLAY_SERVICES_APK,
        GENERAL_ERROR;
    }

    public interface PushTokenRequestListener {
        void onPushTokenReceivedSuccessfully(String pushToken);
        void onErrorFetchingPushToken(GCMPushTokenError error);
    }

    private Activity mActivity;
    private PushTokenRequestListener mListener;

    public GCMPushTokenRequest(Activity activity, PushTokenRequestListener listener) {
        this.mActivity = activity;
        this.mListener = listener;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Logger.d("This device is not supported.");
                if (mListener != null){
                    mListener.onErrorFetchingPushToken(GCMPushTokenError.NO_PLAY_SERVICES_APK);
                }
            }
            return false;
        }
        return true;
    }

}
