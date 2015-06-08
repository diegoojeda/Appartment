package com.uma.tfg.appartment.network.requests.groups;

import android.text.TextUtils;

import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.model.GetRequest;
import com.uma.tfg.appartment.network.model.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// GET GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo/?session_id=234434234324'

public class GroupDetailsGet extends GetRequest {

    public interface GroupDetailsGetListener {
        void onGroupDetailsLoaded(Group group);
        void onGroupDetailsError();
    }

    private String mGroupId;
    private GroupDetailsGetListener mListener;

    public GroupDetailsGet (String groupId, GroupDetailsGetListener listener){
        this.mGroupId = groupId;
        this.mListener = listener;
    }

    @Override
    public Entity getEntity() {
        return Entity.GROUP;
    }

    @Override
    public List<String> getUrlPath() {
        if (!TextUtils.isEmpty(mGroupId)){
            List<String> urlPath = new ArrayList<>();
            urlPath.add(mGroupId);
            return urlPath;
        }
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
            failed = false;
            Group singleGroup = new Group(response.getJSONObject("group"));
            if (mListener != null){
                mListener.onGroupDetailsLoaded(singleGroup);
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onGroupDetailsError();
            }
        }
    }
}
