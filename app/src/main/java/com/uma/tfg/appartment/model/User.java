package com.uma.tfg.appartment.model;

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

    @Override
    public String toString() {
        return "\nNombre: " + userName + "\nE-mail: " + userEmail + "\nUserId: " + userId + "\n";
    }
}
