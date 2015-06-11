package com.uma.tfg.appartment.model;

import com.uma.tfg.appartment.util.JSONUtils;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    public String userId;
    public String userEmail;
    public String userName;
    public String userPictureUrl;

    public User(String userId, String userEmail, String userName, String userPictureUrl) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPictureUrl = userPictureUrl;
    }

    public User(JSONObject userJSON){
        if (userJSON != null) {
            this.userId = JSONUtils.getStringFromJSONObject("gplus", userJSON);
            this.userName = JSONUtils.getStringFromJSONObject("name", userJSON);
            this.userEmail = JSONUtils.getStringFromJSONObject("email", userJSON);
            this.userPictureUrl = JSONUtils.getStringFromJSONObject("picture", userJSON);
        }
    }

    @Override
    public String toString() {
        return "\nNombre: " + userName + "\nE-mail: " + userEmail + "\nUserId: " + userId + "\n";
    }
}
