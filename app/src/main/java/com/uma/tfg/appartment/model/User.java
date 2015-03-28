package com.uma.tfg.appartment.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userEmail;
    private String userName;
    private String userPictureUrl;

    public User(String userId, String userEmail, String userName, String userPictureUrl) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPictureUrl = userPictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPictureUrl() {
        return userPictureUrl;
    }

    public void setUserPictureUrl(String userPictureUrl) {
        this.userPictureUrl = userPictureUrl;
    }

    @Override
    public String toString() {
        return "\nNombre: " + userName + "\nE-mail: " + userEmail + "\nUserId: " + userId + "\n";
    }
}
