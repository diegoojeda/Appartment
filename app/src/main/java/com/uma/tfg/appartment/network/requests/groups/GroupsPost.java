package com.uma.tfg.appartment.network.requests.groups;

import android.text.TextUtils;

import com.uma.tfg.appartment.model.Group;
import com.uma.tfg.appartment.network.model.PostRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// ADD NEWMEMBER -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo(sacado de referee)/?session_id=234434234324' Y action => update Y type => umember
// INSERT GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/?session_id=234434234324' action => insert Y name
// UPDATE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/group_id?session_id=234434234324' Y action => update Y name => nombre nuevo
// INVITE GROUP -> 'http://appartment-pruebamarja.rhcloud.com/group/group_id?session_id=234434234324'  Y action => invite Y emails => array()
// DELETE group -> 'http://appartment-pruebamarja.rhcloud.com/group/id_grupo?session_id=234434234324' Y action => delete

public class GroupsPost extends PostRequest {

    public interface GroupsPostListener{
        void onGroupsPostSuccess(GroupsPostAction action, Group modifiedGroup);
        void onGroupsPostError();
    }

    public enum GroupUpdateType {
        UMEMBER("umember"),
        DMEMBER("dmember");

        private String type;

        GroupUpdateType(String type){
            this.type = type;
        }

        public String toString() {
            return this.type;
        }
    }

    public enum GroupsPostAction {
        INSERT("insert"),
        INVITE("invite"),
        UPDATE("update"),
        DELETE("delete");

        private String action;

        GroupsPostAction(String action){
            this.action = action;
        }

        public String toString() {
            return action;
        }
    }

    private GroupsPostAction mAction;
    public GroupUpdateType mUpdateType;
    public String mName;
    public String mId;
    public List<String> mEmails;

    public GroupsPostListener mListener;

    public GroupsPost(GroupsPostAction action){
        this.mAction = action;
    }

    public GroupsPost(GroupUpdateType type) {
        this.mAction = GroupsPostAction.UPDATE;
        this.mUpdateType = type;
    }

    @Override
    public List<NameValuePair> getPostParameters() {
        List<NameValuePair> parameters = new ArrayList<>();
        switch(mAction){
            case INSERT:
                parameters.add(new BasicNameValuePair("name", mName));
                break;
            case INVITE:
                //TODO

                parameters.add(new BasicNameValuePair("emails", new JSONArray(mEmails).toString()));
                break;
            case UPDATE:
                if (mUpdateType == GroupUpdateType.UMEMBER || mUpdateType == GroupUpdateType.DMEMBER){
                    parameters.add(new BasicNameValuePair("type", mUpdateType.toString()));
                }
                break;
        }
        return parameters;
    }

    @Override
    public String getAction() {
        return mAction.toString();
    }

    @Override
    public Entity getEntity() {
        return Entity.GROUP;
    }

    @Override
    public List<String> getUrlPath() {
        if (mAction == GroupsPostAction.INVITE || mAction == GroupsPostAction.UPDATE
                || mAction == GroupsPostAction.DELETE){
            if (!TextUtils.isEmpty(mId)) {
                List<String> urlPath = new ArrayList<>();
                urlPath.add(mId);
                return urlPath;
            }
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
            Group g = null;
            if (response.has("inserted")){
                g = new Group(response.getJSONObject("inserted"));

            }
            if (mListener != null){
                mListener.onGroupsPostSuccess(mAction, g);
            }
        }
        if (mListener != null) {
            if (failed) {
                mListener.onGroupsPostError();
            }
        }

    }
}
