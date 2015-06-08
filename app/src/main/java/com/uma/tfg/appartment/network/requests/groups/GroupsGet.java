package com.uma.tfg.appartment.network.requests.groups;

import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.model.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// GET GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324'

public class GroupsGet extends GetRequest {

    public interface GroupsGetListener {
        void onGroupsReceivedSuccessfully(List<Group> groups);
        void onErrorReceivingGroups();
    }

    private GroupsGetListener mListener;

    public GroupsGet(GroupsGetListener listener){
        this.mListener = listener;
    }

    @Override
    public Entity getEntity() {
        return Entity.GROUP;
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
    public void processResponse(JSONObject response) throws JSONException {
        boolean failed = true;
        int rc = response.getInt("rc");
        if (rc == 0){
            if (response.has("groups")) {
                failed = false;
                List<Group> groupsList = new ArrayList<>();
                JSONArray groupsJSONArray = response.getJSONArray("groups");
                for (int i = 0; i < groupsJSONArray.length(); i++) {
                    groupsList.add(new Group(groupsJSONArray.getJSONObject(i)));
                }
                if (mListener != null) {
                    mListener.onGroupsReceivedSuccessfully(groupsList);
                }
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onErrorReceivingGroups();
            }
        }
    }
}
