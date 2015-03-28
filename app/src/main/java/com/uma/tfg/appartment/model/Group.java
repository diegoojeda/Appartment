package com.uma.tfg.appartment.model;

import java.io.Serializable;

public class Group implements Serializable {

    public String mId;
    public String mGroupName;

    public Group(String mId, String mGroupName) {
        this.mId = mId;
        this.mGroupName = mGroupName;
    }

}
