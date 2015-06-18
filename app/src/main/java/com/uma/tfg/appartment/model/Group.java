package com.uma.tfg.appartment.model;

import com.uma.tfg.appartment.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {

    public String mId;
    public String mGroupName;
    public String mCdt;
    public int mCount;

    public String mCreatorId;

    public List<User> mMembers;

    public Group (JSONObject jsonGroup){
        mId = JSONUtils.getStringFromJSONObject("id", jsonGroup);
        mGroupName = JSONUtils.getStringFromJSONObject("name", jsonGroup);
        mCdt = JSONUtils.getStringFromJSONObject("cdt", jsonGroup);
        mCreatorId = JSONUtils.getStringFromJSONObject("creator", jsonGroup);
        mCount = JSONUtils.getIntFromJSONObject("nmembers", jsonGroup);

        JSONArray membersJSONArray = JSONUtils.getJSONArrayFromJSONObject("members", jsonGroup);
        if (membersJSONArray != null) {
            mMembers = new ArrayList<>();
            for (int i = 0; i < membersJSONArray.length(); i++) {
                mMembers.add(new User(JSONUtils.getJSONObjectFromJSONArray(membersJSONArray, i)));
            }
        }
    }

    public Group(String id, String groupName, String creatorId, List<User> groupMembers) {
        mId = id;
        mGroupName = groupName;
        mCreatorId = creatorId;
        mMembers = groupMembers;
    }
}
