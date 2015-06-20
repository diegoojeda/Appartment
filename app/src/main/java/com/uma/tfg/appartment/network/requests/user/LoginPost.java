package com.uma.tfg.appartment.network.requests.user;

import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
* $id = "1111"; //ID GPLUS
  $lang = "es_Es";
  $name = "Alvaro Marjalizo";
  $email = "Amarjalizo89@gmail.com";
* */

/*
* Devuelve únicamente RC
* */
public class LoginPost extends PostRequest {

    public interface LoginPostListener {
        void onLoginPostSuccess();
        void onLoginPostError();
    }

    private String mUserId;
    private String mUserEmail;
    private String mUserName;
    private String mUserPicture;

    private LoginPostListener mListener;

    public LoginPost(String userId, String userEmail, String userName, String userPicture, LoginPostListener listener) {
        this.mUserId = userId;
        this.mUserEmail = userEmail;
        this.mUserName = userName;
        this.mUserPicture = userPicture;
        this.mListener = listener;
    }

    @Override
    public List<NameValuePair> getPostParameters() {
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("id", mUserId));
        arguments.add(new BasicNameValuePair("lang", Locale.getDefault().toString())); //TODO
        arguments.add(new BasicNameValuePair("picture", mUserPicture));
        arguments.add(new BasicNameValuePair("name", mUserName));
        arguments.add(new BasicNameValuePair("email", mUserEmail));

        return arguments;
    }

    @Override
    public Entity getEntity() {
        return Entity.USER;
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
    public String getAction() {
        return "login";
    }

    @Override
    public void processResponse(JSONObject response) throws JSONException{
        boolean failed = true;
        int rc = response.getInt("rc");
        if (rc == 0){
            failed = false;
        }
        if (mListener != null){
            if (failed){
                mListener.onLoginPostError();
            }
            else{
                mListener.onLoginPostSuccess();
            }
        }
    }
}
