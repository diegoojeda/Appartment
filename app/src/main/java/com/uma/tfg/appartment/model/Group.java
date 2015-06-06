package com.uma.tfg.appartment.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {

    public String mId;
    public String mGroupName;

    public List<User> mMembers;

    public Group (JSONObject jsonGroup){
        try {
            mId = jsonGroup.getString("id");
            mGroupName = jsonGroup.getString("name");
            if (jsonGroup.has("members")){
                mMembers = new ArrayList<>();
                JSONArray membersJSONArray = jsonGroup.getJSONArray("members");
                for (int i = 0; i<membersJSONArray.length(); i++){
                    mMembers.add(new User(membersJSONArray.getJSONObject(i)));
                }
            }
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public Group(String id, String groupName, List<User> groupMembers) {
        mId = id;
        mGroupName = groupName;
        mMembers = groupMembers;
    }

}
