package com.uma.tfg.appartment.network.requests;

import android.os.Build;

import com.uma.tfg.appartment.AppartmentSharedPreferences;
import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/*
* $hw_id = "c9f6f6474a5ee9240";
  $os_version = "4.3";
  $platform = "android";
  $app_version = "1";
  $dev_brand = "motorola";
  $dev_model = "XT1032";
  $lang = "es_Es";
  $token = "APA91bEOt5I22gyqzVdNiIOgioSpUAw0b5mSO1rFOiTh3muiWfJ5b9_4c8CdJ8vJkyGShnW6f-jnHt53xrB3GNAzpfCjTg2ok7bE_ROAc7VlSv-OmdwjXo5Z678ExBe2dEcJpwk82-zrgBrTNCrbhShqt3Ca-razCA";
*
* */

/*
* Devuelve RC y session_id
* */

public class IniPost extends PostRequest {

    public interface IniPostListener {
        void onIniSuccess();
        void onIniError();
    }

    private String mDeviceId;
    private String mLang;
    private String mPushtoken;
    private int mAppVersion;

    private IniPostListener mListener;

    public IniPost(String deviceId, String lang, String pushToken, int appVersion, IniPostListener listener) {
        this.mDeviceId = deviceId;
        this.mLang = lang;
        this.mPushtoken = pushToken;
        this.mAppVersion = appVersion;
        this.mListener = listener;
    }

    @Override
    public String getAction() {
        return "ini";
    }

    @Override
    public List<NameValuePair> getPostParameters() {
        List<NameValuePair> parameters = new ArrayList<>();

        parameters.add(new BasicNameValuePair("hw_id", mDeviceId));
        parameters.add(new BasicNameValuePair("os_version", Build.VERSION.INCREMENTAL));
        parameters.add(new BasicNameValuePair("platform", "android"));
        parameters.add(new BasicNameValuePair("app_version", Integer.toString(mAppVersion)));
        parameters.add(new BasicNameValuePair("dev_brand", Build.MANUFACTURER));
        parameters.add(new BasicNameValuePair("dev_model", Build.MODEL));
        parameters.add(new BasicNameValuePair("lang", mLang));
        parameters.add(new BasicNameValuePair("token", mPushtoken));

        return parameters;
    }

    @Override
    public Entity getEntity() {
        return Entity.INI;
    }

    @Override
    public List<String> getUrlPath() {
        return null;
    }

    @Override
    public JSONObject getUrlQueryParameters() {
        return null;
    }

    @Override
    public void processResponse(JSONObject response) throws JSONException{
        boolean failed = true;
        int rc = response.getInt("rc");
        if (rc == 0){
            failed = false;
            String sessionId = response.getString("session_id");
            AppartmentSharedPreferences.setSessionId(sessionId);
        }
        if (mListener != null){
            if (failed){
                mListener.onIniError();
            }
            else{
                mListener.onIniSuccess();
            }
        }

    }
}
