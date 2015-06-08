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
    public String mCdt;

    public String mCreatorId;

    public List<String> mMembersIds;

    public Group (JSONObject jsonGroup){
        try {
            mId = jsonGroup.getString("_id");
            mGroupName = jsonGroup.getString("name");
            mCdt = jsonGroup.getString("cdt");
            mCreatorId = jsonGroup.getString("creator");

            if (jsonGroup.has("members")){
                mMembersIds = new ArrayList<>();
                JSONArray membersJSONArray = jsonGroup.getJSONArray("members");
                for (int i = 0; i<membersJSONArray.length(); i++){
                    mMembersIds.add(membersJSONArray.getString(i));
                }
            }
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public Group(String id, String groupName, String creatorId, List<String> groupMembers) {
        mId = id;
        mGroupName = groupName;
        mCreatorId = creatorId;
        mMembersIds = groupMembers;
    }

}
