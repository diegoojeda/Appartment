package com.uma.tfg.appartment.model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

    public String mId;
    public String mGroupName;

    public List<String> mMembers;//TODO Esto se sustituirá probablemente por List<User>. Está así para probar

    public Group(String id, String groupName, List<String> groupMembers) {
        mId = id;
        mGroupName = groupName;
        mMembers = groupMembers;
    }

}
