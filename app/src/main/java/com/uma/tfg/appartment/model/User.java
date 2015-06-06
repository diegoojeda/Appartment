package com.uma.tfg.appartment.model;

import org.json.JSONException;
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
        try{
            this.userId = userJSON.getString("id");
            this.userEmail = userJSON.getString("email");
            this.userName = userJSON.getString("name");
            this.userPictureUrl = userJSON.getString("picture");
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "\nNombre: " + userName + "\nE-mail: " + userEmail + "\nUserId: " + userId + "\n";
    }
}
